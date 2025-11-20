package com.stockstreaming.demo.service.query;

import com.stockstreaming.demo.service.query.SearchCriteria;
import lombok.Data;

import java.util.List;

@Data
public class SearchQuery {
    private List<SearchCriteria> filters;
}
