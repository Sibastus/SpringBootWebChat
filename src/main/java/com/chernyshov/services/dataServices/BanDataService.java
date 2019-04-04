package com.chernyshov.services.dataServices;


import com.chernyshov.entities.Ban;
import com.chernyshov.entities.ChatUser;
import com.chernyshov.exceptions.UserAlreadyBanedException;
import com.chernyshov.exceptions.UserAlreadyExist;
import com.chernyshov.exceptions.UserCanNotBeUnBanedException;
import com.chernyshov.exceptions.UserNotFoundException;
import com.chernyshov.repositories.BanRepository;
import com.chernyshov.repositories.ChatUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("Duplicates")
@Service
@Slf4j
public class BanDataService {

    private BanRepository banRepository;

    private ChatUserRepository chatUserRepository;

    public BanDataService(BanRepository banRepository, ChatUserRepository chatUserRepository) {
        this.banRepository = banRepository;
        this.chatUserRepository = chatUserRepository;
    }


    @Transactional
    public void addUserToBan(String login) {
        ChatUser chatUser = chatUserRepository.findByLogin(login);
        if(chatUser == null) {
            String message = String.format("User with login %s does not exist", login);
            log.warn(message);
            throw new UserNotFoundException(message);
        }
        if (chatUser.getBan() != null) {
            String message = "User already was banned";
            log.warn(message);
            throw new UserAlreadyBanedException(message);
        }
        Ban ban = new Ban();
        ban.setChatUser(chatUser);
        banRepository.save(ban);
    }


    @Transactional
    public void removeFromBan(String login) {
        ChatUser chatUser = chatUserRepository.findByLogin(login);
        if(chatUser == null) {
            String message = String.format("User with login %s does not exist", login);
            log.warn(message);
            throw new UserNotFoundException(message);
        }
        if (chatUser.getBan() == null) {
            String message = "User can not be deleted from ban";
            log.warn(message);
            throw new UserCanNotBeUnBanedException(message);
        }
        Ban ban = chatUser.getBan();
        chatUser.setBan(null);
        banRepository.delete(ban);
    }

    public boolean verifyForBan(String login) {
        ChatUser chatUser = chatUserRepository.findByLogin(login);
        if (chatUser == null) {
            String message = String.format("User with login %s does not exist", login);
            log.warn(message);
            throw new UserNotFoundException(message);
        }
        return chatUser.getBan() != null;
    }

}
