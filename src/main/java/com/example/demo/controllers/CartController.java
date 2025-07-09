package com.example.demo.controllers;

import com.example.demo.service.CartService;
import com.example.demo.models.Cart;
import com.example.demo.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CartController {

    @Autowired
    private CartService cartService;

    // Mostrar catálogo y carrito
    @GetMapping
    public String viewCart(Model model) {
        Long userId = 11L;
        List<Cart> items = cartService.getCartByUserId(userId);
        List<Product> catalogo = cartService.getAllProducts();
        model.addAttribute("carrito", items);
        model.addAttribute("productos", catalogo);
        return "carrito";
    }

    // Mostrar vista CRUD del carrito
    @GetMapping("/crud")
    public String crudCart(Model model) {
        Long userId = 11L;
        List<Cart> items = cartService.getCartByUserId(userId);
        model.addAttribute("carrito", items);
        return "carrito_crud";
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public String addToCart(@RequestParam("productId") Long id) {
        cartService.addToCart(id, 11L);
        return "redirect:/carrito/crud";
    }

    // Eliminar producto del carrito
    @PostMapping("/eliminar")
    public String removeFromCart(@RequestParam Long cartId) {
        cartService.removeFromCart(cartId);
        return "redirect:/carrito/crud";
    }

    // Actualizar cantidad (opcional si implementas formulario de edición)
    @PostMapping("/editar")
    public String updateCantidad(@RequestParam Long cartId, @RequestParam Integer cantidad) {
        cartService.updateCantidad(cartId, cantidad);
        return "redirect:/carrito/crud";
    }

   @PostMapping("/confirmar")
public String confirmarCompra(Model model) {
    Long userId = 11L;
    try {
        cartService.confirmarCompra(userId);
        model.addAttribute("mensaje", "¡Compra exitosa!");
    } catch (RuntimeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "carrito_crud"; // o redirige donde prefieras
    }
    return "compra_exitosa";
}


}
