package com.wtc.chat_backend.service;

import com.wtc.chat_backend.model.dto.AuthResponse;
import com.wtc.chat_backend.model.dto.LoginRequest;
import com.wtc.chat_backend.model.dto.RegisterRequest;
import com.wtc.chat_backend.model.User;
import com.wtc.chat_backend.model.enums.Role;
import com.wtc.chat_backend.repository.UserRepository;
import com.wtc.chat_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;
    private final MessageService messageService;

    // ─── REGISTRO ────────────────────────────────────────────────────────────

    public AuthResponse register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + req.email());
        }

        String role = (req.role() != null && req.role().equalsIgnoreCase("OPERATOR"))
                ? "OPERATOR"
                : "CLIENT";

        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(Role.valueOf(role));

        User saved = userRepository.save(user);

        if (saved.getRole() == Role.CLIENT) {
            String customerId = customerService.ensurePortalProfileForClient(saved.getName(), saved.getEmail());
            messageService.ensureWelcomePortalConversation(customerId, saved.getName());
        }

        String token = jwtUtil.generateToken(saved.getEmail(), String.valueOf(saved.getRole()));
        String refreshToken = jwtUtil.generateRefreshToken(saved.getEmail());
        return AuthResponse.of(token, refreshToken, saved.getId(), saved.getName(),
                saved.getEmail(), String.valueOf(saved.getRole()));
    }

    // ─── LOGIN ────────────────────────────────────────────────────────────────

    public AuthResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + req.email()));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("Senha incorreta");
        }

        if (user.getRole() == Role.CLIENT) {
            String customerId = customerService.ensurePortalProfileForClient(user.getName(), user.getEmail());
            messageService.ensureWelcomePortalConversation(customerId, user.getName());
        }

        String token = jwtUtil.generateToken(user.getEmail(), String.valueOf(user.getRole()));
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        return AuthResponse.of(token, refreshToken, user.getId(), user.getName(),
                user.getEmail(), String.valueOf(user.getRole()));
    }
}