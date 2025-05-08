package org.example.creativelab.mapper;

import org.example.creativelab.dto.PostDTO;
import org.example.creativelab.dto.UserDTO;
import org.example.creativelab.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PostDTO toDto(Post post) {
        UserDTO userDTO = userMapper.toDTO(post.getUser());

        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImageData(post.getImageData());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUser(userDTO);
        dto.setCommentsCount(post.getComments().size());
        dto.setLikesCount(post.getLikes().size());
        dto.setTags(post.getTags());

        return dto;
    }
}