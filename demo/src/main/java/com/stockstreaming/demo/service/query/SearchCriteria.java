package com.stockstreaming.demo.service.query;

import com.stockstreaming.demo.model.FilterOperator;
import lombok.Data;

@Data
public class SearchCriteria {
    private String field;       // e.g., "name"
    private FilterOperator operator;  // e.g., CONTAINS
    private Object value;       // e.g., "srikar"
}
