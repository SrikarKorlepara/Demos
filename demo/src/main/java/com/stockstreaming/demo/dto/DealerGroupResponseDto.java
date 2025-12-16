package com.stockstreaming.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealerGroupResponseDto {

    private Long id;
    private String name;
    private String businessId;
    private String description;

}
