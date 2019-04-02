package com.chernyshov.repositories;

import com.chernyshov.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByReceiverLogin(String login);

    void deleteByReceiverLogin(String login);
}
