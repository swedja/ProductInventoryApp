package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.UpdateProductDTO;

import java.util.List;

public interface IProductService {
    ProductDTO createProduct(ProductDTO product);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts(ProductRequest productRequest);

    ProductDTO updateProduct(Long id, UpdateProductDTO product);

    void deleteProduct(Long id);
}