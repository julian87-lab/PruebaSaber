package com.pruebasaber.app.repository;

import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    @Query("SELECT e FROM Estudiante e WHERE e.usuario = :usuario")
    Optional<Estudiante> findByUsuario(Usuario usuario);

    Optional<Estudiante> findByNumeroDocumento(String numeroDocumento);

    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);
    
    Optional<Estudiante> findByUsuarioId(Long usuarioId);

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByCodigoEstudiante(String codigoEstudiante);

    List<Estudiante> findByActivoTrue();

    List<Estudiante> findByActivoFalse();

    long countByActivoTrue();

    long countByActivoFalse();

    List<Estudiante> findByPrimerNombreContainingIgnoreCaseOrPrimerApellidoContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCase(
            String primerNombre, String primerApellido, String numeroDocumento
    );
}