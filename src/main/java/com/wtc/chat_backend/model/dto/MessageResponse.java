package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.Message;
import com.wtc.chat_backend.model.enums.MessageStatus;
import com.wtc.chat_backend.model.enums.MessageType;

import java.time.LocalDateTime;

public record MessageResponse(
        String id,
        String conversationId,
        String senderId,
        String content,
        MessageType messageType,
        String mediaUrl,
        String deeplinkUrl,
        MessageStatus messageStatus,
        LocalDateTime createdAt
) {
    public static MessageResponse from(Message m) {
        return new MessageResponse(
                m.getId(),
                m.getConversationId(),
                m.getSenderId(),
                m.getContent(),
                m.getMessageType(),
                m.getMediaUrl(),
                m.getDeeplinkUrl(),
                m.getMessageStatus(),
                m.getCreatedAt()
        );
    }
}