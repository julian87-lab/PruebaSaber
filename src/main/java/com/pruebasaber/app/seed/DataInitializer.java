package com.pruebasaber.app.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pruebasaber.app.models.Estudiante;
import com.pruebasaber.app.models.Resultado;
import com.pruebasaber.app.models.Rol;
import com.pruebasaber.app.models.Usuario;
import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository resultadoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           EstudianteRepository estudianteRepository,
                           ResultadoRepository resultadoRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository = resultadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (usuarioRepository.findByEmail("admin@pruebasaber.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setEmail("admin@pruebasaber.com");
            admin.setPassword(passwordEncoder.encode("Admin123*"));
            admin.setRol(Rol.ADMIN);
            admin.setActivo(true);
            usuarioRepository.save(admin);
        }

        if (estudianteRepository.count() == 0) {
            Usuario usuarioEst = new Usuario();
            usuarioEst.setNombre("Juan");
            usuarioEst.setApellido("Barbosa");
            usuarioEst.setEmail("juan.barbosa@pruebasaber.com");
            usuarioEst.setPassword(passwordEncoder.encode("Juan123*"));
            usuarioEst.setRol(Rol.ESTUDIANTE);
            usuarioEst.setActivo(true);
            usuarioRepository.save(usuarioEst);

            Estudiante estudiante = new Estudiante();
            estudiante.setTipoDocumento("CC");
            estudiante.setNumeroDocumento("100000001");
            estudiante.setPrimerApellido("BARBOSA");
            estudiante.setSegundoApellido("GONZALEZ");
            estudiante.setPrimerNombre("JUAN");
            estudiante.setSegundoNombre("CARLOS");
            estudiante.setCorreoElectronico("juan.barbosa@pruebasaber.com");
            estudiante.setNumeroTelefono("3001234567");
            estudiante.setCodigoEstudiante("EK20183007722");
            estudiante.setUsuario(usuarioEst);
            estudiante.setActivo(true);
            estudianteRepository.save(estudiante);

            Resultado resultado = new Resultado();
            resultado.setPeriodo("2026-1");
            resultado.setPuntajeGlobal(200.0);
            resultado.setNivelPuntajeGlobal("Nivel 4");
            resultado.setComunicacionEscrita(128.0);
            resultado.setNivelComunicacionEscrita("Nivel 2");
            resultado.setRazonamientoCuantitativo(182.0);
            resultado.setNivelRazonamientoCuantitativo("Nivel 3");
            resultado.setLecturaCritica(202.0);
            resultado.setNivelLecturaCritica("Nivel 4");
            resultado.setCompetenciasCiudadanas(206.0);
            resultado.setNivelCompetenciasCiudadanas("Nivel 4");
            resultado.setIngles(183.0);
            resultado.setNivelIngles("Nivel 3");
            resultado.setFormulacionProyectosIngenieria(185.0);
            resultado.setNivelFormulacionProyectosIngenieria("Nivel 3");
            resultado.setPensamientoCientificoMatematicasEstadistica(160.0);
            resultado.setNivelPensamientoCientificoMatematicasEstadistica("Nivel 3");
            resultado.setDisenoSoftware(197.0);
            resultado.setNivelDisenoSoftware("Nivel 4");
            resultado.setNivelInglesCC("B1");
            resultado.setEstudiante(estudiante);

            resultadoRepository.save(resultado);
        }
    }
}