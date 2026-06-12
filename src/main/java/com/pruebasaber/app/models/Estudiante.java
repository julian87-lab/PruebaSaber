package com.pruebasaber.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30)
    @Column(length = 30, nullable = false, unique = true)
    private String numeroDocumento;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String primerApellido;

    @Size(max = 100)
    @Column(length = 100)
    private String segundoApellido;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String primerNombre;

    @Size(max = 100)
    @Column(length = 100)
    private String segundoNombre;

    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 150)
    @Column(length = 150)
    private String correoElectronico;

    @Size(max = 30)
    @Column(length = 30)
    private String numeroTelefono;

    @Size(max = 50)
    @Column(length = 50, unique = true)
    private String codigoEstudiante;

    @Size(max = 200)
    @Column(length = 200)
    private String programa;

    @Size(max = 50)
    @Column(length = 50)
    private String tipoPrograma;

    @Min(value = 1, message = "El semestre debe ser mayor o igual a 1")
    private Integer semestre;

    @Column(columnDefinition = "boolean default true")
    private boolean activo = true;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resultado> resultados = new ArrayList<>();

    public Estudiante() {}

    public Long getId() { return id; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getNumeroTelefono() { return numeroTelefono; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }

    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public String getTipoPrograma() { return tipoPrograma; }
    public void setTipoPrograma(String tipoPrograma) { this.tipoPrograma = tipoPrograma; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Resultado> getResultados() { return resultados; }
    public void setResultados(List<Resultado> resultados) { this.resultados = resultados; }
}