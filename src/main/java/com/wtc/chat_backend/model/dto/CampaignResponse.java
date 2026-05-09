package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.Campaign;
import com.wtc.chat_backend.model.enums.CampaignStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CampaignResponse(
        String id,
        String title,
        String content,
        String segmentId,
        List<String> targetCustomerIds,
        String deeplinkUrl,
        CampaignStatus campaignStatus,
        int totalRecipients,
        LocalDateTime scheduledAt,
        LocalDateTime sentAt,
        LocalDateTime createdAt
) {
    public static CampaignResponse from(Campaign c) {
        return new CampaignResponse(
                c.getId(),
                c.getTitle(),
                c.getContent(),
                c.getSegmentId(),
                c.getTargetCustomerIds(),
                c.getDeeplinkUrl(),
                c.getCampaignStatus(),
                c.getTargetCustomerIds() != null ? c.getTargetCustomerIds().size() : 0,
                c.getScheduledAt(),
                c.getSentAt(),
                c.getCreatedAt()
        );
    }
}