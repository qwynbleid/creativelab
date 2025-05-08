package org.example.creativelab.service;

import jakarta.transaction.Transactional;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.repository.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;

    public UserEntityService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public Optional<UserEntity> getUserById(Long userId) {
        return userEntityRepository.findById(userId);
    }

    @Transactional
    public void followUser(Long followerId, String username) {
        UserEntity user = userEntityRepository.findByProfile_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        UserEntity follower = userEntityRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found with id: " + followerId));

        if (user.getId().equals(followerId)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }

        if (user.getFollowers().contains(follower)) {
            throw new IllegalStateException("User is already followed");
        }

        user.getFollowers().add(follower);
        follower.getFollowing().add(user);

        userEntityRepository.save(user);
        userEntityRepository.save(follower);
    }

    @Transactional
    public void unfollowUser(Long followerId, String username) {
        UserEntity user = userEntityRepository.findByProfile_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        UserEntity follower = userEntityRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found with id: " + followerId));

        if (!user.getFollowers().contains(follower)) {
            throw new IllegalStateException("User is not followed");
        }

        user.getFollowers().remove(follower);
        follower.getFollowing().remove(user);

        userEntityRepository.save(user);
        userEntityRepository.save(follower);
    }

    public List<UserEntity> getFollowers(Long userId) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return new ArrayList<>(user.getFollowers());
    }

    public List<UserEntity> getFollowing(Long userId) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return new ArrayList<>(user.getFollowing());
    }
}
