package com.wtc.chat_backend.model;

import com.wtc.chat_backend.model.enums.AuditAction;
import com.wtc.chat_backend.model.enums.AuditEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    private String userId;
    private AuditAction auditAction;
    private AuditEntity auditEntity;
    private String entityId;
    private String ip;
    private LocalDateTime timestamp = LocalDateTime.now();
}