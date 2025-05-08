package org.example.creativelab.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostDTO {
    private Long id;
    private UserDTO user;
    private String title;
    private String content;
    private byte[] imageData;
    private LocalDateTime createdAt;
    private int commentsCount;
    private int likesCount;
    private Set<String> tags;
}