package com.stockstreaming.demo.security;


import com.stockstreaming.demo.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter{

    private final JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtUtils.getJwtFromHeader(request);
            if (token != null && jwtUtils.validateJwtToken(token)) {
                Claims claims = jwtUtils.extractClaims(token);
                String userId = claims.getSubject();

                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles");
                List<GrantedAuthority> authorities =
                        roles.stream()
                                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                                .toList();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,null,authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Authentication failed with following {}",e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
