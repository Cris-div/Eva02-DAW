package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class VistaController {

    // Página principal
    @GetMapping( "/index")
    public String index() {
        return "index";
    }
    @GetMapping("/error")
    public String error() {
        return "error";
    }
    // Login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Register
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Redirige al index después del login si quieres usar fetch
    @GetMapping("/home")
    public RedirectView homeAfterLogin() {
        return new RedirectView("/");
    }

    // Otras vistas
    @GetMapping("/pacientes")
    public String pacientes() {
        return "pacientes";
    }

    @GetMapping("/citas")
    public String citas() {
        return "citas";
    }

    @GetMapping("/consultas")
    public String consultas() {
        return "consultas";
    }

    @GetMapping("/hospitalizacion")
    public String hospitalizacion() {
        return "hospitalizacion";
    }

    @GetMapping("/facturacion")
    public String facturacion() {
        return "facturacion";
    }

    @GetMapping("/usuarios")
    public String usuarios() {
        return "usuarios";
    }
}
