package com.wtc.chat_backend.service;

import com.wtc.chat_backend.model.dto.CampaignRequest;
import com.wtc.chat_backend.model.dto.CampaignResponse;
import com.wtc.chat_backend.model.Campaign;
import com.wtc.chat_backend.model.Conversation;
import com.wtc.chat_backend.model.Message;
import com.wtc.chat_backend.model.enums.CampaignStatus;
import com.wtc.chat_backend.model.enums.ConversationStatus;
import com.wtc.chat_backend.model.enums.MessageStatus;
import com.wtc.chat_backend.model.enums.MessageType;
import com.wtc.chat_backend.repository.CampaignRepository;
import com.wtc.chat_backend.repository.ConversationRepository;
import com.wtc.chat_backend.repository.CustomerRepository;
import com.wtc.chat_backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository     campaignRepository;
    private final CustomerRepository     customerRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository      messageRepository;

    // ─── LISTAR ──────────────────────────────────────────────────────────────

    public List<CampaignResponse> findAll() {
        return campaignRepository.findAll()
                .stream()
                .map(CampaignResponse::from)
                .toList();
    }

    public CampaignResponse findById(String id) {
        return CampaignResponse.from(getOrThrow(id));
    }

    // ─── CRIAR E ENVIAR (ou agendar) ─────────────────────────────────────────

    public CampaignResponse create(CampaignRequest req) {

        if (req.segmentId() == null && (req.targetCustomerIds() == null || req.targetCustomerIds().isEmpty())) {
            throw new IllegalArgumentException("Informe segmentId ou targetCustomerIds");
        }

        // Resolve lista de destinatários
        List<String> recipientIds = resolveRecipients(req);

        Campaign campaign = new Campaign();
        campaign.setTitle(req.title());
        campaign.setContent(req.content());
        campaign.setSegmentId(req.segmentId());
        campaign.setTargetCustomerIds(recipientIds);
        campaign.setDeeplinkUrl(req.deeplinkUrl());
        campaign.setScheduledAt(req.scheduledAt());

        // Agendada ou envio imediato?
        if (req.scheduledAt() != null && req.scheduledAt().isAfter(LocalDateTime.now())) {
            campaign.setCampaignStatus(CampaignStatus.SCHEDULED);
            return CampaignResponse.from(campaignRepository.save(campaign));
        }

        // Envio imediato
        campaign.setCampaignStatus(CampaignStatus.SENT);
        campaign.setSentAt(LocalDateTime.now());
        Campaign saved = campaignRepository.save(campaign);

        dispatch(saved, recipientIds);

        return CampaignResponse.from(saved);
    }

    // ─── AGENDAR CAMPANHA EXISTENTE ──────────────────────────────────────────

    public CampaignResponse schedule(String id, LocalDateTime scheduledAt) {

        if (scheduledAt == null || scheduledAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data de agendamento deve ser futura");
        }

        Campaign campaign = getOrThrow(id);

        if (campaign.getCampaignStatus() == CampaignStatus.SENT) {
            throw new IllegalArgumentException("Campanha já foi enviada");
        }

        campaign.setScheduledAt(scheduledAt);
        campaign.setCampaignStatus(CampaignStatus.SCHEDULED);

        return CampaignResponse.from(campaignRepository.save(campaign));
    }

    // ─── ENVIAR CAMPANHA AGENDADA MANUALMENTE ────────────────────────────────

    public CampaignResponse sendNow(String id) {
        Campaign campaign = getOrThrow(id);

        if (campaign.getCampaignStatus() == CampaignStatus.SENT) {
            throw new IllegalArgumentException("Campanha já foi enviada");
        }

        campaign.setCampaignStatus(CampaignStatus.SENT);
        campaign.setSentAt(LocalDateTime.now());
        Campaign saved = campaignRepository.save(campaign);

        dispatch(saved, saved.getTargetCustomerIds());

        return CampaignResponse.from(saved);
    }

    // ─── HELPERS PRIVADOS ────────────────────────────────────────────────────

    private void dispatch(Campaign campaign, List<String> recipientIds) {
        String senderId = getCurrentUserId();

        for (String customerId : recipientIds) {
            try {
                // Busca ou cria conversa com o customer
                Conversation conversation = conversationRepository
                        .findByCustomerIdAndOperatorId(customerId, senderId)
                        .orElseGet(() -> {
                            Conversation conv = new Conversation();
                            conv.setCustomerId(customerId);
                            conv.setOperatorId(senderId);
                            conv.setConversationStatus(ConversationStatus.OPEN);
                            return conversationRepository.save(conv);
                        });

                // Atualiza timestamp da conversa
                conversation.setUpdatedAt(LocalDateTime.now());
                conversationRepository.save(conversation);

                // Cria a mensagem da campanha
                Message message = new Message();
                message.setConversationId(conversation.getId());
                message.setSenderId(senderId);
                message.setContent("[" + (campaign.getTitle() != null ? campaign.getTitle() : "Campanha")
                        + "]\n" + campaign.getContent());
                message.setMessageType(MessageType.TEXT);
                message.setDeeplinkUrl(campaign.getDeeplinkUrl());
                message.setMessageStatus(MessageStatus.SENT);
                messageRepository.save(message);

            } catch (Exception e) {
                // Falha em um customer não deve interromper os demais
            }
        }
    }

    private List<String> resolveRecipients(CampaignRequest req) {
        if (req.targetCustomerIds() != null && !req.targetCustomerIds().isEmpty()) {
            return req.targetCustomerIds();
        }

        // Busca todos os customers do segmento
        return customerRepository.findBySegmentId(req.segmentId())
                .stream()
                .map(c -> c.getId())
                .toList();
    }

    private Campaign getOrThrow(String id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campanha não encontrada: " + id));
    }

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}