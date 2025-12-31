package com.stockstreaming.demo.service;

import com.stockstreaming.demo.dto.UserCreationRequest;

public interface UserService {
    String createUser(UserCreationRequest request);

}
