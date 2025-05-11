package org.example.creativelab.mapper;

import org.example.creativelab.dto.MessageDTO;
import org.example.creativelab.dto.ProfileDTO;
import org.example.creativelab.dto.UserDTO;
import org.example.creativelab.model.Message;
import org.example.creativelab.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;

@Component
public class MessageMapper {

    public MessageDTO toMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSender(toProfileDTO(message.getSender()));
        messageDTO.setReceiver(toProfileDTO(message.getReceiver()));
        messageDTO.setContent(message.getContent());
        if (message.getImage() != null) {
            messageDTO.setImageBase64(Base64.getEncoder().encodeToString(message.getImage()));
        }
        messageDTO.setTimestamp(message.getTimestamp());
        return messageDTO;
    }

    private ProfileDTO toProfileDTO(UserEntity user) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(user.getId());
        profileDTO.setUsername(user.getProfile() != null ? user.getProfile().getUsername() : "");
        profileDTO.setFullName(user.getProfile() != null ? user.getProfile().getFullName() : "");
        profileDTO.setBio(user.getProfile() != null ? user.getProfile().getBio() : "");

        if (user.getProfile() != null && user.getProfile().getProfilePicture() != null) {
            byte[] profilePicture = user.getProfile().getProfilePicture();
            profileDTO.setProfilePicture(Base64.getEncoder().encodeToString(profilePicture));
        } else {
            profileDTO.setProfilePicture("");
        }

        profileDTO.setInterests(user.getProfile() != null ? user.getProfile().getInterests() : new ArrayList<>());

        return profileDTO;
    }
}