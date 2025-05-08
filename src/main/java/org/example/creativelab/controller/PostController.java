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
            @RequestParam("userId") Long userId) {

        try {
            Post post = postService.createPost(title, content, userId);

            if (file != null && !file.isEmpty()) {
                post.setImageData(file.getBytes());
                postService.savePost(post); 
                return ResponseEntity.ok("Post created with image: " + post.getId());
            }

            return ResponseEntity.ok("Post created: " + post.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating post with image");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
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

    @GetMapping("/{userId}/all-posts")
    public ResponseEntity<List<PostDTO>> getPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getAllUserPosts(userId));
    }

}
