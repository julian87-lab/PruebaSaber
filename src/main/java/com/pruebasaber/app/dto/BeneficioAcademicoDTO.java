package com.pruebasaber.app.dto;

public class BeneficioAcademicoDTO {

    private boolean aplica;
    private String tipoPrueba;
    private String rango;
    private String descripcion;
    private String notaSeminario;
    private String porcentajeBeca;
    private String vigencia;
    private String observaciones;

    public BeneficioAcademicoDTO() {
        this.aplica = false;
        this.tipoPrueba = "No aplica";
        this.rango = "-";
        this.descripcion = "No registra beneficio académico según el acuerdo.";
        this.notaSeminario = "-";
        this.porcentajeBeca = "0%";
        this.vigencia = "No aplica";
        this.observaciones = "No cumple con el rango mínimo exigido por el acuerdo institucional.";
    }

    public boolean isAplica() {
        return aplica;
    }

    public void setAplica(boolean aplica) {
        this.aplica = aplica;
    }

    public String getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(String tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNotaSeminario() {
        return notaSeminario;
    }

    public void setNotaSeminario(String notaSeminario) {
        this.notaSeminario = notaSeminario;
    }

    public String getPorcentajeBeca() {
        return porcentajeBeca;
    }

    public void setPorcentajeBeca(String porcentajeBeca) {
        this.porcentajeBeca = porcentajeBeca;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}