package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(

        // Para envio 1:1 — informe customerId
        String customerId,

        // Para envio em massa — informe segmentId
        // Se ambos forem informados, customerId tem prioridade
        String segmentId,

        @NotBlank(message = "Conteúdo é obrigatório")
        String content,

        @NotNull(message = "Tipo é obrigatório")
        MessageType messageType,

        String mediaUrl,      // opcional — link de imagem
        String deeplinkUrl    // opcional — deeplink interno
) {}