package com.example.demo.model;

import com.example.demo.model.Product;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION") // Ensure column name matches SQL schema
    private String description;


    @OneToMany(mappedBy = "category")
    private List<Product> products;

}