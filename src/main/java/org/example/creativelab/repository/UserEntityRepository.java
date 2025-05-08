package org.example.creativelab.repository;

import org.example.creativelab.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByProfile_Username(String username);
}
