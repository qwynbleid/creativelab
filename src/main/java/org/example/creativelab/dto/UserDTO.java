package org.example.creativelab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private byte[] profilePicture;
}
