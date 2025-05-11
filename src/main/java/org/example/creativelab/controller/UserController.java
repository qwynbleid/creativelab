package org.example.creativelab.controller;

import org.example.creativelab.dto.UserDTO;
import org.example.creativelab.mapper.UserMapper;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.service.UserEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UserEntityService userService;
    UserMapper userMapper;

    public UserController(UserEntityService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping("/follow")
    public ResponseEntity<Map<String, String>> followUser(
            @RequestParam Long followerId,
            @RequestParam String username) {
        try {
            userService.followUser(followerId, username);
            return ResponseEntity.ok(Map.of("message", "Followed successfully"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, String>> unfollowUser(
            @RequestParam Long followerId,
            @RequestParam String username) {
        try {
            userService.unfollowUser(followerId, username);
            return ResponseEntity.ok(Map.of("message", "Unfollowed successfully"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable Long userId) {
        try {
            List<UserDTO> followers = userService.getFollowers(userId)
                    .stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(followers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable Long userId) {
        try {
            List<UserDTO> following = userService.getFollowing(userId)
                    .stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(following);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @GetMapping("/{userId}/follow-stats")
    public ResponseEntity<Map<String, Integer>> getFollowStats(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowStats(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String searchTerm) {
        List<UserDTO> users = userService.searchUsers(searchTerm)
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
}
