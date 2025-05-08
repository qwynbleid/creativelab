package org.example.creativelab.controller;

import org.example.creativelab.model.AuthRequest;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.security.JwtUtil;
import org.example.creativelab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        UserEntity user = userService.getUserByEmail(authRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(authRequest.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(authRequest.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("userId", user.getId().toString());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Refresh token expired"));
        }

        String email = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(email);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody AuthRequest request) {
        Long userId = userService.saveUser(request.getEmail(), request.getPassword()).getId();
        return ResponseEntity.ok(userId);
    }
}
