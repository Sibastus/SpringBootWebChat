package com.chernyshov.services.dataServices;


import com.chernyshov.entities.Ban;
import com.chernyshov.entities.ChatUser;
import com.chernyshov.repositories.BanRepository;
import com.chernyshov.repositories.ChatUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
            //exception ///TODO exception
        }
        if (chatUser.getBan() != null) {
            //exeption
        }
        Ban ban = new Ban();
        ban.setChatUser(chatUser);
        banRepository.save(ban);
    }


    @Transactional
    public void removeFromBan(String login) {
        ChatUser chatUser = chatUserRepository.findByLogin(login);
        if(chatUser == null) {
            //exception
        }
        if (chatUser.getBan() == null) {
            //exception
        }
        Ban ban = chatUser.getBan();
        chatUser.setBan(null);
        banRepository.delete(ban);
    }

    public boolean verifyForBan(String login) {
        ChatUser chatUser = chatUserRepository.findByLogin(login);
        if (chatUser == null) {
            //exception
        }
        return chatUser.getBan() != null;
    }

}
