package com.example.demo.service;

import com.example.demo.models.Cart;
import com.example.demo.models.Product;
import java.util.List;

public interface CartService {
    List<Cart> getCartByUserId(Long userId);
    void addToCart(Long productId, Long userId);
    void removeFromCart(Long cartId);
    void clearCart(Long userId);
    void confirmarCompra(Long userId);

    List<Product> getAllProducts();

    // ✅ Método faltante:
    void updateCantidad(Long cartId, Integer cantidad);
}
