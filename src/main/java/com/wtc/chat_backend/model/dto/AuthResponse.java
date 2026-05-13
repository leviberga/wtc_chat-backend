package com.wtc.chat_backend.model.dto;

public record AuthResponse(
        String token,
        String refreshToken,
        String type,
        String userId,
        String name,
        String email,
        String role
) {
    public static AuthResponse of(String token, String refreshToken, String userId,
                                  String name, String email, String role) {
        return new AuthResponse(token, refreshToken, "Bearer", userId, name, email, role);
    }
}