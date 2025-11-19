package com.stockstreaming.demo.service;

import com.stockstreaming.demo.model.FilterOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchFilter {

    private String field;       // e.g. "name" or "dealerLocations.city"
    private FilterOperator filterOperator;  // one of the enum values
    private List<String> values; // one or more string values (we'll convert types later)

}

