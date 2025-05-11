package org.example.creativelab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private ProfileDTO sender;
    private ProfileDTO receiver;
    private String content;
    private String imageBase64;
    private LocalDateTime timestamp;
}
