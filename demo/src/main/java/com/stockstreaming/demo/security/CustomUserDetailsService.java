package com.stockstreaming.demo.security;

import com.stockstreaming.demo.model.AuthProvider;
import com.stockstreaming.demo.model.User;
import com.stockstreaming.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new BadCredentialsException(
                    "Password login not allowed for OAuth users"
            );
        }

        if(!user.getEnabled()) {
            throw new BadCredentialsException("User account is disabled");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList())
                .accountLocked(false)
                .disabled(!user.getEnabled())
                .accountExpired(false)
                .build();
    }
}
