//package com.example.demo.controller;
//
//import com.example.demo.dto.ProductDTO;
//import com.example.demo.service.IProductService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ProductController.class)
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private IProductService productService; // Mock the service layer
//
//    // Test: Get all products
//    @Test
//    void shouldReturnListOfProducts() throws Exception {
//        // Mock response
//        List<ProductDTO> mockProducts = List.of(
//                new ProductDTO(1L, "Phone", "Smartphone", 699.99, 50, 1),
//                new ProductDTO(2L, "Laptop", "High-performance", 1299.99, 20, 1)
//        );
//
//        // Mock the service method
//        Mockito.when(productService.getAllProducts(any())).thenReturn(mockProducts);
//
//        // Perform GET /products
//        mockMvc.perform(get("/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}")) // Empty body for simplicity
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].name").value("Phone"))
//                .andExpect(jsonPath("$[1].name").value("Laptop"));
//    }
//
//    // Test: Get product by ID
//    @Test
//    void shouldReturnProductById() throws Exception {
//        // Mock response from the service
//        ProductDTO mockProduct = new ProductDTO(1L, "Phone", "Smartphone", 699.99, 50, 1);
//
//        Mockito.when(productService.getProductById(1L)).thenReturn(mockProduct);
//
//        // Perform GET /products/1
//        mockMvc.perform(get("/products/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Phone"))
//                .andExpect(jsonPath("$.description").value("Smartphone"))
//                .andExpect(jsonPath("$.price").value(699.99));
//    }
//
//    // Test: Create a new product
//    @Test
//    void shouldCreateNewProduct() throws Exception {
//        // Mock input and response
//        ProductDTO inputProduct = new ProductDTO(null, "Tablet", "High-end tablet", 599.99, 30, 0);
//        ProductDTO createdProduct = new ProductDTO(1L, "Tablet", "High-end tablet", 599.99, 30, 0);
//
//        Mockito.when(productService.createProduct(any(ProductDTO.class))).thenReturn(createdProduct);
//
//        // Perform POST /products
//        mockMvc.perform(post("/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                        {
//                          "name": "Tablet",
//                          "description": "High-end tablet",
//                          "price": "599.99",
//                          "quantity": 30
//                        }
//                        """))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Tablet"))
//                .andExpect(jsonPath("$.description").value("High-end tablet"));
//    }
//
//    // Test: Update a product
//    @Test
//    void shouldUpdateProduct() throws Exception {
//        // Mock input and response
//        ProductDTO updatedProduct = new ProductDTO(1L, "Updated Product", "Updated Description", 799.99, 40, 0);
//        Mockito.when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(updatedProduct);
//
//        // Perform PUT /products/1
//        mockMvc.perform(put("/products/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                        {
//                          "name": "Updated Product",
//                          "description": "Updated Description",
//                          "price": "799.99",
//                          "quantity": 40
//                        }
//                        """))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Product"))
//                .andExpect(jsonPath("$.description").value("Updated Description"))
//                .andExpect(jsonPath("$.price").value(799.99));
//    }
//
//    // Test: Delete a product
//    @Test
//    void shouldDeleteProduct() throws Exception {
//        // Mock service call
//        Mockito.doNothing().when(productService).deleteProduct(1L);
//
//        // Perform DELETE /products/1
//        mockMvc.perform(delete("/products/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//    }
//}