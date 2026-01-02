package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.dto.UserCreationRequest;
import com.stockstreaming.demo.model.AuthProvider;
import com.stockstreaming.demo.model.Role;
import com.stockstreaming.demo.model.User;
import com.stockstreaming.demo.repository.RoleRepository;
import com.stockstreaming.demo.repository.UserRepository;
import com.stockstreaming.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public String createUser(UserCreationRequest request) {
        if(userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Default role not found"));

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .enabled(false)
                .build();
        user.addRole(defaultRole);
        userRepository.save(user);

        return "User created successfully";
    }




}
