package com.pruebasaber.app.repository;

import com.pruebasaber.app.models.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    @Query("""
        SELECT r
        FROM Resultado r
        JOIN FETCH r.estudiante e
        WHERE e.id = :estudianteId
        ORDER BY r.periodo DESC, r.puntajeGlobal DESC
    """)
    List<Resultado> findByEstudianteId(@Param("estudianteId") Long estudianteId);

    @Query("""
        SELECT r
        FROM Resultado r
        JOIN FETCH r.estudiante e
        ORDER BY r.puntajeGlobal DESC
    """)
    List<Resultado> findAllConEstudiante();

    @Query("""
        SELECT r
        FROM Resultado r
        JOIN FETCH r.estudiante e
        WHERE r.periodo = :periodo
        ORDER BY r.puntajeGlobal DESC
    """)
    List<Resultado> findByPeriodoConEstudiante(@Param("periodo") String periodo);

    @Query("""
        SELECT r
        FROM Resultado r
        JOIN FETCH r.estudiante e
        WHERE LOWER(e.primerNombre) LIKE LOWER(CONCAT('%', :texto, '%'))
           OR LOWER(e.primerApellido) LIKE LOWER(CONCAT('%', :texto, '%'))
           OR LOWER(e.numeroDocumento) LIKE LOWER(CONCAT('%', :texto, '%'))
        ORDER BY r.puntajeGlobal DESC
    """)
    List<Resultado> buscarConEstudiante(@Param("texto") String texto);

    @Query("""
        SELECT r
        FROM Resultado r
        JOIN FETCH r.estudiante e
        WHERE r.periodo = :periodo
          AND (
                LOWER(e.primerNombre) LIKE LOWER(CONCAT('%', :texto, '%'))
             OR LOWER(e.primerApellido) LIKE LOWER(CONCAT('%', :texto, '%'))
             OR LOWER(e.numeroDocumento) LIKE LOWER(CONCAT('%', :texto, '%'))
          )
        ORDER BY r.puntajeGlobal DESC
    """)
    List<Resultado> buscarPorPeriodoConEstudiante(@Param("periodo") String periodo,
                                                  @Param("texto") String texto);

    boolean existsByEstudianteIdAndPeriodoAndNumeroRegistro(Long estudianteId, String periodo, String numeroRegistro);
}