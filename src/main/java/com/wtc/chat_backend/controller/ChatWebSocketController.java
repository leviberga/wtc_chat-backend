package com.wtc.chat_backend.controller;

import com.wtc.chat_backend.model.dto.MessageRequest;
import com.wtc.chat_backend.model.dto.StatusUpdateRequest;
import com.wtc.chat_backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageRequest request) {
        var responses = messageService.send(request);

        responses.forEach(response ->
                messagingTemplate.convertAndSend(
                        "/topic/conversation." + response.conversationId(),
                        response
                )
        );
    }

    @MessageMapping("/chat.status")
    public void updateStatus(@Payload StatusUpdateRequest request) {
        var response = messageService.updateStatus(request.messageId(), request.status());

        messagingTemplate.convertAndSend(
                "/queue/user." + response.conversationId(),
                response
        );
    }
}