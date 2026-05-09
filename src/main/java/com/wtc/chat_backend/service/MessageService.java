package com.wtc.chat_backend.service;

import com.wtc.chat_backend.model.dto.InboxResponse;
import com.wtc.chat_backend.model.dto.MessageRequest;
import com.wtc.chat_backend.model.dto.MessageResponse;
import com.wtc.chat_backend.model.Conversation;
import com.wtc.chat_backend.model.Message;
import com.wtc.chat_backend.model.enums.ConversationStatus;
import com.wtc.chat_backend.model.enums.MessageStatus;
import com.wtc.chat_backend.repository.ConversationRepository;
import com.wtc.chat_backend.repository.CustomerRepository;
import com.wtc.chat_backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository      messageRepository;
    private final ConversationRepository conversationRepository;
    private final CustomerRepository     customerRepository;

    // ─── ENVIAR MENSAGEM ─────────────────────────────────────────────────────

    public List<MessageResponse> send(MessageRequest req) {

        String senderId = getCurrentUserId();

        // Envio para segmento → dispara para todos os customers do segmento
        if (req.customerId() == null && req.segmentId() != null) {
            return customerRepository.findBySegmentId(req.segmentId())
                    .stream()
                    .map(customer -> sendToCustomer(customer.getId(), senderId, req))
                    .toList();
        }

        // Envio 1:1
        if (req.customerId() != null) {
            return List.of(sendToCustomer(req.customerId(), senderId, req));
        }

        throw new IllegalArgumentException("Informe customerId ou segmentId");
    }

    // ─── BUSCAR MENSAGEM POR ID ──────────────────────────────────────────────

    public MessageResponse findById(String id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mensagem não encontrada: " + id));
        return MessageResponse.from(message);
    }

    // ─── INBOX DO CUSTOMER ───────────────────────────────────────────────────

    public List<InboxResponse> getInbox(String customerId) {
        return conversationRepository
                .findByCustomerIdOrderByUpdatedAtDesc(customerId)
                .stream()
                .map(conv -> {
                    MessageResponse lastMessage = messageRepository
                            .findTopByConversationIdOrderByCreatedAtDesc(conv.getId())
                            .map(MessageResponse::from)
                            .orElse(null);

                    long unread = messageRepository
                            .countByConversationIdAndMessageStatusNot(
                                    conv.getId(), MessageStatus.READ);

                    return new InboxResponse(
                            conv.getId(),
                            conv.getCustomerId(),
                            conv.getOperatorId(),
                            conv.getConversationStatus(),
                            lastMessage,
                            unread,
                            conv.getUpdatedAt()
                    );
                })
                .toList();
    }

    // ─── BUSCAR MENSAGENS DE UMA CONVERSA ────────────────────────────────────

    public List<MessageResponse> getByConversation(String conversationId) {
        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    // ─── ATUALIZAR STATUS ────────────────────────────────────────────────────

    public MessageResponse updateStatus(String id, MessageStatus newStatus) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mensagem não encontrada: " + id));

        message.setMessageStatus(newStatus);
        return MessageResponse.from(messageRepository.save(message));
    }

    // ─── HELPERS PRIVADOS ────────────────────────────────────────────────────

    private MessageResponse sendToCustomer(String customerId, String senderId, MessageRequest req) {

        // Busca conversa existente ou cria uma nova
        Conversation conversation = conversationRepository
                .findByCustomerIdAndOperatorId(customerId, senderId)
                .orElseGet(() -> createConversation(customerId, senderId));

        // Atualiza timestamp da conversa
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        // Cria e salva a mensagem
        Message message = new Message();
        message.setConversationId(conversation.getId());
        message.setSenderId(senderId);
        message.setContent(req.content());
        message.setMessageType(req.messageType());
        message.setMediaUrl(req.mediaUrl());
        message.setDeeplinkUrl(req.deeplinkUrl());
        message.setMessageStatus(MessageStatus.SENT);

        return MessageResponse.from(messageRepository.save(message));
    }

    private Conversation createConversation(String customerId, String operatorId) {
        Conversation conv = new Conversation();
        conv.setCustomerId(customerId);
        conv.setOperatorId(operatorId);
        conv.setConversationStatus(ConversationStatus.OPEN);
        return conversationRepository.save(conv);
    }

    private String getCurrentUserId() {
        // Retorna o email/id do usuário autenticado pelo JWT
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}