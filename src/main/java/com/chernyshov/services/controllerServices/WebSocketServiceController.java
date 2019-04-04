package com.chernyshov.services.controllerServices;


import com.chernyshov.converters.MessageConvert;
import com.chernyshov.dto.SendMessage;
import com.chernyshov.entities.Message;
import com.chernyshov.entities.RedisMessage;
import com.chernyshov.services.dataServices.MessageDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketServiceController {

    private MessageDataService messageDataService;

    private MessageConvert messageConvert;

    public WebSocketServiceController(MessageDataService messageDataService, MessageConvert messageConvert) {
        this.messageDataService = messageDataService;
        this.messageConvert = messageConvert;
    }

    public List<SendMessage> getAllMessage(String receiver) {
        List<Message> messages = messageDataService.getAllPrivateMessage(receiver);
        List<SendMessage> privateMessages = messageConvert.convertListPrivateMessageToSendMessage(messages);
        List<RedisMessage> broadcastMessages = messageDataService.getAllBroadcastMessage();
        List<SendMessage> broadMessages = messageConvert.convertListBroadcastMessageToSendMessage(broadcastMessages);
        privateMessages.addAll(broadMessages);
        return privateMessages;
    }

    //delete messages from postgres
    public void deleteAllPrivateMessages(String receiver) {
        messageDataService.deleteAllPrivateMessage(receiver);
    }

    //save to redis
    public void saveBroadcastMessage(SendMessage sendMessage) {
        messageDataService.saveBroadcastMessage(sendMessage.getSender(), sendMessage.getMessage());
    }

    //save to postgres
    public void savePrivateMessage(String to, SendMessage sendMessage) {
        messageDataService.savePrivateMessage(to, sendMessage.getSender(), sendMessage.getMessage());
    }


}
