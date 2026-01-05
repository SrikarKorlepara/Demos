package com.stockstreaming.demo.config;


import com.stockstreaming.demo.security.AuthEntryPointJwt;
import com.stockstreaming.demo.security.AuthTokenFilter;
import com.stockstreaming.demo.security.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;

    private final AuthTokenFilter authTokenFilter;

    private final AuthEntryPointJwt authenticationEntryPoint;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/users/create",
                                "/api/auth/test/login",
                                "/auth/**",
                                "/oauth2/**",
                                "/login/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()
                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2->oauth2
                        .successHandler(oAuth2SuccessHandler))

                .addFilterBefore(
                        authTokenFilter, UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(
                        exception->exception.authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(
                        session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .headers(headers->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception{
        return builder.getAuthenticationManager();

    }

    //    @Bean
//    @Order(1)
//    SecurityFilterChain formLoginChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/login", "/oauth2/**", "/h2-console/**")
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                )
//                .formLogin(form-> form
//                        .successHandler((request, response, authentication) -> {
//                            String jwt = jwtUtils.generateTokenFromUsername(
//                                    (UserDetails) authentication.getPrincipal()
//                            );
//                            response.setContentType("application/json");
//                            response.getWriter().write("{\"token\":\"" + jwt + "\"}");
//                        }).defaultSuccessUrl("/api/auth/test/hello", true))
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .csrf(AbstractHttpConfigurer::disable)
//                .headers(headers ->
//                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
//                );
//
//        return http.build();
//    }

}
