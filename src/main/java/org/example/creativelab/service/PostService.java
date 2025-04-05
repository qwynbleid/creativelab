package org.example.creativelab.service;

import org.example.creativelab.model.Post;
import org.example.creativelab.model.UserEntity;
import org.example.creativelab.repository.PostRepository;
import org.example.creativelab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public Post createPost(String title, String content, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Создаем новый пост
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);

        // Сохраняем пост в базе
        return postRepository.save(post);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }
}
