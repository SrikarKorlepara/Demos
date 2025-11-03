package com.stockstreaming.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealerLocationResponseDto {
    private String locationId;
    private String name;
    private String address;
    private String city;
    private String phoneNumber;
    private DealerGroupResponseDto dealerGroup;
}
