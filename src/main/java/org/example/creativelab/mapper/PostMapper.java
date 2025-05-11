package org.example.creativelab.mapper;

import org.example.creativelab.dto.PostDTO;
import org.example.creativelab.dto.UserDTO;
import org.example.creativelab.model.Post;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class PostMapper {

    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PostDTO toDto(Post post, Long userId) {
        UserDTO userDTO = userMapper.toDTO(post.getUser());

        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());

        if (post.getPostImage() != null) {
            String base64Picture = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(post.getPostImage());
            dto.setPostImage(base64Picture);
        } else {
            dto.setPostImage(null);
        }

        dto.setCreatedAt(post.getCreatedAt());
        dto.setUser(userDTO);
        dto.setCommentsCount(post.getComments().size());
        dto.setLikesCount(post.getLikes().size());
        dto.setTags(post.getTags());

        boolean isLikedByUser = post.getLikes().stream()
                .anyMatch(user -> user.getId().equals(userId));
        dto.setLikedByUser(isLikedByUser);

        return dto;
    }
}