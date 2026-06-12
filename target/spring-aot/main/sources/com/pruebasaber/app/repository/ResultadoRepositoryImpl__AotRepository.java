package com.pruebasaber.app.repository;

import com.pruebasaber.app.models.Resultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.Param;

/**
 * AOT generated JPA repository implementation for {@link ResultadoRepository}.
 */
@Generated
public class ResultadoRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ResultadoRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ResultadoRepository#buscarConEstudiante(java.lang.String)}.
   */
  public List<Resultado> buscarConEstudiante(@Param("texto") String texto) {
    String queryString = "    SELECT r\n"
            + "    FROM Resultado r\n"
            + "    JOIN FETCH r.estudiante e\n"
            + "    WHERE LOWER(e.primerNombre) LIKE LOWER(CONCAT('%', :texto, '%'))\n"
            + "       OR LOWER(e.primerApellido) LIKE LOWER(CONCAT('%', :texto, '%'))\n"
            + "       OR LOWER(e.numeroDocumento) LIKE LOWER(CONCAT('%', :texto, '%'))\n"
            + "    ORDER BY r.puntajeGlobal DESC\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("texto", texto);

    return (List<Resultado>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ResultadoRepository#buscarPorPeriodoConEstudiante(java.lang.String,java.lang.String)}.
   */
  public List<Resultado> buscarPorPeriodoConEstudiante(@Param("periodo") String periodo,
      @Param("texto") String texto) {
    String queryString = "    SELECT r\n"
            + "    FROM Resultado r\n"
            + "    JOIN FETCH r.estudiante e\n"
            + "    WHERE r.periodo = :periodo\n"
            + "      AND (\n"
            + "            LOWER(e.primerNombre) LIKE LOWER(CONCAT('%', :texto, '%'))\n"
            + "         OR LOWER(e.primerApellido) LIKE LOWER(CONCAT('%', :texto, '%'))\n"
            + "         OR LOWER(e.numeroDocumento) LIKE LOWER(CONCAT('%', :texto, '%'))\n"
            + "      )\n"
            + "    ORDER BY r.puntajeGlobal DESC\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("periodo", periodo);
    query.setParameter("texto", texto);

    return (List<Resultado>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ResultadoRepository#existsByEstudianteIdAndPeriodoAndNumeroRegistro(java.lang.Long,java.lang.String,java.lang.String)}.
   */
  public boolean existsByEstudianteIdAndPeriodoAndNumeroRegistro(Long estudianteId, String periodo,
      String numeroRegistro) {
    String queryString = "SELECT r.id FROM Resultado r WHERE r.estudiante.id = :estudianteId AND r.periodo = :periodo AND r.numeroRegistro = :numeroRegistro";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("estudianteId", estudianteId);
    query.setParameter("periodo", periodo);
    query.setParameter("numeroRegistro", numeroRegistro);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link ResultadoRepository#findAllConEstudiante()}.
   */
  public List<Resultado> findAllConEstudiante() {
    String queryString = "    SELECT r\n"
            + "    FROM Resultado r\n"
            + "    JOIN FETCH r.estudiante e\n"
            + "    ORDER BY r.puntajeGlobal DESC\n";
    Query query = this.entityManager.createQuery(queryString);

    return (List<Resultado>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ResultadoRepository#findByEstudianteId(java.lang.Long)}.
   */
  public List<Resultado> findByEstudianteId(@Param("estudianteId") Long estudianteId) {
    String queryString = "    SELECT r\n"
            + "    FROM Resultado r\n"
            + "    JOIN FETCH r.estudiante e\n"
            + "    WHERE e.id = :estudianteId\n"
            + "    ORDER BY r.periodo DESC, r.puntajeGlobal DESC\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("estudianteId", estudianteId);

    return (List<Resultado>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ResultadoRepository#findByPeriodoConEstudiante(java.lang.String)}.
   */
  public List<Resultado> findByPeriodoConEstudiante(@Param("periodo") String periodo) {
    String queryString = "    SELECT r\n"
            + "    FROM Resultado r\n"
            + "    JOIN FETCH r.estudiante e\n"
            + "    WHERE r.periodo = :periodo\n"
            + "    ORDER BY r.puntajeGlobal DESC\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("periodo", periodo);

    return (List<Resultado>) query.getResultList();
  }
}
