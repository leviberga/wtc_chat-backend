package com.wtc.chat_backend.model;

import com.wtc.chat_backend.model.enums.MessageStatus;
import com.wtc.chat_backend.model.enums.MessageType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private String conversationId;
    private String senderId;
    private String content;
    private MessageType messageType;
    private String mediaUrl;
    private String deeplinkUrl;
    private MessageStatus messageStatus;
    private LocalDateTime createdAt = LocalDateTime.now();
}