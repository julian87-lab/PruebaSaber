package com.pruebasaber.app.controller;

import com.pruebasaber.app.dto.BeneficioAcademicoDTO;
import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Resultado;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.service.BeneficioAcademicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private final ResultadoRepository resultadoRepository;
    private final BeneficioAcademicoService beneficioAcademicoService;

    public DocenteController(ResultadoRepository resultadoRepository,
                             BeneficioAcademicoService beneficioAcademicoService) {
        this.resultadoRepository = resultadoRepository;
        this.beneficioAcademicoService = beneficioAcademicoService;
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model) {
        List<Resultado> resultados = resultadoRepository.findAllConEstudiante();
        List<BeneficioFila> beneficios = new ArrayList<>();

        for (Resultado resultado : resultados) {
            Estudiante estudiante = resultado.getEstudiante();
            BeneficioAcademicoDTO beneficio = beneficioAcademicoService.evaluarBeneficio(estudiante, resultado);

            if (beneficio != null && beneficio.isAplica()) {
                beneficios.add(new BeneficioFila(estudiante, resultado, beneficio));
            }
        }

        model.addAttribute("titulo", "Informe de Beneficios");
        model.addAttribute("rol", "DOCENTE");
        model.addAttribute("beneficios", beneficios);
        model.addAttribute("totalBeneficiados", beneficios.size());

        return "docente/informe-beneficios";
    }

    @GetMapping("/resolucion-beneficios")
    public String resolucionBeneficios(Model model) {
        model.addAttribute("titulo", "Resolución Beneficios Tecnología e Ingeniería");
        model.addAttribute("rol", "DOCENTE");
        model.addAttribute("nombrePdf", "ACUERDO-No.-01-009-CONSEJO-DIRECTIVO-Reglamento-de-Estimulos.pdf");

        return "docente/resolucion-beneficios";
    }

    public static class BeneficioFila {
        private final Estudiante estudiante;
        private final Resultado resultado;
        private final BeneficioAcademicoDTO beneficio;

        public BeneficioFila(Estudiante estudiante, Resultado resultado, BeneficioAcademicoDTO beneficio) {
            this.estudiante = estudiante;
            this.resultado = resultado;
            this.beneficio = beneficio;
        }

        public Estudiante getEstudiante() {
            return estudiante;
        }

        public Resultado getResultado() {
            return resultado;
        }

        public BeneficioAcademicoDTO getBeneficio() {
            return beneficio;
        }
    }
}