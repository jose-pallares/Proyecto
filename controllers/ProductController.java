package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.PdfExportService;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdfExportService pdfExportService;

    // Mostrar el panel admin con filtros aplicados
    @GetMapping("/admin-panel")
    public String mostrarPanel(HttpSession session,
                                Model model,
                                @RequestParam(required = false) String ordenarPor,
                                @RequestParam(required = false) String orden,
                                @RequestParam(required = false) String nombre,
                                @RequestParam(required = false) Integer cantidad) {

        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            return "redirect:/";
        }

        List<Product> productos = productService.obtenerProductosFiltrados(ordenarPor, orden, nombre, cantidad);
        model.addAttribute("productos", productos);
        model.addAttribute("usuarios", userRepository.findAll());
        return "admin-panel";
    }

    // Exportar los productos filtrados a PDF
    @GetMapping("/admin-panel/exportar-pdf")
    public void exportarReporteProductos(HttpServletResponse response,
                                         HttpSession session,
                                         @RequestParam(required = false) String ordenarPor,
                                         @RequestParam(required = false) String orden,
                                         @RequestParam(required = false) String nombre,
                                         @RequestParam(required = false) Integer cantidad) throws IOException {

        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null || !"admin".equals(usuario.getRole())) {
            response.sendRedirect("/");
            return;
        }

        List<Product> productosFiltrados = productService.obtenerProductosFiltrados(ordenarPor, orden, nombre, cantidad);
        pdfExportService.exportarProductosA(response, productosFiltrados);
    }
}
