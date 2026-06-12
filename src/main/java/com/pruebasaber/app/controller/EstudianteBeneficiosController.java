package com.pruebasaber.app.controller;

import com.pruebasaber.app.dto.BeneficioAcademicoDTO;
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

import java.util.Comparator;
import java.util.List;

@Controller
public class EstudianteBeneficiosController {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository resultadoRepository;
    private final BeneficioAcademicoService beneficioAcademicoService;

    public EstudianteBeneficiosController(UsuarioRepository usuarioRepository,
                                          EstudianteRepository estudianteRepository,
                                          ResultadoRepository resultadoRepository,
                                          BeneficioAcademicoService beneficioAcademicoService) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository = resultadoRepository;
        this.beneficioAcademicoService = beneficioAcademicoService;
    }

    @GetMapping("/mis-beneficios")
    public String verMisBeneficios(Authentication authentication, Model model) {
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + email));

        Estudiante estudiante = estudianteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("No existe estudiante asociado al usuario: " + email));

        List<Resultado> resultados = resultadoRepository.findByEstudianteId(estudiante.getId());

        Resultado resultadoBase = resultados.stream()
                .filter(r -> r.getPuntajeGlobal() != null)
                .max(Comparator.comparing(Resultado::getPuntajeGlobal))
                .orElse(null);

        BeneficioAcademicoDTO beneficio = beneficioAcademicoService.evaluarBeneficio(estudiante, resultadoBase);

        model.addAttribute("titulo", "Mis beneficios");
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("resultado", resultadoBase);
        model.addAttribute("beneficio", beneficio);
        model.addAttribute("tieneResultados", !resultados.isEmpty());
        model.addAttribute("aplicaBeneficio", beneficio != null && beneficio.isAplica());

        return "estudiantes/mis-beneficios";
    }

    @GetMapping("/resolucion-beneficios-estudiante")
    public String verResolucionBeneficios(Model model) {
        model.addAttribute("titulo", "Resolución de beneficios");
        model.addAttribute("nombrePdf", "ACUERDO-No.-01-009-CONSEJO-DIRECTIVO-Reglamento-de-Estimulos.pdf");
        return "estudiantes/resolucion-beneficios";
    }
}