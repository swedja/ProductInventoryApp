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
    private Long id;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Description is required.")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Quantity is required.")
    @Min(value = 0, message = "Quantity must be at least 0.")
    @Column(nullable = false)
    private Integer quantity;

    @Version
    private Integer version;


    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
}