package com.pruebasaber.app.service;

import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Resultado;
import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;

@Service
public class ExcelImportService {

    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository resultadoRepository;

    public ExcelImportService(EstudianteRepository estudianteRepository,
                              ResultadoRepository resultadoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository = resultadoRepository;
    }

    @Transactional
    public int importarResultados(MultipartFile archivo, String periodo) throws Exception {
        int importados = 0;

        try (InputStream is = archivo.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();

                String tipoDocumento = limpiar(formatter.formatCellValue(row.getCell(0)));
                String numeroDocumento = limpiar(formatter.formatCellValue(row.getCell(1)));

                if (numeroDocumento.isBlank()) {
                    continue;
                }

                String puntajeRaw = limpiar(formatter.formatCellValue(row.getCell(9)));
                if (puntajeRaw.equalsIgnoreCase("ANULADO")) {
                    continue;
                }

                String primerApellido = limpiar(formatter.formatCellValue(row.getCell(2)));
                String segundoApellido = limpiar(formatter.formatCellValue(row.getCell(3)));
                String primerNombre = limpiar(formatter.formatCellValue(row.getCell(4)));
                String segundoNombre = limpiar(formatter.formatCellValue(row.getCell(5)));
                String correoElectronico = limpiar(formatter.formatCellValue(row.getCell(6)));
                String numeroTelefono = limpiar(formatter.formatCellValue(row.getCell(7)));
                String numeroRegistro = limpiar(formatter.formatCellValue(row.getCell(8)));

                Estudiante estudiante = obtenerOCrearEstudiante(
                        tipoDocumento,
                        numeroDocumento,
                        primerApellido,
                        segundoApellido,
                        primerNombre,
                        segundoNombre,
                        correoElectronico,
                        numeroTelefono
                );

                if (!numeroRegistro.isBlank()
                        && resultadoRepository.existsByEstudianteIdAndPeriodoAndNumeroRegistro(
                        estudiante.getId(), periodo, numeroRegistro)) {
                    continue;
                }

                Resultado resultado = new Resultado();
                resultado.setEstudiante(estudiante);
                resultado.setPeriodo(limpiar(periodo));
                resultado.setNumeroRegistro(numeroRegistro);

                Double puntajeGlobal = parseDouble(puntajeRaw);
                Double comunicacionEscrita = parseDouble(formatter.formatCellValue(row.getCell(11)));
                Double razonamientoCuantitativo = parseDouble(formatter.formatCellValue(row.getCell(13)));
                Double lecturaCritica = parseDouble(formatter.formatCellValue(row.getCell(15)));
                Double competenciasCiudadanas = parseDouble(formatter.formatCellValue(row.getCell(17)));
                Double ingles = parseDouble(formatter.formatCellValue(row.getCell(19)));
                Double formulacion = parseDouble(formatter.formatCellValue(row.getCell(21)));
                Double pensamiento = parseDouble(formatter.formatCellValue(row.getCell(23)));
                Double disenoSoftware = parseDouble(formatter.formatCellValue(row.getCell(25)));

                resultado.setPuntajeGlobal(puntajeGlobal);
                resultado.setNivelPuntajeGlobal(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(10)), puntajeGlobal)
                );

