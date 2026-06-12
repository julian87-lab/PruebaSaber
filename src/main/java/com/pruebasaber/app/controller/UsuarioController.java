package com.pruebasaber.app.controller;

import com.pruebasaber.app.models.Rol;
import com.pruebasaber.app.models.Usuario;
import com.pruebasaber.app.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q,
                         @RequestParam(required = false) String estado,
                         Model model) {

        String textoBusqueda = q != null ? q.trim() : "";
        String estadoFiltro = estado != null ? estado.trim() : "";

        List<Usuario> usuarios;

        boolean hayBusqueda = !textoBusqueda.isBlank();
        boolean hayEstado = !estadoFiltro.isBlank();

        if (hayBusqueda && hayEstado) {
            if ("activos".equalsIgnoreCase(estadoFiltro)) {
                usuarios = usuarioRepository
                        .findByActivoTrueAndNombreContainingIgnoreCaseOrActivoTrueAndApellidoContainingIgnoreCaseOrActivoTrueAndEmailContainingIgnoreCase(
                                textoBusqueda, textoBusqueda, textoBusqueda);
            } else if ("inactivos".equalsIgnoreCase(estadoFiltro)) {
                usuarios = usuarioRepository
                        .findByActivoFalseAndNombreContainingIgnoreCaseOrActivoFalseAndApellidoContainingIgnoreCaseOrActivoFalseAndEmailContainingIgnoreCase(
                                textoBusqueda, textoBusqueda, textoBusqueda);
            } else {
                usuarios = usuarioRepository
                        .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrEmailContainingIgnoreCase(
                                textoBusqueda, textoBusqueda, textoBusqueda);
            }
        } else if (hayBusqueda) {
            usuarios = usuarioRepository
                    .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            textoBusqueda, textoBusqueda, textoBusqueda);
        } else if ("activos".equalsIgnoreCase(estadoFiltro)) {
            usuarios = usuarioRepository.findByActivoTrue();
        } else if ("inactivos".equalsIgnoreCase(estadoFiltro)) {
            usuarios = usuarioRepository.findByActivoFalse();
        } else {
            usuarios = usuarioRepository.findAll();
        }

        cargarModelo(model, usuarios, new Usuario(), false, textoBusqueda, estadoFiltro);
        return "usuarios/usuarios";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("nuevoUsuario") Usuario usuario,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {

        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.email", "Ya existe un usuario con ese correo.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            result.rejectValue("password", "error.password", "La contraseña es obligatoria.");
        }

        if (result.hasErrors()) {
            cargarModelo(model, usuarioRepository.findAll(), usuario, false, null, null);
            return "usuarios/usuarios";
        }

        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
            flash.addFlashAttribute("mensajeExito", "Usuario creado correctamente.");
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("mensajeError", "No fue posible guardar el usuario por datos duplicados.");
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            flash.addFlashAttribute("mensajeError", "El usuario no existe.");
            return "redirect:/usuarios";
        }

        usuario.setPassword("");
        cargarModelo(model, usuarioRepository.findAll(), usuario, true, null, null);
        return "usuarios/usuarios";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("nuevoUsuario") Usuario formulario,
                             BindingResult result,
                             Model model,
                             RedirectAttributes flash) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            flash.addFlashAttribute("mensajeError", "No se pudo actualizar el usuario.");
            return "redirect:/usuarios";
        }

        if (formulario.getEmail() != null
                && !usuario.getEmail().equalsIgnoreCase(formulario.getEmail())
                && usuarioRepository.existsByEmail(formulario.getEmail())) {
            result.rejectValue("email", "error.email", "Ya existe otro usuario con ese correo.");
        }

        if (result.hasErrors()) {
            formulario.setId(id);
            formulario.setPassword("");
            cargarModelo(model, usuarioRepository.findAll(), formulario, true, null, null);
            return "usuarios/usuarios";
        }

        usuario.setNombre(formulario.getNombre());
        usuario.setApellido(formulario.getApellido());
        usuario.setEmail(formulario.getEmail());
        usuario.setRol(formulario.getRol());
        usuario.setActivo(formulario.isActivo());

        if (formulario.getPassword() != null && !formulario.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(formulario.getPassword()));
        }

        try {
            usuarioRepository.save(usuario);
            flash.addFlashAttribute("mensajeExito", "Usuario actualizado correctamente.");
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("mensajeError", "No fue posible actualizar el usuario.");
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes flash) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        });

        flash.addFlashAttribute("mensajeExito", "Usuario desactivado correctamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes flash) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
        });

        flash.addFlashAttribute("mensajeExito", "Usuario activado correctamente.");
        return "redirect:/usuarios";
    }

    private void cargarModelo(Model model,
                              List<Usuario> usuarios,
                              Usuario nuevoUsuario,
                              boolean modoEdicion,
                              String q,
                              String estado) {
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("nuevoUsuario", nuevoUsuario);
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("q", q);
        model.addAttribute("estado", estado);
        model.addAttribute("roles", Arrays.asList(Rol.values()));
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalActivos", usuarioRepository.countByActivoTrue());
        model.addAttribute("totalInactivos", usuarioRepository.countByActivoFalse());
    }
}