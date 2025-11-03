package com.stockstreaming.demo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealerLocationRequestDto {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Location ID is required")
    @Size(max = 50, message = "Location ID must not exceed 50 characters")
    private String locationId;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    @NotNull(message = "Dealer Group Business ID is required")
    private String dealerGroupBusinessId;
}
