package org.example.creativelab.mapper;

import org.example.creativelab.dto.UserDTO;
import org.example.creativelab.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity user) {
        if (user == null || user.getProfile() == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getProfile().getUsername());
        dto.setFullName(user.getProfile().getFullName());
        dto.setProfilePicture(user.getProfile().getProfilePicture());

        return dto;
    }
}
