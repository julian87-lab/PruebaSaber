package com.pruebasaber.app.repository;

import com.pruebasaber.app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByActivoTrue();

    List<Usuario> findByActivoFalse();

    long countByActivoTrue();

    long countByActivoFalse();

    List<Usuario> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nombre, String apellido, String email
    );

    List<Usuario> findByActivoTrueAndNombreContainingIgnoreCaseOrActivoTrueAndApellidoContainingIgnoreCaseOrActivoTrueAndEmailContainingIgnoreCase(
            String nombre, String apellido, String email
    );

    List<Usuario> findByActivoFalseAndNombreContainingIgnoreCaseOrActivoFalseAndApellidoContainingIgnoreCaseOrActivoFalseAndEmailContainingIgnoreCase(
            String nombre, String apellido, String email
    );
}