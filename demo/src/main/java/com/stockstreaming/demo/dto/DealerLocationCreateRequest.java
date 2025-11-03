package com.stockstreaming.demo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealerLocationCreateRequest {

    @NotBlank(message = "Dealer Group ID is required")
    @Size(max = 50, message = "Dealer Group ID must not exceed 50 characters")
    private String dealerGroupId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private String address;

    private String city;

    private String phoneNumber;


}
