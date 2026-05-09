package com.wtc.chat_backend.repository;

import com.wtc.chat_backend.model.Campaign;
import com.wtc.chat_backend.model.enums.CampaignStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignRepository extends MongoRepository<Campaign, String> {

    List<Campaign> findBySegmentId(String segmentId);

    List<Campaign> findByCampaignStatus(CampaignStatus status);

    // Busca campanhas agendadas que já passaram do horário (para um scheduler futuro)
    List<Campaign> findByCampaignStatusAndScheduledAtBefore(
            CampaignStatus status, LocalDateTime dateTime);
}