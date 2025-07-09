package com.example.demo.service;

import com.example.demo.models.Product;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> obtenerProductosFiltrados(String ordenarPor, String orden, String nombre, Integer cantidad) {
        List<Product> productos;

        if ("ventas".equalsIgnoreCase(ordenarPor)) {
            productos = "descendente".equalsIgnoreCase(orden)
                    ? productRepository.findAllByOrderByCantidadVendidaDesc()
                    : productRepository.findAllByOrderByCantidadVendidaAsc();
        } else if ("stock".equalsIgnoreCase(ordenarPor)) {
            productos = productRepository.findAllByOrderByStockAsc();
        } else {
            productos = productRepository.findAll();
        }

        if (nombre != null && !nombre.isEmpty()) {
            productos = productos.stream()
                    .filter(p -> p.getName().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (cantidad != null && cantidad > 0 && cantidad < productos.size()) {
            productos = productos.subList(0, cantidad);
        }

        return productos;
    }

    @Override
    public List<Product> obtenerTodosLosProductos() {
        return productRepository.findAll();
    }
}
