package com.wtc.chat_backend.controller;

import com.wtc.chat_backend.model.dto.InboxResponse;
import com.wtc.chat_backend.model.dto.MessageRequest;
import com.wtc.chat_backend.model.dto.MessageResponse;
import com.wtc.chat_backend.model.enums.MessageStatus;
import com.wtc.chat_backend.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // POST /messages — envio 1:1 ou para segmento
    @PostMapping("/messages")
    public ResponseEntity<List<MessageResponse>> send(@Valid @RequestBody MessageRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(req));
    }

    // GET /messages/{id}
    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(messageService.findById(id));
    }

    // PATCH /messages/{id}/status — atualiza status (DELIVERED, READ, FAILED)
    @PatchMapping("/messages/{id}/status")
    public ResponseEntity<MessageResponse> updateStatus(
            @PathVariable String id,
            @RequestParam MessageStatus status) {
        return ResponseEntity.ok(messageService.updateStatus(id, status));
    }

    // GET /inbox/{customerId} — inbox do customer com última mensagem e não lidas
    @GetMapping("/inbox/{customerId}")
    public ResponseEntity<List<InboxResponse>> getInbox(@PathVariable String customerId) {
        return ResponseEntity.ok(messageService.getInbox(customerId));
    }

    // GET /conversations/{conversationId}/messages — histórico completo
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<MessageResponse>> getByConversation(
            @PathVariable String conversationId) {
        return ResponseEntity.ok(messageService.getByConversation(conversationId));
    }
}