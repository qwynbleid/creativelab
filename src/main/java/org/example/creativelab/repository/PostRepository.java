package org.example.creativelab.repository;

import org.example.creativelab.model.Post;
import org.example.creativelab.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserInOrderByCreatedAtDesc(Set<UserEntity> users);
}
