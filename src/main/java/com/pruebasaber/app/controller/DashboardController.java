package com.pruebasaber.app.controller;

import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Resultado;
import com.pruebasaber.app.models.Usuario;
import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.repository.UsuarioRepository;
import com.pruebasaber.app.service.BeneficioAcademicoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.pruebasaber.app.dto.BeneficioAcademicoDTO;

import java.util.Comparator;
import java.util.List;

@Controller
public class DashboardController {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository resultadoRepository;
    private final BeneficioAcademicoService beneficioAcademicoService;

    public DashboardController(UsuarioRepository usuarioRepository,
                               EstudianteRepository estudianteRepository,
                               ResultadoRepository resultadoRepository,
                               BeneficioAcademicoService beneficioAcademicoService) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository = resultadoRepository;
        this.beneficioAcademicoService = beneficioAcademicoService;
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("titulo", "Panel Administrador");
        model.addAttribute("rol", "ADMIN");
        model.addAttribute("usuariosTotal", usuarioRepository.count());
        model.addAttribute("estudiantesTotal", estudianteRepository.count());
        model.addAttribute("estudiantesActivos", estudianteRepository.countByActivoTrue());
        model.addAttribute("estudiantesInactivos", estudianteRepository.countByActivoFalse());
        model.addAttribute("resultadosTotal", resultadoRepository.count());
        return "dashboard/dashboard";
    }

    @GetMapping("/dashboard/docente")
    public String docenteDashboard(Model model) {
        model.addAttribute("titulo", "Panel Docente");
        model.addAttribute("rol", "DOCENTE");
        model.addAttribute("estudiantesTotal", estudianteRepository.count());
        model.addAttribute("estudiantesActivos", estudianteRepository.countByActivoTrue());
        model.addAttribute("resultadosTotal", resultadoRepository.count());
        return "dashboard/dashboard";
    }

    @GetMapping("/dashboard/estudiante")
    public String estudianteDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + email));

        Estudiante estudiante = estudianteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("No existe estudiante asociado al usuario: " + email));

        List<Resultado> resultados = resultadoRepository.findByEstudianteId(estudiante.getId());

        double promedioGlobal = resultados.stream()
                .filter(r -> r.getPuntajeGlobal() != null)
                .mapToDouble(Resultado::getPuntajeGlobal)
                .average()
                .orElse(0.0);

        Double mejorPuntaje = resultados.stream()
                .map(Resultado::getPuntajeGlobal)
                .filter(p -> p != null)
                .max(Double::compareTo)
                .orElse(0.0);

        Resultado resultadoBase = resultados.stream()
                .filter(r -> r.getPuntajeGlobal() != null)
                .max(Comparator.comparing(Resultado::getPuntajeGlobal))
                .orElse(null);

        BeneficioAcademicoDTO beneficio = beneficioAcademicoService.evaluarBeneficio(estudiante, resultadoBase);
        model.addAttribute("titulo", "Panel Estudiante");
        model.addAttribute("rol", "ESTUDIANTE");
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("resultadosTotal", resultados.size());
        model.addAttribute("promedioGlobal", String.format("%.1f", promedioGlobal));
        model.addAttribute("mejorPuntaje", String.format("%.1f", mejorPuntaje));
        model.addAttribute("beneficio", beneficio);
        model.addAttribute("tieneBeneficio", beneficio != null && beneficio.isAplica());

        return "dashboard/dashboard";
    }
}