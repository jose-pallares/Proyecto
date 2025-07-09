package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.models.Product;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    // Mostrar panel de administración (usuarios + productos)
    @GetMapping
    public String index(HttpSession session, Model model) {
        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            return "redirect:/";
        }

        List<User> usuarios = userRepository.findAll();
        List<Product> productos = productService.obtenerTodosLosProductos();

        model.addAttribute("usuarios", usuarios);  // importante: "usuarios", no "users"
        model.addAttribute("productos", productos);

        return "admin-panel";
    }

    // Crear usuario
    @GetMapping("/create")
    public String create(HttpSession session, Model model) {
        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "users/create";
    }

    @PostMapping
    public String store(@ModelAttribute User user, HttpSession session) {
        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            return "redirect:/";
        }

        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, HttpSession session, Model model) {
        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            return "redirect:/";
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "users/edit";
        } else {
            return "redirect:/users";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute User updatedUser, HttpSession session) {
        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            return "redirect:/";
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
        }

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            return "redirect:/";
        }

        userRepository.deleteById(id);
        return "redirect:/users";
    }
}