                resultado.setComunicacionEscrita(comunicacionEscrita);
                resultado.setNivelComunicacionEscrita(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(12)), comunicacionEscrita)
                );

                resultado.setRazonamientoCuantitativo(razonamientoCuantitativo);
                resultado.setNivelRazonamientoCuantitativo(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(14)), razonamientoCuantitativo)
                );

                resultado.setLecturaCritica(lecturaCritica);
                resultado.setNivelLecturaCritica(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(16)), lecturaCritica)
                );

                resultado.setCompetenciasCiudadanas(competenciasCiudadanas);
                resultado.setNivelCompetenciasCiudadanas(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(18)), competenciasCiudadanas)
                );

                resultado.setIngles(ingles);
                resultado.setNivelIngles(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(20)), ingles)
                );

                resultado.setFormulacionProyectosIngenieria(formulacion);
                resultado.setNivelFormulacionProyectosIngenieria(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(22)), formulacion)
                );

                resultado.setPensamientoCientificoMatematicasEstadistica(pensamiento);
                resultado.setNivelPensamientoCientificoMatematicasEstadistica(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(24)), pensamiento)
                );

                resultado.setDisenoSoftware(disenoSoftware);
                resultado.setNivelDisenoSoftware(
                        normalizarNivelGeneral(formatter.formatCellValue(row.getCell(26)), disenoSoftware)
                );

                resultado.setNivelInglesCC(
                        normalizarNivelIdioma(formatter.formatCellValue(row.getCell(27)))
                );

                resultadoRepository.save(resultado);
                importados++;
            }
        }

        return importados;
    }

    private Estudiante obtenerOCrearEstudiante(String tipoDocumento,
                                               String numeroDocumento,
                                               String primerApellido,
                                               String segundoApellido,
                                               String primerNombre,
                                               String segundoNombre,
                                               String correoElectronico,
                                               String numeroTelefono) {

        Optional<Estudiante> existente = estudianteRepository.findByNumeroDocumento(numeroDocumento);

        if (existente.isPresent()) {
            Estudiante e = existente.get();

            if (!tipoDocumento.isBlank()) e.setTipoDocumento(tipoDocumento);
            if (!primerApellido.isBlank()) e.setPrimerApellido(primerApellido);
            if (!segundoApellido.isBlank()) e.setSegundoApellido(segundoApellido);
            if (!primerNombre.isBlank()) e.setPrimerNombre(primerNombre);
            if (!segundoNombre.isBlank()) e.setSegundoNombre(segundoNombre);
            if (!correoElectronico.isBlank()) e.setCorreoElectronico(correoElectronico);
            if (!numeroTelefono.isBlank()) e.setNumeroTelefono(numeroTelefono);

            if (e.getCodigoEstudiante() == null || e.getCodigoEstudiante().isBlank()) {
                e.setCodigoEstudiante("COD-" + numeroDocumento);
            }

            if (e.getSemestre() == null) {
                e.setSemestre(1);
            }

            if (!e.isActivo()) {
                e.setActivo(true);
            }

            return estudianteRepository.save(e);
        }

        Estudiante nuevo = new Estudiante();
        nuevo.setTipoDocumento(tipoDocumento.isBlank() ? "CC" : tipoDocumento);
        nuevo.setNumeroDocumento(numeroDocumento);
        nuevo.setPrimerApellido(primerApellido);
        nuevo.setSegundoApellido(segundoApellido);
        nuevo.setPrimerNombre(primerNombre);
        nuevo.setSegundoNombre(segundoNombre);
        nuevo.setCorreoElectronico(correoElectronico);
        nuevo.setNumeroTelefono(numeroTelefono);
        nuevo.setCodigoEstudiante("COD-" + numeroDocumento);
        nuevo.setSemestre(1);
        nuevo.setActivo(true);

        return estudianteRepository.save(nuevo);
    }

    private String normalizarNivelGeneral(String nivelExcel, Double puntaje) {
        String valor = limpiar(nivelExcel);

        if (esNivelValido(valor)) {
            return valor;
        }

        return calcularNivelGeneral(puntaje);
    }

    private String normalizarNivelIdioma(String valorExcel) {
        String valor = limpiar(valorExcel).toUpperCase(Locale.ROOT);

        if (valor.isBlank()) {
            return "-";
        }

        if (contieneFormula(valor)) {
            return "-";
        }

        valor = valor.replace(" CC", "").trim();

        if (valor.matches("A0|A1|A2|B1|B2|C1|C2")) {
            return valor;
        }

        return "-";
    }

    private boolean esNivelValido(String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }

        String normalizado = valor.trim().toUpperCase(Locale.ROOT);

        if (contieneFormula(normalizado)) {
            return false;
        }

        return normalizado.startsWith("NIVEL ")
                || normalizado.matches("A0|A1|A2|B1|B2|C1|C2")
                || normalizado.matches("A0 CC|A1 CC|A2 CC|B1 CC|B2 CC|C1 CC|C2 CC");
    }

    private boolean contieneFormula(String valor) {
        if (valor == null) {
            return false;
        }

        String v = valor.toUpperCase(Locale.ROOT);
        return v.startsWith("=")
                || v.contains("IF(")
                || v.contains("SI(")
                || v.contains("AND(")
                || v.contains("Y(");
    }

    private String calcularNivelGeneral(Double puntaje) {
        if (puntaje == null) {
            return "-";
        }

        if (puntaje >= 191 && puntaje <= 300) return "Nivel 4";
        if (puntaje >= 156 && puntaje <= 190) return "Nivel 3";
        if (puntaje >= 126 && puntaje <= 155) return "Nivel 2";
        if (puntaje >= 0 && puntaje <= 125) return "Nivel 1";

        return "-";
    }

    private Double parseDouble(String valor) {
        try {
            String limpio = limpiar(valor);
            if (limpio.isBlank() || limpio.equalsIgnoreCase("ANULADO")) {
                return null;
            }
            return Double.valueOf(limpio.replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}