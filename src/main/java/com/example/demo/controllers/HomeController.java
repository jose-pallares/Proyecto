package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    // Mostrar inicio cuando accedes a "/inicio"
    @GetMapping("/inicio")
    public String mostrarInicio(HttpSession session, Model model) {
        User usuario = (User) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        }

        return "inicio";
    }

    // Redirigir "/" a "/inicio"
    @GetMapping("/")
    public String redireccionRaiz() {
        return "redirect:/inicio";
    }
}
