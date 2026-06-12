package com.pruebasaber.app.controller;

import com.pruebasaber.app.dto.BeneficioAcademicoDTO;
import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Resultado;
import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.service.BeneficioAcademicoService;
import com.pruebasaber.app.service.ExcelImportService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository resultadoRepository;
    private final ExcelImportService excelImportService;
    private final BeneficioAcademicoService beneficioAcademicoService;

    public EstudianteController(EstudianteRepository estudianteRepository,
                                ResultadoRepository resultadoRepository,
                                ExcelImportService excelImportService,
                                BeneficioAcademicoService beneficioAcademicoService) {
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository = resultadoRepository;
        this.excelImportService = excelImportService;
        this.beneficioAcademicoService = beneficioAcademicoService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q,
                         @RequestParam(required = false) String estado,
                         Model model) {

        List<Estudiante> estudiantes;

        boolean hayBusqueda = q != null && !q.trim().isEmpty();
        boolean hayEstado = estado != null && !estado.isBlank();

        if (hayBusqueda && hayEstado) {
            List<Estudiante> base = "activos".equalsIgnoreCase(estado)
                    ? estudianteRepository.findByActivoTrue()
                    : "inactivos".equalsIgnoreCase(estado)
                    ? estudianteRepository.findByActivoFalse()
                    : estudianteRepository.findAll();

            estudiantes = base.stream()
                    .filter(e ->
                            contieneTexto(e.getPrimerNombre(), q) ||
                            contieneTexto(e.getPrimerApellido(), q) ||
                            contieneTexto(e.getNumeroDocumento(), q)
                    )
                    .toList();

        } else if (hayBusqueda) {
            estudiantes = estudianteRepository
                    .findByPrimerNombreContainingIgnoreCaseOrPrimerApellidoContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCase(q, q, q);

        } else if ("activos".equalsIgnoreCase(estado)) {
            estudiantes = estudianteRepository.findByActivoTrue();

        } else if ("inactivos".equalsIgnoreCase(estado)) {
            estudiantes = estudianteRepository.findByActivoFalse();

        } else {
            estudiantes = estudianteRepository.findAll();
        }

        cargarModeloListado(model, estudiantes, new Estudiante(), false, q, estado);
        return "estudiantes/estudiantes";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("nuevoEstudiante") Estudiante estudiante,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {

        if (estudiante.getNumeroDocumento() != null &&
                estudianteRepository.existsByNumeroDocumento(estudiante.getNumeroDocumento())) {
            result.rejectValue("numeroDocumento", "error.numeroDocumento", "Ya existe un estudiante con ese número de documento.");
        }

        if (estudiante.getCodigoEstudiante() != null &&
                !estudiante.getCodigoEstudiante().isBlank() &&
                estudianteRepository.existsByCodigoEstudiante(estudiante.getCodigoEstudiante())) {
            result.rejectValue("codigoEstudiante", "error.codigoEstudiante", "Ya existe un estudiante con ese código.");
        }

        if (result.hasErrors()) {
            cargarModeloListado(model, estudianteRepository.findAll(), estudiante, false, null, null);
            return "estudiantes/estudiantes";
        }

        if (estudiante.getCodigoEstudiante() == null || estudiante.getCodigoEstudiante().isBlank()) {
            estudiante.setCodigoEstudiante("COD-" + estudiante.getNumeroDocumento());
        }

        if (estudiante.getSemestre() == null) {
            estudiante.setSemestre(1);
        }

        estudiante.setActivo(true);

        try {
            estudianteRepository.save(estudiante);
            flash.addFlashAttribute("mensajeExito", "Estudiante creado correctamente.");
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("mensajeError", "No fue posible guardar el estudiante por datos duplicados.");
        }

        return "redirect:/estudiantes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);

        if (estudiante == null) {
            flash.addFlashAttribute("mensajeError", "El estudiante no existe.");
            return "redirect:/estudiantes";
        }

        cargarModeloListado(model, estudianteRepository.findAll(), estudiante, true, null, null);
        return "estudiantes/estudiantes";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("nuevoEstudiante") Estudiante formulario,
                             BindingResult result,
                             Model model,
                             RedirectAttributes flash) {

        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);

        if (estudiante == null) {
            flash.addFlashAttribute("mensajeError", "No se pudo actualizar el estudiante.");
            return "redirect:/estudiantes";
        }

        var existenteDocumento = estudianteRepository.findByNumeroDocumento(formulario.getNumeroDocumento());
        if (existenteDocumento.isPresent() && !existenteDocumento.get().getId().equals(id)) {
            result.rejectValue("numeroDocumento", "error.numeroDocumento", "Ya existe otro estudiante con ese número de documento.");
        }

        if (formulario.getCodigoEstudiante() != null && !formulario.getCodigoEstudiante().isBlank()) {
            var existenteCodigo = estudianteRepository.findByCodigoEstudiante(formulario.getCodigoEstudiante());
            if (existenteCodigo.isPresent() && !existenteCodigo.get().getId().equals(id)) {
                result.rejectValue("codigoEstudiante", "error.codigoEstudiante", "Ya existe otro estudiante con ese código.");
            }
        }

        if (result.hasErrors()) {
            cargarModeloListado(model, estudianteRepository.findAll(), formulario, true, null, null);
            return "estudiantes/estudiantes";
        }

        estudiante.setTipoDocumento(formulario.getTipoDocumento());
        estudiante.setNumeroDocumento(formulario.getNumeroDocumento());
        estudiante.setPrimerNombre(formulario.getPrimerNombre());
        estudiante.setSegundoNombre(formulario.getSegundoNombre());
        estudiante.setPrimerApellido(formulario.getPrimerApellido());
        estudiante.setSegundoApellido(formulario.getSegundoApellido());
        estudiante.setCorreoElectronico(formulario.getCorreoElectronico());
        estudiante.setNumeroTelefono(formulario.getNumeroTelefono());

        if (formulario.getCodigoEstudiante() == null || formulario.getCodigoEstudiante().isBlank()) {
            estudiante.setCodigoEstudiante("COD-" + formulario.getNumeroDocumento());
        } else {
            estudiante.setCodigoEstudiante(formulario.getCodigoEstudiante());
        }

        estudiante.setPrograma(formulario.getPrograma());
        estudiante.setTipoPrograma(formulario.getTipoPrograma());
        estudiante.setSemestre(formulario.getSemestre());

        try {
            estudianteRepository.save(estudiante);
            flash.addFlashAttribute("mensajeExito", "Estudiante actualizado correctamente.");
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("mensajeError", "No fue posible actualizar el estudiante por datos duplicados.");
        }

        return "redirect:/estudiantes";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);

        if (estudiante == null) {
            flash.addFlashAttribute("mensajeError", "El estudiante no existe.");
            return "redirect:/estudiantes";
        }

        List<Resultado> resultados = resultadoRepository.findByEstudianteId(id);

        Resultado resultadoBase = resultados.stream()
                .filter(r -> r.getPuntajeGlobal() != null)
                .max(Comparator.comparing(Resultado::getPuntajeGlobal))
                .orElse(null);

        BeneficioAcademicoDTO beneficio = beneficioAcademicoService.evaluarBeneficio(estudiante, resultadoBase);

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("resultados", resultados);
        model.addAttribute("beneficio", beneficio);

        return "estudiantes/detalle-estudiante";
    }

    @PostMapping("/importar")
    public String importarExcel(@RequestParam("file") MultipartFile file,
                                @RequestParam("periodo") String periodo,
                                RedirectAttributes flash) {
        if (file.isEmpty()) {
            flash.addFlashAttribute("mensajeError", "Selecciona un archivo Excel (.xlsx) antes de importar.");
            return "redirect:/estudiantes";
        }

        if (periodo == null || periodo.trim().isBlank()) {
            flash.addFlashAttribute("mensajeError", "Debes indicar el periodo antes de importar.");
            return "redirect:/estudiantes";
        }

        try {
            int total = excelImportService.importarResultados(file, periodo.trim());
            flash.addFlashAttribute("mensajeExito", total + " registros importados correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al importar: " + e.getMessage());
        }

        return "redirect:/estudiantes";
    }
    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes flash) {
        estudianteRepository.findById(id).ifPresent(estudiante -> {
            estudiante.setActivo(false);
            estudianteRepository.save(estudiante);
        });

        flash.addFlashAttribute("mensajeExito", "Estudiante desactivado correctamente.");
        return "redirect:/estudiantes";
    }

    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes flash) {
        estudianteRepository.findById(id).ifPresent(estudiante -> {
            estudiante.setActivo(true);
            estudianteRepository.save(estudiante);
        });

        flash.addFlashAttribute("mensajeExito", "Estudiante activado correctamente.");
        return "redirect:/estudiantes";
    }

    private boolean contieneTexto(String valor, String filtro) {
        return valor != null && filtro != null &&
                valor.toLowerCase().contains(filtro.toLowerCase());
    }

    private void cargarModeloListado(Model model,
                                     List<Estudiante> estudiantes,
                                     Estudiante nuevoEstudiante,
                                     boolean modoEdicion,
                                     String q,
                                     String estado) {
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("nuevoEstudiante", nuevoEstudiante);
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("q", q);
        model.addAttribute("estado", estado);
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalActivos", estudianteRepository.findByActivoTrue().size());
        model.addAttribute("totalInactivos", estudianteRepository.findByActivoFalse().size());
    }
}