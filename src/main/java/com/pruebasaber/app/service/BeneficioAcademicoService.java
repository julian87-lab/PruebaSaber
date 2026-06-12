package com.pruebasaber.app.service;

import com.pruebasaber.app.dto.BeneficioAcademicoDTO;
import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Resultado;
import org.springframework.stereotype.Service;

@Service
public class BeneficioAcademicoService {

    public BeneficioAcademicoDTO evaluarBeneficio(Estudiante estudiante, Resultado resultado) {
        BeneficioAcademicoDTO dto = new BeneficioAcademicoDTO();

        if (estudiante == null || resultado == null || resultado.getPuntajeGlobal() == null) {
            dto.setObservaciones("No es posible calcular el beneficio porque no hay resultado con puntaje global.");
            return dto;
        }

        double puntaje = resultado.getPuntajeGlobal();
        String tipoPrueba = determinarTipoPrueba(estudiante);

        dto.setTipoPrueba(tipoPrueba);

        if ("Saber T&T".equals(tipoPrueba)) {
            aplicarReglaTyT(dto, puntaje, estudiante);
        } else {
            aplicarReglaSaberPro(dto, puntaje, estudiante);
        }

        if (dto.isAplica()) {
            dto.setVigencia("Válido por un (1) año desde la divulgación oficial de resultados por parte del ICFES.");
            dto.setDescripcion(dto.getDescripcion() + " Incluye reconocimiento según el rango del puntaje global.");
        } else {
            dto.setRango("-");
            dto.setNotaSeminario("-");
            dto.setPorcentajeBeca("0%");
            dto.setVigencia("No aplica");
            dto.setDescripcion("No registra beneficio académico según el acuerdo.");
            dto.setObservaciones("No cumple con el rango mínimo exigido por el acuerdo institucional para " + tipoPrueba + ".");
        }

        return dto;
    }

    private String determinarTipoPrueba(Estudiante estudiante) {
        String programa = normalizar(estudiante.getPrograma());
        String tipoPrograma = normalizar(estudiante.getTipoPrograma());
        Integer semestre = estudiante.getSemestre();

        if (esTyT(programa, tipoPrograma, semestre)) {
            return "Saber T&T";
        }

        return "Saber Pro";
    }

    private boolean esTyT(String programa, String tipoPrograma, Integer semestre) {
        boolean programaTecnologico =
                contiene(programa, "tecnologia") ||
                contiene(programa, "tecnológico") ||
                contiene(programa, "tecnologico") ||
                contiene(programa, "técnica") ||
                contiene(programa, "tecnica") ||
                contiene(programa, "técnico") ||
                contiene(programa, "tecnico");

        boolean tipoTecnologico =
                contiene(tipoPrograma, "tecnologia") ||
                contiene(tipoPrograma, "tecnológico") ||
                contiene(tipoPrograma, "tecnologico") ||
                contiene(tipoPrograma, "técnica") ||
                contiene(tipoPrograma, "tecnica") ||
                contiene(tipoPrograma, "técnico") ||
                contiene(tipoPrograma, "tecnico");

        boolean programaIngenieria =
                contiene(programa, "ingenieria") ||
                contiene(programa, "ingeniería") ||
                contiene(tipoPrograma, "profesional") ||
                contiene(tipoPrograma, "universitario");

        if (programaTecnologico || tipoTecnologico) {
            return true;
        }

        if (programaIngenieria) {
            return false;
        }

        return semestre != null && semestre <= 6;
    }

    private void aplicarReglaTyT(BeneficioAcademicoDTO dto, double puntaje, Estudiante estudiante) {
        if (puntaje >= 120 && puntaje <= 150) {
            dto.setAplica(true);
            dto.setRango("120 - 150");
            dto.setDescripcion("Se exime la entrega del informe final del trabajo de grado o se exonera realizar Seminario de Grado II.");
            dto.setNotaSeminario("4.5");
            dto.setPorcentajeBeca("0%");
        } else if (puntaje >= 151 && puntaje <= 170) {
            dto.setAplica(true);
            dto.setRango("151 - 170");
            dto.setDescripcion("Se exime la entrega del informe final del trabajo de grado o se exonera realizar Seminario de Grado II.");
            dto.setNotaSeminario("4.7");
            dto.setPorcentajeBeca("50%");
        } else if (puntaje >= 171) {
            dto.setAplica(true);
            dto.setRango("171 o más");
            dto.setDescripcion("Se exime la entrega del informe final del trabajo de grado o se exonera realizar Seminario de Grado II.");
            dto.setNotaSeminario("5.0");
            dto.setPorcentajeBeca("100%");
        }

        if (dto.isAplica()) {
            Integer semestre = estudiante.getSemestre();
            if (semestre != null && semestre > 6) {
                dto.setObservaciones("Aplica por regla de Saber T&T, pero conviene revisar el semestre registrado.");
            } else {
                dto.setObservaciones("Beneficio calculado para programa técnico o tecnológico.");
            }
        }
    }

    private void aplicarReglaSaberPro(BeneficioAcademicoDTO dto, double puntaje, Estudiante estudiante) {
        if (puntaje >= 180 && puntaje <= 210) {
            dto.setAplica(true);
            dto.setRango("180 - 210");
            dto.setDescripcion("Se exime la entrega del informe final del trabajo de grado o se exonera realizar Seminario de Grado IV.");
            dto.setNotaSeminario("4.5");
            dto.setPorcentajeBeca("0%");
        } else if (puntaje >= 211 && puntaje <= 240) {
            dto.setAplica(true);
            dto.setRango("211 - 240");
            dto.setDescripcion("Se exime la entrega del informe final del trabajo de grado o se exonera realizar Seminario de Grado IV.");
            dto.setNotaSeminario("4.7");
            dto.setPorcentajeBeca("50%");
        } else if (puntaje >= 241) {
            dto.setAplica(true);
            dto.setRango("241 o más");
            dto.setDescripcion("Se exime la entrega del informe final del trabajo de grado o se exonera realizar Seminario de Grado IV.");
            dto.setNotaSeminario("5.0");
            dto.setPorcentajeBeca("100%");
        }

        if (dto.isAplica()) {
            Integer semestre = estudiante.getSemestre();
            if (semestre != null && semestre < 7) {
                dto.setObservaciones("Aplica por regla de Saber Pro, pero conviene revisar el semestre registrado.");
            } else {
                dto.setObservaciones("Beneficio calculado para programa profesional.");
            }
        }
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase();
    }

    private boolean contiene(String base, String texto) {
        return base != null && base.contains(texto);
    }
}