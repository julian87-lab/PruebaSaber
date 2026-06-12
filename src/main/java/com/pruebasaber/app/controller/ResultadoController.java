package com.pruebasaber.app.controller;

import com.pruebasaber.app.models.Resultado;
import com.pruebasaber.app.repository.ResultadoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/resultados")
public class ResultadoController {

    private final ResultadoRepository resultadoRepository;

    public ResultadoController(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q,
                         @RequestParam(required = false) String periodo,
                         Model model) {

        String textoBusqueda = q != null ? q.trim() : "";
        String periodoFiltro = periodo != null ? periodo.trim() : "";

        List<Resultado> todos = resultadoRepository.findAllConEstudiante();
        List<Resultado> resultados;

        boolean hayBusqueda = !textoBusqueda.isBlank();
        boolean hayPeriodo = !periodoFiltro.isBlank();

        if (hayBusqueda && hayPeriodo) {
            resultados = resultadoRepository.buscarPorPeriodoConEstudiante(periodoFiltro, textoBusqueda);
        } else if (hayBusqueda) {
            resultados = resultadoRepository.buscarConEstudiante(textoBusqueda);
        } else if (hayPeriodo) {
            resultados = resultadoRepository.findByPeriodoConEstudiante(periodoFiltro);
        } else {
            resultados = todos;
        }

        Set<String> periodos = new LinkedHashSet<>();
        todos.stream()
                .map(Resultado::getPeriodo)
                .filter(p -> p != null && !p.isBlank())
                .sorted(Comparator.reverseOrder())
                .forEach(periodos::add);

        long totalResultados = todos.size();

        long totalNivelAlto = todos.stream()
                .filter(r -> r.getNivelPuntajeGlobal() != null)
                .filter(r -> r.getNivelPuntajeGlobal().equalsIgnoreCase("Nivel 4")
                          || r.getNivelPuntajeGlobal().equalsIgnoreCase("Nivel 3"))
                .count();

        double promedioGlobal = todos.stream()
                .filter(r -> r.getPuntajeGlobal() != null)
                .mapToDouble(Resultado::getPuntajeGlobal)
                .average()
                .orElse(0.0);

        double mejorPuntaje = todos.stream()
                .filter(r -> r.getPuntajeGlobal() != null)
                .mapToDouble(Resultado::getPuntajeGlobal)
                .max()
                .orElse(0.0);

        model.addAttribute("resultados", resultados);
        model.addAttribute("periodos", periodos);
        model.addAttribute("q", textoBusqueda);
        model.addAttribute("periodo", periodoFiltro);
        model.addAttribute("totalResultados", totalResultados);
        model.addAttribute("totalNivelAlto", totalNivelAlto);
        model.addAttribute("promedioGlobal", Math.round(promedioGlobal));
        model.addAttribute("mejorPuntaje", Math.round(mejorPuntaje));

        return "resultados/resultados";
    }
}