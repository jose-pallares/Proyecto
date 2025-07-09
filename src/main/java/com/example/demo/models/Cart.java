package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")  // ✅ nombre correcto según la BD
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {           // ✅ nombre de clase con mayúscula

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")     // ✅ asegura coincidencia con la BD
    private Long userId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity")    // ✅ mapeo correcto del campo en la BD
    private Integer cantidad;
}
