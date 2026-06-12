package com.pruebasaber.app.controller;

import com.pruebasaber.app.models.Usuario;
import com.pruebasaber.app.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiPerfilController {

    private final UsuarioRepository usuarioRepository;

    public MiPerfilController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/mi-perfil")
    public String verMiPerfil(Authentication authentication, Model model) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No hay usuario autenticado en la sesión.");
        }

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + email));

        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Mi perfil");

        return "estudiantes/mi-perfil";
    }
}