package com.wtc.chat_backend.controller;

import com.wtc.chat_backend.model.AuditLog;
import com.wtc.chat_backend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(auditLogRepository.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(auditLogRepository.findByUserIdOrderByTimestampDesc(userId));
    }
}