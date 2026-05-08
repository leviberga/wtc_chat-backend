package com.wtc.chat_backend.model;

import com.wtc.chat_backend.model.enums.CampaignStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "campaigns")
public class Campaign {

    @Id
    private String id;

    private String title;
    private String content;
    private String segmentId;
    private List<String> targetCustomerIds;
    private String deeplinkUrl;
    private CampaignStatus campaignStatus;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt = LocalDateTime.now();
}