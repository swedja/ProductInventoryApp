package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationRequest {
    private int page;
    private int size;
    private String sortField;
    private SortDirection sortDirection;
}
