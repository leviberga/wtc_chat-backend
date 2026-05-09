package com.wtc.chat_backend.config;

import com.wtc.chat_backend.model.User;
import com.wtc.chat_backend.model.enums.Role;
import com.wtc.chat_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Só executa se não houver usuários cadastrados
        if (userRepository.count() > 0) {
            log.info("DataInitializer — usuários já existem, pulando seed.");
            return;
        }

        log.info("DataInitializer — criando usuários iniciais...");

        // ── Operador 1 ────────────────────────────────────────────────────────
        User op1 = new User();
        op1.setId("op001");
        op1.setName("Ana Operadora");
        op1.setEmail("ana@wtc.com");
        op1.setPassword(passwordEncoder.encode("senha123"));
        op1.setRole(Role.valueOf("OPERATOR"));
        userRepository.save(op1);

        // ── Operador 2 ────────────────────────────────────────────────────────
        User op2 = new User();
        op2.setId("op002");
        op2.setName("Carlos Operador");
        op2.setEmail("carlos@wtc.com");
        op2.setPassword(passwordEncoder.encode("senha123"));
        op2.setRole(Role.valueOf("OPERATOR"));
        userRepository.save(op2);

        // ── Cliente (para testar login pelo app) ──────────────────────────────
        User client = new User();
        client.setId("usr_client01");
        client.setName("João Silva");
        client.setEmail("joao.silva@fintech.com");
        client.setPassword(passwordEncoder.encode("senha123"));
        client.setRole(Role.valueOf("CLIENT"));
        userRepository.save(client);

        log.info("DataInitializer — usuários criados com sucesso!");
        log.info("  Operador 1 → ana@wtc.com     / senha123");
        log.info("  Operador 2 → carlos@wtc.com  / senha123");
        log.info("  Cliente    → joao.silva@fintech.com / senha123");
    }
}
