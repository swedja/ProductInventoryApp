package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts(ProductRequest productRequest) {
        Pageable pageable = handlePaging(productRequest.getPaginationRequest());
        Filter filter = productRequest.getFilter();
        if(filter != null) {
            if (filter.getName() != null && !filter.getName().isEmpty() && filter.getPriceRange() == null) {
                return productRepository.findByName(filter.getName()).stream().map(this::convertToProductDTO).toList();
            }
            if(filter.getPriceRange() != null) {
                if (filter.getPriceRange().getMaxPrice() != null && filter.getPriceRange().getMinPrice() != null && (filter.getName() == null || filter.getName().isEmpty())) {
                    return productRepository.findByPriceBetween(filter.getPriceRange().getMinPrice(), filter.getPriceRange().getMaxPrice()).stream().map(this::convertToProductDTO).toList();
                }
                if (filter.getName() != null && !filter.getName().isEmpty() && filter.getPriceRange().getMinPrice() != null && filter.getPriceRange().getMaxPrice() != null) {
                    return productRepository.findByName(filter.getName())
                            .stream()
                            .filter(product ->
                                    product.getPrice() >= filter.getPriceRange().getMinPrice() && product.getPrice() <= filter.getPriceRange().getMaxPrice())
                            .map(this::convertToProductDTO)
                            .toList();
                }
            }
        }
        return productRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::convertToProductDTO)
                .toList();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToProductDTO)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());

        return convertToProductDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (updatedProduct.getName() != null) {
            existingProduct.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getQuantity() != null) {
            existingProduct.setQuantity(updatedProduct.getQuantity());
        }

        return convertToProductDTO(productRepository.save(existingProduct));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    private ProductDTO convertToProductDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getVersion());
    }

    Pageable handlePaging(PaginationRequest paginationRequest){
        int pageNumber = 0;
        int pageSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        if (paginationRequest != null){
            pageNumber = paginationRequest.getPage() -1;

            if(paginationRequest.getSize() > 0){
                pageSize = paginationRequest.getSize();
            }
            SortDirection sortDirection = SortDirection.ASC;
            if(paginationRequest.getSortDirection() != null){
                sortDirection = paginationRequest.getSortDirection();
            }
            String sortField = "name";
            if(paginationRequest.getSortField() != null){
                sortField = paginationRequest.getSortField();
            }
            sort = getSort(sortDirection, sortField);
        }
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private Sort getSort(SortDirection sortDirection, String sortField)
    {
        if (sortField == null)
        {
            return Sort.unsorted();
        }

        switch (sortDirection)
        {
            case DESC ->
            {
                return Sort.by(Sort.Direction.DESC, sortField);
            }
            case ASC ->
            {
                return Sort.by(Sort.Direction.ASC, sortField);
            }
            default ->
            {
                return Sort.unsorted();
            }
        }
    }

}