package com.stockstreaming.demo.dto;

import java.util.List;

public record LoginResponse(String token,String userId ,String name,
                            List<String> roles) {
}
