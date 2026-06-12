package com.pruebasaber.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }
}