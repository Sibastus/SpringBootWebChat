package com.chernyshov.services.dataServices;


import com.chernyshov.entities.ChatUser;
import com.chernyshov.repositories.ChatUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ChatUserDataServices {

    @Autowired
    private ChatUserRepository chatUserRepository;



    @Transactional
    public ChatUser saveUser(ChatUser user) {
        ChatUser chatUser = chatUserRepository.findByLogin(user.getLogin());
        if (chatUser != null) {
            log.warn(String.format("User with login %s already exist", chatUser.getLogin()));

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
