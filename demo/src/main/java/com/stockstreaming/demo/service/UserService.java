package com.stockstreaming.demo.service;

import com.stockstreaming.demo.dto.UserCreationRequest;
import com.stockstreaming.demo.model.AuthProvider;
import com.stockstreaming.demo.model.User;

public interface UserService {
    String createUser(UserCreationRequest request);


}
