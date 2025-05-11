package org.example.creativelab.repository;

import org.example.creativelab.model.Message;
import org.example.creativelab.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(UserEntity sender, UserEntity receiver);
    List<Message> findByReceiver(UserEntity receiver);
}
