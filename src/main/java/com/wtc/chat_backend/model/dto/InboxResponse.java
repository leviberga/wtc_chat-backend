package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.enums.ConversationStatus;

import java.time.LocalDateTime;

// Cada item do inbox = conversa + última mensagem + contagem de não lidas
public record InboxResponse(
        String conversationId,
        String customerId,
        String operatorId,
        ConversationStatus conversationStatus,
        MessageResponse lastMessage,
        long unreadCount,
        LocalDateTime updatedAt
) {}