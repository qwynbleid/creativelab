package org.example.creativelab.service;

import org.example.creativelab.model.Comment;
import org.example.creativelab.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
