package com.wtc.chat_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public record CampaignRequest(

        @NotBlank(message = "Título é obrigatório")
        String title,

        @NotBlank(message = "Conteúdo é obrigatório")
        String content,

        // Envio para segmento inteiro OU lista específica de customers
        // Pelo menos um dos dois deve ser informado
        String segmentId,
        List<String> targetCustomerIds,

        String deeplinkUrl,

        // Se informado, agenda para essa data — senão envia imediatamente
        LocalDateTime scheduledAt
) {}