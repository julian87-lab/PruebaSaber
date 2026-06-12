package com.pruebasaber.app.controller;

import com.pruebasaber.app.dto.BeneficioAcademicoDTO;
import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Resultado;
import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.service.BeneficioAcademicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/dashboard/coordinador")
public class CoordinadorController {

    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository resultadoRepository;
    private final BeneficioAcademicoService beneficioAcademicoService;

    public CoordinadorController(EstudianteRepository estudianteRepository,
                                 ResultadoRepository resultadoRepository,
                                 BeneficioAcademicoService beneficioAcademicoService) {
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository = resultadoRepository;
        this.beneficioAcademicoService = beneficioAcademicoService;
    }

    @GetMapping
    public String dashboard(Model model) {
        long estudiantesTotal = estudianteRepository.count();
        long estudiantesActivos = estudianteRepository.countByActivoTrue();
        long estudiantesInactivos = estudianteRepository.countByActivoFalse();
        long resultadosTotal = resultadoRepository.count();

        model.addAttribute("titulo", "Dashboard Coordinador");
        model.addAttribute("rol", "COORDINADOR");
        model.addAttribute("estudiantesTotal", estudiantesTotal);
        model.addAttribute("estudiantesActivos", estudiantesActivos);
        model.addAttribute("estudiantesInactivos", estudiantesInactivos);
        model.addAttribute("resultadosTotal", resultadosTotal);

        return "coordinador/dashboard";
    }

    @GetMapping("/informe-alumnos")
    public String informeAlumnos(@RequestParam(required = false) String q, Model model) {
        String textoBusqueda = q != null ? q.trim() : "";

        List<Estudiante> estudiantes = textoBusqueda.isBlank()
                ? estudianteRepository.findAll()
                : estudianteRepository.findByPrimerNombreContainingIgnoreCaseOrPrimerApellidoContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCase(
                textoBusqueda, textoBusqueda, textoBusqueda
        );

        List<AlumnoFila> alumnos = estudiantes.stream()
                .map(e -> new AlumnoFila(
                        e.getId(),
                        e.getNumeroDocumento(),
                        e.getPrimerNombre(),
                        e.getPrimerApellido(),
                        e.getSegundoApellido(),
                        e.getPrograma(),
                        e.getSemestre(),
                        e.getCodigoEstudiante(),
                        e.getCorreoElectronico(),
                        e.isActivo(),
                        resultadoRepository.findByEstudianteId(e.getId()).size()
                ))
                .toList();

        model.addAttribute("titulo", "Informe General de Alumnos");
        model.addAttribute("rol", "COORDINADOR");
        model.addAttribute("q", textoBusqueda);
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalActivos", estudianteRepository.countByActivoTrue());
        model.addAttribute("totalInactivos", estudianteRepository.countByActivoFalse());

        return "coordinador/informe-alumnos";
    }

    @GetMapping("/informe-resultados")
    public String informeResultados(@RequestParam(required = false) String q,
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

        List<ResultadoFila> filasResultados = resultados.stream()
                .map(r -> new ResultadoFila(
                        r,
                        beneficioAcademicoService.evaluarBeneficio(r.getEstudiante(), r)
                ))
                .toList();

        model.addAttribute("titulo", "Informe Detallado de Resultados");
        model.addAttribute("rol", "COORDINADOR");
        model.addAttribute("filasResultados", filasResultados);
        model.addAttribute("periodos", periodos);
        model.addAttribute("q", textoBusqueda);
        model.addAttribute("periodo", periodoFiltro);
        model.addAttribute("totalResultados", todos.size());

        return "coordinador/informe-resultados";
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model) {
        List<Resultado> resultados = resultadoRepository.findAllConEstudiante();
        List<BeneficioFila> beneficios = new ArrayList<>();

        for (Resultado resultado : resultados) {
            Estudiante estudiante = resultado.getEstudiante();
            BeneficioAcademicoDTO beneficio = beneficioAcademicoService.evaluarBeneficio(estudiante, resultado);

            if (beneficio.isAplica()) {
                beneficios.add(new BeneficioFila(estudiante, resultado, beneficio));
            }
        }

        model.addAttribute("titulo", "Informe de Beneficios");
        model.addAttribute("rol", "COORDINADOR");
        model.addAttribute("beneficios", beneficios);
        model.addAttribute("totalBeneficiados", beneficios.size());

        return "coordinador/informe-beneficios";
    }

    @GetMapping("/resolucion-beneficios")
    public String resolucionBeneficios(Model model) {
        model.addAttribute("titulo", "Resolución Beneficios Tecnología e Ingeniería");
        model.addAttribute("rol", "COORDINADOR");
        model.addAttribute("nombrePdf", "ACUERDO-No.-01-009-CONSEJO-DIRECTIVO-Reglamento-de-Estimulos.pdf");

        return "coordinador/resolucion-beneficios";
    }

    public static class AlumnoFila {
        private final Long id;
        private final String numeroDocumento;
        private final String primerNombre;
        private final String primerApellido;
        private final String segundoApellido;
        private final String programa;
        private final Integer semestre;
        private final String codigoEstudiante;
        private final String correoElectronico;
        private final boolean activo;
        private final int totalResultados;

        public AlumnoFila(Long id, String numeroDocumento, String primerNombre, String primerApellido,
                          String segundoApellido, String programa, Integer semestre,
                          String codigoEstudiante, String correoElectronico,
                          boolean activo, int totalResultados) {
            this.id = id;
            this.numeroDocumento = numeroDocumento;
            this.primerNombre = primerNombre;
            this.primerApellido = primerApellido;
            this.segundoApellido = segundoApellido;
            this.programa = programa;
            this.semestre = semestre;
            this.codigoEstudiante = codigoEstudiante;
            this.correoElectronico = correoElectronico;
            this.activo = activo;
            this.totalResultados = totalResultados;
        }

        public Long getId() { return id; }
        public String getNumeroDocumento() { return numeroDocumento; }
        public String getPrimerNombre() { return primerNombre; }
        public String getPrimerApellido() { return primerApellido; }
        public String getSegundoApellido() { return segundoApellido; }
        public String getPrograma() { return programa; }
        public Integer getSemestre() { return semestre; }
        public String getCodigoEstudiante() { return codigoEstudiante; }
        public String getCorreoElectronico() { return correoElectronico; }
        public boolean isActivo() { return activo; }
        public int getTotalResultados() { return totalResultados; }
    }

    public static class ResultadoFila {
        private final Resultado resultado;
        private final BeneficioAcademicoDTO beneficio;

        public ResultadoFila(Resultado resultado, BeneficioAcademicoDTO beneficio) {
            this.resultado = resultado;
            this.beneficio = beneficio;
        }

        public Resultado getResultado() {
            return resultado;
        }

        public BeneficioAcademicoDTO getBeneficio() {
            return beneficio;
        }
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