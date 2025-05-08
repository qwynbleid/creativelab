package org.example.creativelab.mapper;

import org.example.creativelab.dto.CommentDTO;
import org.example.creativelab.dto.UserDTO;
import org.example.creativelab.model.Comment;
import org.example.creativelab.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommentDTO toDto(Comment comment) {
        UserEntity user = comment.getUser();
        UserDTO userDTO = userMapper.toDTO(user);
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                userDTO
        );
    }
}
