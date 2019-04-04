package com.chernyshov.services.dataServices;


import com.chernyshov.entities.ChatUser;
import com.chernyshov.exceptions.UserAlreadyExist;
import com.chernyshov.repositories.ChatUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ChatUserDataService {

    private ChatUserRepository chatUserRepository;

    public ChatUserDataService(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Transactional
    public ChatUser saveUser(ChatUser user) {
        ChatUser chatUser = chatUserRepository.findByLogin(user.getLogin());
        if (chatUser != null) {
            String message = String.format("User with login %s already exist", chatUser.getLogin());
            log.warn(message);
            throw new UserAlreadyExist(message);
        } else {
            chatUserRepository.save(user);
            log.info(String.format("User %s has been saved", user.getLogin()));
        }
        return user;
    }

    public List<ChatUser> getAll() {
        return chatUserRepository.findAll();
    }
}
