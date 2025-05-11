package org.example.creativelab.mapper;

import org.example.creativelab.dto.ProfileDTO;
import org.example.creativelab.model.Profile;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
public class ProfileMapper {

    public ProfileDTO toDto(Profile profile) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setFullName(profile.getFullName());
        dto.setBio(profile.getBio());
        dto.setInterests(profile.getInterests());

        if (profile.getProfilePicture() != null) {
            String base64Picture = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(profile.getProfilePicture());
            dto.setProfilePicture(base64Picture);
        } else {
            dto.setProfilePicture(null);
        }

        return dto;
    }

}
