package com.pruebasaber.app.repository;

import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link EstudianteRepository}.
 */
@Generated
public class EstudianteRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public EstudianteRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#existsByCodigoEstudiante(java.lang.String)}.
   */
  public boolean existsByCodigoEstudiante(String codigoEstudiante) {
    String queryString = "SELECT e.id FROM Estudiante e WHERE e.codigoEstudiante = :codigoEstudiante";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("codigoEstudiante", codigoEstudiante);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#existsByNumeroDocumento(java.lang.String)}.
   */
  public boolean existsByNumeroDocumento(String numeroDocumento) {
    String queryString = "SELECT e.id FROM Estudiante e WHERE e.numeroDocumento = :numeroDocumento";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("numeroDocumento", numeroDocumento);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#findByActivoFalse()}.
   */
  public List<Estudiante> findByActivoFalse() {
    String queryString = "SELECT e FROM Estudiante e WHERE e.activo = FALSE";
    Query query = this.entityManager.createQuery(queryString);

    return (List<Estudiante>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#findByActivoTrue()}.
   */
  public List<Estudiante> findByActivoTrue() {
    String queryString = "SELECT e FROM Estudiante e WHERE e.activo = TRUE";
    Query query = this.entityManager.createQuery(queryString);

    return (List<Estudiante>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#findByCodigoEstudiante(java.lang.String)}.
   */
  public Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante) {
    String queryString = "SELECT e FROM Estudiante e WHERE e.codigoEstudiante = :codigoEstudiante";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("codigoEstudiante", codigoEstudiante);

    return Optional.ofNullable((Estudiante) convertOne(query.getSingleResultOrNull(), false, Estudiante.class));
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#findByNumeroDocumento(java.lang.String)}.
   */
  public Optional<Estudiante> findByNumeroDocumento(String numeroDocumento) {
    String queryString = "SELECT e FROM Estudiante e WHERE e.numeroDocumento = :numeroDocumento";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("numeroDocumento", numeroDocumento);

    return Optional.ofNullable((Estudiante) convertOne(query.getSingleResultOrNull(), false, Estudiante.class));
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#findByPrimerNombreContainingIgnoreCaseOrPrimerApellidoContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCase(java.lang.String,java.lang.String,java.lang.String)}.
   */
  public List<Estudiante> findByPrimerNombreContainingIgnoreCaseOrPrimerApellidoContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCase(
      String primerNombre, String primerApellido, String numeroDocumento) {
    String queryString = "SELECT e FROM Estudiante e WHERE UPPER(e.primerNombre) LIKE UPPER(:primerNombre) ESCAPE '\\' OR UPPER(e.primerApellido) LIKE UPPER(:primerApellido) ESCAPE '\\' OR UPPER(e.numeroDocumento) LIKE UPPER(:numeroDocumento) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("primerNombre", "%%%s%%".formatted(primerNombre != null ? primerNombre.toUpperCase() : primerNombre));
    query.setParameter("primerApellido", "%%%s%%".formatted(primerApellido != null ? primerApellido.toUpperCase() : primerApellido));
    query.setParameter("numeroDocumento", "%%%s%%".formatted(numeroDocumento != null ? numeroDocumento.toUpperCase() : numeroDocumento));

    return (List<Estudiante>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EstudianteRepository#findByUsuario(com.pruebasaber.app.models.Usuario)}.
   */
  public Optional<Estudiante> findByUsuario(Usuario usuario) {
    String queryString = "SELECT e FROM Estudiante e WHERE e.usuario = :usuario";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("usuario", usuario);

    return Optional.ofNullable((Estudiante) convertOne(query.getSingleResultOrNull(), false, Estudiante.class));
  }
}
