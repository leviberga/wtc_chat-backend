package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.enums.MessageStatus;

public record StatusUpdateRequest(
        String messageId,
        MessageStatus status
) {}