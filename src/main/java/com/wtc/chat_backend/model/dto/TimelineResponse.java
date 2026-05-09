package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TimelineResponse(
        // Dados básicos
        String customerId,
        String name,
        String email,
        String phone,
        String segmentName,
        List<String> tags,
        Double score,
        CustomerStatus customerStatus,
        LocalDateTime createdAt,

        // Histórico — será expandido quando Message/Campaign estiverem prontos
        List<MessageSummary> lastMessages,
        List<CampaignSummary> lastCampaigns,
        List<TaskSummary> openTasks
) {
    // Sub-records para não criar arquivos extras agora
    public record MessageSummary(String id, String content, LocalDateTime sentAt) {}
    public record CampaignSummary(String id, String title, LocalDateTime receivedAt) {}
    public record TaskSummary(String id, String description, String status) {}
}