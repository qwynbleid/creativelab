package org.example.creativelab.service;

import jakarta.transaction.Transactional;
import org.example.creativelab.dto.CommentDTO;
import org.example.creativelab.dto.PostDTO;
import org.example.creativelab.mapper.CommentMapper;
import org.example.creativelab.mapper.PostMapper;
import org.example.creativelab.model.Comment;
import org.example.creativelab.model.Post;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.repository.CommentRepository;
import org.example.creativelab.repository.PostRepository;
import org.example.creativelab.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final PostMapper postMapper;
    private final UserEntityService userEntityService;

    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, CommentMapper commentMapper, CommentService commentService, PostMapper postMapper, UserEntityService userEntityService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
        this.postMapper = postMapper;
        this.userEntityService = userEntityService;
    }

    public Post createPost(String title, String content, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);

        return postRepository.save(post);
    }

    public PostDTO getPostById(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return postMapper.toDto(post, userId);
    }

    public List<PostDTO> getAllUserPosts(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getPosts().stream()
                .map(post -> postMapper.toDto(post, userId))
                .collect(Collectors.toList());
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public void toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
        } else {
            post.getLikes().add(user);
        }

        postRepository.save(post);
    }

    public List<CommentDTO> getCommentDTOsByPostId(Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    public CommentDTO addComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    @Transactional
    public List<PostDTO> getFeedForUser(Long userId) {
        UserEntity user = userEntityService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Set<UserEntity> following = user.getFollowing();
        List<Post> posts = postRepository.findByUserInOrderByCreatedAtDesc(following);

        return posts.stream()
                .map(post -> postMapper.toDto(post, userId))
                .collect(Collectors.toList());
    }
    public boolean isPostLikedByUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getLikes().stream()
                .anyMatch(like -> like.getId().equals(userId));
    }

    public List<PostDTO> getRecommendedPosts(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> interests = user.getProfile().getInterests();
        if (interests.isEmpty()) {
            return Collections.emptyList();
        }

        List<Post> recommendedPosts = postRepository.findByTagsIn(interests);
        return recommendedPosts.stream()
                .map(post -> postMapper.toDto(post, userId))
                .collect(Collectors.toList());
    }
}
