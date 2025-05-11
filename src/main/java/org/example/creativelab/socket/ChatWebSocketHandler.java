package org.example.creativelab.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.creativelab.dto.MessageDTO;
import org.example.creativelab.dto.MessageRequest;
import org.example.creativelab.service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Register JavaTimeModule
    private final MessageService messageService;

    public ChatWebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Extract userId from the URL query parameter
        String query = session.getUri().getQuery();
        String userIdStr = query.substring(query.indexOf("=") + 1);
        Long userId = Long.parseLong(userIdStr);

        // Store the session
        userSessions.put(userId, session);
        System.out.println("User " + userId + " connected");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MessageRequest messageRequest = objectMapper.readValue(message.getPayload(), MessageRequest.class);

        // Save the message and get the full MessageDTO (with sender/receiver ProfileDTO)
        MessageDTO messageDTO = messageService.sendMessage(messageRequest);

        // Get the receiver's session
        WebSocketSession receiverSession = userSessions.get(messageRequest.getReceiverId());

        String json = objectMapper.writeValueAsString(messageDTO);

        if (receiverSession != null && receiverSession.isOpen()) {
            // Send the full MessageDTO to the receiver
            receiverSession.sendMessage(new TextMessage(json));
        }

        // Send acknowledgment (full MessageDTO) back to sender
        session.sendMessage(new TextMessage(json));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Remove the session when user disconnects
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        System.out.println("User disconnected");
    }

    public String determineMessageType(MessageRequest messageRequest) {
        boolean hasText = messageRequest.getContent() != null && !messageRequest.getContent().isEmpty();
        boolean hasImage = messageRequest.getImageBase64() != null && !messageRequest.getImageBase64().isEmpty();

        if (hasText && hasImage) {
            return "text_and_image";
        } else if (hasImage) {
            return "image";
        } else if (hasText) {
            return "text";
        }

        return "unknown";
    }
}