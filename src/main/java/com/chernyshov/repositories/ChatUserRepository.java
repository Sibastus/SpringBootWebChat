package com.chernyshov.repositories;

import com.chernyshov.entities.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    ChatUser findByLogin(String login);
}
