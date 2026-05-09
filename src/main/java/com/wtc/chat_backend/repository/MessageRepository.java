package com.wtc.chat_backend.repository;

import com.wtc.chat_backend.model.Message;
import com.wtc.chat_backend.model.enums.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, String> {

    // Todas as mensagens de uma conversa, ordenadas por data
    List<Message> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    // Última mensagem de uma conversa
    Optional<Message> findTopByConversationIdOrderByCreatedAtDesc(String conversationId);

    // Contagem de mensagens não lidas em uma conversa
    long countByConversationIdAndMessageStatusNot(String conversationId, MessageStatus status);

    // Últimas N mensagens de um customer (para timeline)
    List<Message> findTop5ByConversationIdOrderByCreatedAtDesc(String conversationId);
}