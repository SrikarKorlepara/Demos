package com.stockstreaming.demo.security;

import com.stockstreaming.demo.model.AuthProvider;
import com.stockstreaming.demo.model.Role;
import com.stockstreaming.demo.model.User;
import com.stockstreaming.demo.repository.RoleRepository;
import com.stockstreaming.demo.repository.UserRepository;
import com.stockstreaming.demo.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final JwtUtils jwtUtils;

//    @Value("${spring.app.redirectUrl}")
    private String redirectUrl = "http://localhost:3000/oauth2/redirect?token=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("OAuth2 User Attributes: {}", oAuth2User.getAttributes().toString());
        String githubId = Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
        String login = Objects.requireNonNull(oAuth2User.getAttribute("login")).toString();

        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        AuthProvider authProvider;
        try {
            authProvider = AuthProvider.fromRegistrationId(registrationId);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid OAuth provider: {}", registrationId);
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unsupported OAuth provider"
            );
            return;
        }
        User user = findByAuthProviderAndProviderIdOrCreateUser(authProvider, githubId, login);

        String jwt = jwtUtils.generateTokenFromUser(user);

        response.sendRedirect(
                redirectUrl + jwt
        );

    }

    private User findByAuthProviderAndProviderIdOrCreateUser(AuthProvider authProvider, String providerId, String email) {
        return userRepository.findByAuthProviderAndProviderId(authProvider, providerId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(email)
                            .authProvider(authProvider)
                            .providerId(providerId)
                            .enabled(true)
                            .roles(new HashSet<>())
                            .build();
                    Role defaultRole = roleRepository.findByName("ROLE_USER")
                            .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
                    newUser.addRole(defaultRole);
                    return userRepository.save(newUser);
                });

    }
}
