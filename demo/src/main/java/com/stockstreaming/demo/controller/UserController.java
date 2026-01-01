package com.stockstreaming.demo.controller;


import com.stockstreaming.demo.dto.UserCreationRequest;
import com.stockstreaming.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public String createUser(@RequestBody UserCreationRequest request) {
        userService.createUser(request);
        return "User created successfully";
    }
}
