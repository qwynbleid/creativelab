package org.example.creativelab.service;

import jakarta.transaction.Transactional;
import org.example.creativelab.model.Profile;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    public ProfileService(ProfileRepository profileRepository, UserService userService) {
        this.profileRepository = profileRepository;
        this.userService = userService;
    }

    @Transactional
    public Profile createOrUpdateProfile(Long userId, String username, String fullName, String bio, MultipartFile profilePicture, List<String> interests) throws IOException {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = user.getProfile();

        if (profile == null) {
            profile = new Profile();
            user.setProfile(profile);
        }

        profile.setUsername(username);
        profile.setFullName(fullName);
        profile.setBio(bio);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            profile.setProfilePicture(profilePicture.getBytes());
        }

        if (interests != null && !interests.isEmpty()) {
            profile.setInterests(interests);
        }

        profileRepository.save(profile);
        return profile;
    }

    public Profile getProfile(Long userId) {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProfile();
    }
}