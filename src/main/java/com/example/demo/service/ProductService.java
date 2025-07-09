package com.example.demo.service;

import com.example.demo.models.Product;
import java.util.List;

public interface ProductService {
    List<Product> obtenerProductosFiltrados(String ordenarPor, String orden, String nombre, Integer cantidad);
    List<Product> obtenerTodosLosProductos(); // ← Nuevo método
}
