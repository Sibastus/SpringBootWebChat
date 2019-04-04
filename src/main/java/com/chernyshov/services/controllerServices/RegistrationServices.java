package com.chernyshov.services.controllerServices;


import com.chernyshov.converters.ChatUserConvert;
import com.chernyshov.dto.ChatUserDto;
import com.chernyshov.entities.ChatUser;
import com.chernyshov.services.dataServices.ChatUserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RegistrationServices {

    private ChatUserDataService chatUserDataService;

    private ApplicationEventPublisher applicationEventPublisher;

    private ChatUserConvert chatUserConvert;

    public RegistrationServices(ChatUserDataService chatUserDataService, ApplicationEventPublisher applicationEventPublisher, ChatUserConvert chatUserConvert) {
        this.chatUserDataService = chatUserDataService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.chatUserConvert = chatUserConvert;
    }


    public void saveUser(ChatUserDto chatUserDto) {//TODO refactor
        ChatUser chatUser = chatUserDataService.saveUser(chatUserConvert.cinvertToEntity(chatUserDto));
        ChatUserDto newChatUserDto = chatUserConvert.convertToDto(chatUser);
        newChatUserDto.setPassword(chatUserDto.getPassword());
        applicationEventPublisher.publishEvent(newChatUserDto);
        log.trace(String.format("Event for user with login %s was generated", newChatUserDto.getLogin()));
    }

    public List<ChatUserDto> getAllUser(){
        return chatUserDataService.getAll().stream()
                .map(entity -> chatUserConvert.convertToDto(entity))
                .collect(Collectors.toList());
    }


}
