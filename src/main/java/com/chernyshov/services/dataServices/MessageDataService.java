package com.chernyshov.services.dataServices;


import com.chernyshov.entities.ChatUser;
import com.chernyshov.entities.Message;
import com.chernyshov.entities.RedisMessage;
import com.chernyshov.repositories.ChatUserRepository;
import com.chernyshov.repositories.MessageRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageDataService {

    private MessageRepository messageRepository;

    private ChatUserRepository chatUserRepository;

    private RedisTemplate<String, RedisMessage> redisMessageRepository;

    public MessageDataService(MessageRepository messageRepository, ChatUserRepository chatUserRepository, RedisTemplate<String, RedisMessage> redisMessageRepository) {
        this.messageRepository = messageRepository;
        this.chatUserRepository = chatUserRepository;
        this.redisMessageRepository = redisMessageRepository;
    }


    public List<Message> getAllPrivateMessage(String receiver) {
        return messageRepository.findByReceiverLogin(receiver);
    }

    @Transactional
    public void savePrivateMessage(String to, String from, String message) {
        ChatUser receiver = chatUserRepository.findByLogin(to);
        ChatUser sender = chatUserRepository.findByLogin(from);
        Message mess = new Message();
        mess.setBody(message);
        mess.setReceiver(receiver);
        mess.setSender(sender);
        messageRepository.save(mess);
    }

    @Transactional
    public void deleteAllPrivateMessage(String receiver) {
        messageRepository.deleteByReceiverLogin(receiver);
    }

    public void saveBroadcastMessage(String sender, String message) {
        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setMessage(message);
        redisMessage.setSender(sender);
        redisMessageRepository.opsForList().leftPush("broadcast", redisMessage);
    }

    public List<RedisMessage> getAllBroadcastMessage() {
        return redisMessageRepository
                .opsForList()
                .range("broadcast", 0, -1);
    }
}
