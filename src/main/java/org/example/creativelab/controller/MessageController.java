package org.example.creativelab.controller;

import org.example.creativelab.dto.MessageDTO;
import org.example.creativelab.dto.MessageRequest;
import org.example.creativelab.mapper.MessageMapper;
import org.example.creativelab.model.Message;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public MessageDTO sendMessage(@RequestBody MessageRequest messageRequest) {
        return messageService.sendMessage(messageRequest);
    }

    @GetMapping("/between/{senderId}/{receiverId}")
    public List<MessageDTO> getMessagesBetweenUsers(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return messageService.getMessagesBetweenUsers(senderId, receiverId);
    }

    @GetMapping("/for/{userId}")
    public List<MessageDTO> getMessagesForUser(@PathVariable Long userId) {
        return messageService.getMessagesForUser(userId);
    }
}
