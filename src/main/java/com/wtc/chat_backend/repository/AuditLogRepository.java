package com.wtc.chat_backend.repository;

import com.wtc.chat_backend.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByUserIdOrderByTimestampDesc(String userId);
    List<AuditLog> findByEntityAndEntityIdOrderByTimestampDesc(String entity, String entityId);
}