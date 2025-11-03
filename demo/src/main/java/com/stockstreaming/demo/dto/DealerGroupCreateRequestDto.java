package com.stockstreaming.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealerGroupCreateRequestDto {
    private String businessId;
    private String name;
    private String description;
}
