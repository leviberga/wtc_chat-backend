package com.wtc.chat_backend.model;

import com.wtc.chat_backend.model.enums.ConversationStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "conversations")
public class Conversation {

    @Id
    private String id;

    private String customerId;
    private String operatorId;
    private ConversationStatus conversationStatus;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}