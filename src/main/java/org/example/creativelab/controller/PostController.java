package org.example.creativelab.controller;

import org.example.creativelab.dto.CommentDTO;
import org.example.creativelab.dto.PostDTO;
import org.example.creativelab.model.Post;
import org.example.creativelab.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "tags", required = false) List<String> tags) {
        try {
            return ResponseEntity.ok(postService.createPost(title, content, userId, tags, file));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating post with image");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(postService.getPostById(postId, userId));
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        postService.toggleLike(postId, userId);
        return ResponseEntity.ok("Like status toggled");
    }

    @GetMapping("/{postId}/check-like")
    public ResponseEntity<Map<String, Boolean>> checkIfLiked(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        boolean isLiked = postService.isPostLikedByUser(postId, userId);
        return ResponseEntity.ok(Map.of("isLiked", isLiked));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long postId,
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content) {

        CommentDTO createdComment = postService.addComment(postId, userId, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId) {
        List<CommentDTO> commentDTOs = postService.getCommentDTOsByPostId(postId);
        return ResponseEntity.ok(commentDTOs);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostDTO>> getUserFeed(@RequestParam Long userId) {
        return ResponseEntity.ok(postService.getFeedForUser(userId));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<PostDTO>> getRecommendations(@RequestParam Long userId) {
        List<PostDTO> recommendations = postService.getRecommendedPosts(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/{userId}/all-posts")
    public ResponseEntity<List<PostDTO>> getPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getAllUserPosts(userId));
    }

    @GetMapping("/tags/popular")
    public ResponseEntity<Map<String, Long>> getPopularTags() {
        return ResponseEntity.ok(postService.getPopularTags());
    }

    @GetMapping("/search/by-tag")
    public ResponseEntity<List<PostDTO>> searchPostsByTag(
            @RequestParam("tag") String tag,
            @RequestParam("excludeUserId") Long excludeUserId) {
        return ResponseEntity.ok(postService.searchPostsByTag(tag, excludeUserId));
    }
}
