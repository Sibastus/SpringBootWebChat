package com.chernyshov.controllers;


import com.chernyshov.dto.ReceiverMessage;
import com.chernyshov.dto.SendMessage;
import com.chernyshov.services.controllerServices.BanControllerService;
import com.chernyshov.services.controllerServices.WebSocketServiceController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class WebSocketController extends TextWebSocketHandler {

    @Autowired
    private WebSocketServiceController webSocketServiceController;

    @Autowired
    private BanControllerService banControllerService;

    @Autowired
    private Validator validator;

    private Map<String, WebSocketSession> activeUsers = new ConcurrentHashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (banControllerService.verifyForBan((String)session.getAttributes().get("login"))) {
            SendMessage sendMessage = createSendMessage("system", "You are banned", "PRIVATE");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(sendMessage)));
            return;
        }
        activeUsers.put((String)session.getAttributes().get("login"), session);
        List<SendMessage> messageList = webSocketServiceController.getAllMessage((String) session.getAttributes().get("login"));
        messageList
                .stream()
                .peek(messages -> sendPrivateMessage((String)session.getAttributes().get("login"), messages)).count();
        webSocketServiceController.deleteAllPrivateMessages((String) session.getAttributes().get("login"));
        sendAllChangeActiveList();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(banControllerService.verifyForBan((String)session.getAttributes().get("login"))){
            session.close();
            return;
        }
        String jsonString = message.getPayload();
        ReceiverMessage receiverMessage = mapper.readValue(jsonString, ReceiverMessage.class);
        Set<ConstraintViolation<ReceiverMessage>> violations = validator.validate(receiverMessage, ReceiverMessage.class);

        //verify if there are no any errors.
        if (!violations.isEmpty()) {
            //send error message
            String errorMessage = getErrorMessage(violations);
            SendMessage sendMessage = createSendMessage((String) session.getAttributes().get("login"), errorMessage, "ERROR");
            sendPrivateMessage((String) session.getAttributes().get("login"), sendMessage);
            return;
        }
        switch (receiverMessage.getType()) {
            case "BROADCAST": {
                SendMessage sendMessage = createSendMessage((String)session.getAttributes().get("login"), receiverMessage.getMessage(), "BROADCAST");
                this.sendAll(sendMessage);
                webSocketServiceController.saveBroadcastMessage(sendMessage);
                break;
            }
            case "PRIVATE": {
                SendMessage sendMessage = createSendMessage((String)session.getAttributes().get("login"), receiverMessage.getMessage(), "PRIVATE");
                if (isActiveUser(receiverMessage.getReceiver())) {
                    sendPrivateMessage(receiverMessage.getReceiver(), sendMessage);
                } else {
                    webSocketServiceController.savePrivateMessage(receiverMessage.getReceiver(), sendMessage);
                }
                break;
            }
            case "LOGOUT": {
                SendMessage sendMessage = createSendMessage((String)session.getAttributes().get("login"), "", "LOGOUT");
                sendPrivateMessage((String) session.getAttributes().get("login"), sendMessage);
                removeFromActiveUser((String) session.getAttributes().get("login"));
                sendAllChangeActiveList();
                break;
            }
        }

    }

    private SendMessage createSendMessage(String from, String message, String type) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setType(type);
        sendMessage.setMessage(message);
        sendMessage.setSender(from);
        sendMessage.setUserActive(null);
        return sendMessage;
    }

    private void sendPrivateMessage(String to, SendMessage sendMessage) {
        try {
            WebSocketSession session = activeUsers.get(to);
            session.sendMessage(new TextMessage(mapper.writeValueAsString(sendMessage)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendAllChangeActiveList() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setType("LIST");
        sendMessage.setUserActive(new ArrayList<>(activeUsers.keySet()));
        this.sendAll(sendMessage);
    }

    private void sendAll(SendMessage sendMessage){
        try {
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(sendMessage));
            for (Map.Entry<String, WebSocketSession> item: activeUsers.entrySet()) {
                item.getValue().sendMessage(textMessage);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw  new RuntimeException(e);
        }
    }

    private String getErrorMessage(Set<ConstraintViolation<ReceiverMessage>> violations) {
        return violations.iterator().next().getMessage();
    }

    private boolean isActiveUser(String user) {
        return activeUsers.keySet().contains(user);
    }

    private void removeFromActiveUser(String user) {
        activeUsers.remove(user);
    }

}
