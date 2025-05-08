package org.example.creativelab.controller;

import org.example.creativelab.model.Profile;
import org.example.creativelab.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // Оновлення або створення профілю
    @PostMapping("/{userId}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long userId,
                                                 @RequestParam String username,
                                                 @RequestParam String fullName,
                                                 @RequestParam String bio,
                                                 @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                                 @RequestParam(value = "interests", required = false) List<String> interests) throws IOException {
        Profile updatedProfile = profileService.createOrUpdateProfile(userId, username, fullName, bio, profilePicture, interests);
        return ResponseEntity.ok(updatedProfile);
    }

    // Отримання профілю
    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }
}