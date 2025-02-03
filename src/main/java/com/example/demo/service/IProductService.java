package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductRequest;

import java.util.List;

public interface IProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts(ProductRequest productRequest);

    ProductDTO updateProduct(Long id, ProductDTO product);

    void deleteProduct(Long id);
}