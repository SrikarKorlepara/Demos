package com.stockstreaming.demo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealerGroupRequestDto {

    private Long id;

    @NotBlank(message = "Business ID is required")
    @Size(max = 50, message = "Business ID must not exceed 50 characters")
    private String businessId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private List<DealerLocationResponseDto> dealerLocations;

}
