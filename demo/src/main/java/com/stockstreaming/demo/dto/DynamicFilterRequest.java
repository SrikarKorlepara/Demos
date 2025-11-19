package com.stockstreaming.demo.dto;

import com.stockstreaming.demo.service.query.FilterCondition;
import lombok.Data;
import java.util.List;

@Data
public class DynamicFilterRequest {
    private List<FilterCondition> filters;
}
