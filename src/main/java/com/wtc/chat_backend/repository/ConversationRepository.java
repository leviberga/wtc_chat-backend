package com.wtc.chat_backend.repository;

import com.wtc.chat_backend.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation, String> {

    // Busca conversa existente entre customer e operator
    Optional<Conversation> findByCustomerIdAndOperatorId(String customerId, String operatorId);

    // Todas as conversas de um customer (inbox do cliente)
    List<Conversation> findByCustomerIdOrderByUpdatedAtDesc(String customerId);

    // Todas as conversas de um operator
    List<Conversation> findByOperatorIdOrderByUpdatedAtDesc(String operatorId);
}