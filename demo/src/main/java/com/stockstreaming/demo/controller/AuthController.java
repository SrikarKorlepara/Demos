package com.stockstreaming.demo.controller;

import com.stockstreaming.demo.dto.LoginRequest;
import com.stockstreaming.demo.dto.LoginResponse;
import com.stockstreaming.demo.model.User;
import com.stockstreaming.demo.repository.UserRepository;
import com.stockstreaming.demo.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/test")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;


    @GetMapping("/hello")
    @PreAuthorize("isAuthenticated()")
    public String hello() {
        return "Hello, authenticated user! " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Hello, admin user!";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        log.info("Login attempt for user: {}", loginRequest.username());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        String jwt = jwtUtils.generateTokenFromUser(user);
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        log.info("User {} logged in successfully", authentication.getName());
        return ResponseEntity.ok(new LoginResponse(jwt, user.getId().toString(), user.getUsername(), roles));
    }

}