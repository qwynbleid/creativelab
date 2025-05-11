package org.example.creativelab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private Long senderId;
    private Long receiverId;
    private String type;
    private String content;
    private String imageBase64;
}