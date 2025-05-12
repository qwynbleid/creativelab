package org.example.creativelab.mapper;

import org.example.creativelab.dto.UserDTO;
import org.example.creativelab.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getProfile().getUsername());
        dto.setFullName(user.getProfile().getFullName());

        if (user.getProfile().getProfilePicture() != null) {
            String base64Picture = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getProfile().getProfilePicture());
            dto.setProfilePicture(base64Picture);
        } else {
            dto.setProfilePicture(null);
        }

        dto.setInterests(user.getProfile().getInterests());

        return dto;
    }
}
