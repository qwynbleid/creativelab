package org.example.creativelab.controller;

import org.example.creativelab.model.Post;
import org.example.creativelab.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<String> createPostWithImage(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("userId") Long userId) {

        try {
            // Сначала создаем пост
            Post post = postService.createPost(title, content, userId);

            // Если файл был загружен, сохраняем его в картинке поста
            if (file != null && !file.isEmpty()) {
                post.setImageData(file.getBytes());
                post.setImageUrl(file.getOriginalFilename());  // Если хочешь хранить имя файла
                postService.savePost(post); // Сохраняем пост с картинкой
                return ResponseEntity.ok("Post created with image: " + post.getId());
            }

            return ResponseEntity.ok("Post created: " + post.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating post with image");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);  // Возвращаем весь объект поста
        }
        return ResponseEntity.notFound().build();
    }
}
