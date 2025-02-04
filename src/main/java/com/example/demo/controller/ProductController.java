package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService IProductService;

    @Autowired
    public ProductController(IProductService IProductService) {
        this.IProductService = IProductService;
    }

    // Get all products
    @PostMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@Valid @RequestBody ProductRequest productRequest) {
        List<ProductDTO> products = IProductService.getAllProducts(productRequest);
        return ResponseEntity.ok(products);
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = IProductService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Create a new product
    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO product) {
        ProductDTO createdProduct = IProductService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Update a product by ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductDTO product) {
        ProductDTO updatedProduct = IProductService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        IProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}