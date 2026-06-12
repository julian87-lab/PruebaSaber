package com.pruebasaber.app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "resultados")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(length = 20)
    private String periodo;

    @Column(length = 100)
    private String numeroRegistro;

    private Double puntajeGlobal;
    private String nivelPuntajeGlobal;

    private Double comunicacionEscrita;
    private String nivelComunicacionEscrita;

    private Double razonamientoCuantitativo;
    private String nivelRazonamientoCuantitativo;

    private Double lecturaCritica;
    private String nivelLecturaCritica;

    private Double competenciasCiudadanas;
    private String nivelCompetenciasCiudadanas;

    private Double ingles;
    private String nivelIngles;

    private Double formulacionProyectosIngenieria;
    private String nivelFormulacionProyectosIngenieria;

    private Double pensamientoCientificoMatematicasEstadistica;
    private String nivelPensamientoCientificoMatematicasEstadistica;

    private Double disenoSoftware;
    private String nivelDisenoSoftware;

    private String nivelInglesCC;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public Double getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(Double puntajeGlobal) { this.puntajeGlobal = puntajeGlobal; }

    public String getNivelPuntajeGlobal() { return nivelPuntajeGlobal; }
    public void setNivelPuntajeGlobal(String nivelPuntajeGlobal) { this.nivelPuntajeGlobal = nivelPuntajeGlobal; }

    public Double getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(Double comunicacionEscrita) { this.comunicacionEscrita = comunicacionEscrita; }

    public String getNivelComunicacionEscrita() { return nivelComunicacionEscrita; }
    public void setNivelComunicacionEscrita(String nivelComunicacionEscrita) { this.nivelComunicacionEscrita = nivelComunicacionEscrita; }

    public Double getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Double razonamientoCuantitativo) { this.razonamientoCuantitativo = razonamientoCuantitativo; }

    public String getNivelRazonamientoCuantitativo() { return nivelRazonamientoCuantitativo; }
    public void setNivelRazonamientoCuantitativo(String nivelRazonamientoCuantitativo) { this.nivelRazonamientoCuantitativo = nivelRazonamientoCuantitativo; }

    public Double getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(Double lecturaCritica) { this.lecturaCritica = lecturaCritica; }

    public String getNivelLecturaCritica() { return nivelLecturaCritica; }
    public void setNivelLecturaCritica(String nivelLecturaCritica) { this.nivelLecturaCritica = nivelLecturaCritica; }

    public Double getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Double competenciasCiudadanas) { this.competenciasCiudadanas = competenciasCiudadanas; }

    public String getNivelCompetenciasCiudadanas() { return nivelCompetenciasCiudadanas; }
    public void setNivelCompetenciasCiudadanas(String nivelCompetenciasCiudadanas) { this.nivelCompetenciasCiudadanas = nivelCompetenciasCiudadanas; }

    public Double getIngles() { return ingles; }
    public void setIngles(Double ingles) { this.ingles = ingles; }

    public String getNivelIngles() { return nivelIngles; }
    public void setNivelIngles(String nivelIngles) { this.nivelIngles = nivelIngles; }

    public Double getFormulacionProyectosIngenieria() { return formulacionProyectosIngenieria; }
    public void setFormulacionProyectosIngenieria(Double formulacionProyectosIngenieria) { this.formulacionProyectosIngenieria = formulacionProyectosIngenieria; }

    public String getNivelFormulacionProyectosIngenieria() { return nivelFormulacionProyectosIngenieria; }
    public void setNivelFormulacionProyectosIngenieria(String nivelFormulacionProyectosIngenieria) { this.nivelFormulacionProyectosIngenieria = nivelFormulacionProyectosIngenieria; }

    public Double getPensamientoCientificoMatematicasEstadistica() { return pensamientoCientificoMatematicasEstadistica; }
    public void setPensamientoCientificoMatematicasEstadistica(Double pensamientoCientificoMatematicasEstadistica) {
        this.pensamientoCientificoMatematicasEstadistica = pensamientoCientificoMatematicasEstadistica;
    }

    public String getNivelPensamientoCientificoMatematicasEstadistica() { return nivelPensamientoCientificoMatematicasEstadistica; }
    public void setNivelPensamientoCientificoMatematicasEstadistica(String nivelPensamientoCientificoMatematicasEstadistica) {
        this.nivelPensamientoCientificoMatematicasEstadistica = nivelPensamientoCientificoMatematicasEstadistica;
    }

    public Double getDisenoSoftware() { return disenoSoftware; }
    public void setDisenoSoftware(Double disenoSoftware) { this.disenoSoftware = disenoSoftware; }

    public String getNivelDisenoSoftware() { return nivelDisenoSoftware; }
    public void setNivelDisenoSoftware(String nivelDisenoSoftware) { this.nivelDisenoSoftware = nivelDisenoSoftware; }

    public String getNivelInglesCC() { return nivelInglesCC; }
    public void setNivelInglesCC(String nivelInglesCC) { this.nivelInglesCC = nivelInglesCC; }
}