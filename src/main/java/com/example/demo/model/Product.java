package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @NotBlank(message = "Name is required.")
    private String name; // Product name

    @NotEmpty(message = "Description is required.")
    @Column(nullable = false)
    private String description; // Product description

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @Column(nullable = false)
    private Double price; // Product price

    @NotNull(message = "Quantity is required.")
    @Min(value = 0, message = "Quantity must be at least 0.")
    @Column(nullable = false)
    private Integer quantity; // Available quantity of the product

    @Version
    private Integer version; // Optimistic locking field


    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
}