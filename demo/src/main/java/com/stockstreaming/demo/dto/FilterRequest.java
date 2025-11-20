package com.stockstreaming.demo.dto;

import com.stockstreaming.demo.service.query.SearchQuery;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class FilterRequest {

    private SearchQuery query;

    private int page = 0;
    private int size = 20;

    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";
}
