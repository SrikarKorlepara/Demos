package com.stockstreaming.demo.service.query;

import lombok.Data;

@Data
public class FilterCondition {
    private String field;     // "name", "id", "businessId"
    private String operator;  // "equals", "contains", "greaterThan", "lessThan"
    private String value;     // "john", "5", etc.
}
