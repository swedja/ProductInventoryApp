package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
        Specification<Product> specification = buildProductSpecification(filter);

        return productRepository.findAll(specification, pageable).getContent()
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
    public ProductDTO updateProduct(Long id, UpdateProductDTO updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        updateNonNullProperties(updatedProduct, existingProduct);

        return convertToProductDTO(productRepository.save(existingProduct));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    //convert productEntity to productDTO
    private ProductDTO convertToProductDTO(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getVersion());
    }

    //Pagination helper method
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

    //Sort
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

    //Reflection for fields update
    public static void updateNonNullProperties(Object source, Object target) {
        try {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    Field targetField = target.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update properties", e);
        }
    }

    private Specification<Product> buildProductSpecification(Filter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Apply filter by name
            if (filter != null && filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }

            // Apply filter for price range
            if (filter != null && filter.getPriceRange() != null) {
                if (filter.getPriceRange().getMinPrice() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getPriceRange().getMinPrice()));
                }
                if (filter.getPriceRange().getMaxPrice() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getPriceRange().getMaxPrice()));
                }
            }

            // Combine predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}