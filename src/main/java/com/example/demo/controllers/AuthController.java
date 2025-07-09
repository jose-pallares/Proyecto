package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // login.html
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro"; // registro.html
    }

    // Página de prueba para admin
    @GetMapping("/test-admin")
    public String testAdmin() {
        return "admin-panel"; // admin-panel.html
    }

    // Registrar usuario
    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String confirmar,
                                   Model model) {

        if (!password.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "registro";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "El email ya está registrado.");
            return "registro";
        }

        User usuario = new User();
        usuario.setName(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));

        // Asignar rol
        if (email.equalsIgnoreCase("admin@gmail.com")) {
            usuario.setRole("admin");
        } else {
            usuario.setRole("user");
        }

        usuario.setEstado("Desconectado");
        userRepository.save(usuario);

        model.addAttribute("mensaje", "Usuario registrado con éxito. Inicia sesión.");
        return "login";
    }

    // Iniciar sesión
    @PostMapping("/acceder")
    public String iniciarSesion(@RequestParam String email,
                                @RequestParam String password,
                                Model model,
                                HttpSession session) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setEstado("En línea");
                userRepository.save(user);

                session.setAttribute("usuario", user);
                return "redirect:/inicio";
            }
        }

        model.addAttribute("error", "Credenciales incorrectas.");
        return "login";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        User usuario = (User) session.getAttribute("usuario");

        if (usuario != null) {
            usuario.setEstado("Desconectado");
            userRepository.save(usuario); // Actualiza estado en la base de datos
        }

        session.invalidate(); // Cierra la sesión
        return "redirect:/login?logout";
    }
}
