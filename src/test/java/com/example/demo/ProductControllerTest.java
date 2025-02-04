package com.example.demo;

import com.example.demo.controller.ProductController;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.IProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService; // Mock the service layer

    @Test
    void shouldReturnAllProducts() throws Exception {
        List<ProductDTO> products = List.of(
                new ProductDTO(1L, "Phone", "Smartphone", 699.99, 50, 1),
                new ProductDTO(2L, "Laptop", "High-performance", 1299.99, 20, 1)
        );
        Mockito.when(productService.getAllProducts(Mockito.any())).thenReturn(products);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(products.size()))
                .andExpect(jsonPath("$[0].name").value(products.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(products.get(1).getName()));
    }


    @Test
    void shouldReturnProductById() throws Exception {
        ProductDTO mockProduct = new ProductDTO(1L, "Phone", "Smartphone", 699.99, 50, 1);

        Mockito.when(productService.getProductById(1L)).thenReturn(mockProduct);

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.description").value("Smartphone"))
                .andExpect(jsonPath("$.price").value(699.99));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductDTO product = new ProductDTO(null, "Phone", "Smartphone", 699.99, 50, null);
        ProductDTO createdProduct = new ProductDTO(1L, "Phone", "Smartphone", 699.99, 50, 1);
        Mockito.when(productService.createProduct(product)).thenReturn(createdProduct);

        mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "name": "Phone",
                                      "description": "Smartphone",
                                      "price": 699.99,
                                      "quantity": 50
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdProduct.getId()))
                .andExpect(jsonPath("$.name").value(createdProduct.getName()))
                .andExpect(jsonPath("$.price").value(createdProduct.getPrice()));
    }


    @Test
    void shouldUpdateProduct() throws Exception {
        ProductDTO updatedProduct = new ProductDTO(1L, "Updated Product", "Updated Description", 799.99, 40, 0);
        Mockito.when(productService.updateProduct(eq(1L), any(com.example.demo.dto.UpdateProductDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Product",
                                  "description": "Updated Description",
                                  "price": "799.99",
                                  "quantity": 40
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.price").value(799.99));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}