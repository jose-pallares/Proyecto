package com.example.demo.service;

import com.example.demo.models.Cart;
import com.example.demo.models.Product;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void addToCart(Long productId, Long userId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            Cart item = new Cart();
            item.setProduct(product);
            item.setUserId(userId);
            item.setCantidad(1);
            cartRepository.save(item);
        }
    }

    @Override
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId)
                      .forEach(item -> cartRepository.deleteById(item.getId()));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ NUEVO MÉTODO: actualizar cantidad en carrito
    @Override
    public void updateCantidad(Long cartId, Integer cantidad) {
        Cart item = cartRepository.findById(cartId).orElse(null);
        if (item != null && cantidad != null && cantidad > 0) {
            item.setCantidad(cantidad);
            cartRepository.save(item);
        }
    }





    @Override
public void confirmarCompra(Long userId) {
    List<Cart> carrito = cartRepository.findByUserId(userId);

    for (Cart item : carrito) {
        Product producto = item.getProduct();
        int cantidadComprada = item.getCantidad();

        if (producto.getStock() >= cantidadComprada) {
            producto.setStock(producto.getStock() - cantidadComprada);
            producto.setCantidadVendida(producto.getCantidadVendida() + cantidadComprada);
            productRepository.save(producto);
        } else {
            throw new RuntimeException("No hay suficiente stock para: " + producto.getName());
        }
    }

    clearCart(userId);
}

}
