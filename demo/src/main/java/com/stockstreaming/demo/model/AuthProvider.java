package com.stockstreaming.demo.model;

public enum AuthProvider {
    LOCAL,
    GITHUB;

    public static AuthProvider fromRegistrationId(String registrationId) {
        for (AuthProvider provider : values()) {
            if (provider.name().equalsIgnoreCase(registrationId)) {
                return provider;
            }
        }
        throw new IllegalArgumentException(
                "Unsupported OAuth provider: " + registrationId
        );
    }
}
