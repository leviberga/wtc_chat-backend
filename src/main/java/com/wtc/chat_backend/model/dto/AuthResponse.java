package com.wtc.chat_backend.model.dto;

public record AuthResponse(
        String token,
        String type,      // sempre "Bearer"
        String userId,
        String name,
        String email,
        String role
) {
    // Construtor de conveniência para não esquecer o type
    public static AuthResponse of(String token, String userId,
                                  String name, String email, String role) {
        return new AuthResponse(token, "Bearer", userId, name, email, role);
    }
}