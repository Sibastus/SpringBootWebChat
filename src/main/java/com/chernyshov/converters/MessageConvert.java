package com.chernyshov.converters;


import com.chernyshov.dto.SendMessage;
import com.chernyshov.entities.Message;
import com.chernyshov.entities.RedisMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageConvert {

    public SendMessage convertToDto(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setType("PRIVATE");
        sendMessage.setMessage(message.getBody());
        sendMessage.setSender(message.getSender().getLogin());
        return sendMessage;
    }

    public SendMessage convertToDtoFromRedisMessage(RedisMessage redisMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setType("BROADCAST");
        sendMessage.setSender(redisMessage.getSender());
        sendMessage.setMessage(redisMessage.getMessage());
        return sendMessage;
    }

    public List<SendMessage> convertListPrivateMessageToSendMessage(List<Message> messageList) {
        return messageList.stream()
                .map(entity -> convertToDto(entity))
                .collect(Collectors.toList());
    }

    public List<SendMessage> convertListBroadcastMessageToSendMessage(List<RedisMessage> redisMessages) {
        return redisMessages.stream()
                .map(entity -> convertToDtoFromRedisMessage(entity))
                .collect(Collectors.toList());
    }

}
