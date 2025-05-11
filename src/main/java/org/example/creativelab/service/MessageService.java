package org.example.creativelab.service;

import jakarta.transaction.Transactional;
import org.example.creativelab.dto.MessageDTO;
import org.example.creativelab.dto.MessageRequest;
import org.example.creativelab.mapper.MessageMapper;
import org.example.creativelab.model.Message;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Transactional
    public MessageDTO sendMessage(MessageRequest messageRequest) {
        // Создаем отправителя и получателя на основе их id
        UserEntity sender = new UserEntity();
        sender.setId(messageRequest.getSenderId());

        UserEntity receiver = new UserEntity();
        receiver.setId(messageRequest.getReceiverId());

        // Преобразуем imageBase64 в byte[]
        byte[] image = null;
        if (messageRequest.getImageBase64() != null && !messageRequest.getImageBase64().isEmpty()) {
            image = Base64.getDecoder().decode(messageRequest.getImageBase64());
        }

        // Создаем сообщение и сохраняем его в базе данных
        Message message = new Message(sender, receiver, messageRequest.getContent(), image);
        message = messageRepository.save(message);

        // Возвращаем DTO, но с ProfileDTO вместо UserDTO
        return messageMapper.toMessageDTO(message);
    }

    public List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        UserEntity sender = new UserEntity();
        sender.setId(senderId);
        UserEntity receiver = new UserEntity();
        receiver.setId(receiverId);

        // Получаем список сообщений между двумя пользователями
        List<Message> messages = messageRepository.findBySenderAndReceiver(sender, receiver);
        return messages.stream()
                .map(messageMapper::toMessageDTO)
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessagesForUser(Long userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);

        // Получаем все сообщения, где пользователь является получателем
        List<Message> messages = messageRepository.findByReceiver(user);
        return messages.stream()
                .map(messageMapper::toMessageDTO)
                .collect(Collectors.toList());
    }
}