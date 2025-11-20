package com.stockstreaming.demo.service.query;
import com.stockstreaming.demo.model.FilterOperator;

import lombok.Data;

@Data
public class DynamicFilter {
    private String field;
    private FilterOperator operator;
    private Object value;
}
