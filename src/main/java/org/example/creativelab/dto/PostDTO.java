package org.example.creativelab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private UserDTO user;
    private String title;
    private String content;
    private String postImage;
    private LocalDateTime createdAt;
    private int commentsCount;
    private int likesCount;
    private boolean isLikedByUser;
    private Set<String> tags;
}