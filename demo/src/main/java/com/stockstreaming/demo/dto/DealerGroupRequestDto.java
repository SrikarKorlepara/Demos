package com.stockstreaming.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealerGroupRequestDto {

    private Long id;
    private String businessId;
    private String name;
    private String description;

}
