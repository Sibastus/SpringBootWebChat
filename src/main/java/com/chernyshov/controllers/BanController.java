package com.chernyshov.controllers;

import com.chernyshov.dto.ChatUserDto;
import com.chernyshov.services.controllerServices.BanControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class BanController {

    private BanControllerService banControllerService;

    public BanController(BanControllerService banControllerService) {
        this.banControllerService = banControllerService;
    }

    @CrossOrigin
    @PostMapping(value = "/ban")
    public void addUserToBan(@RequestBody ChatUserDto chatUserDto) {
        log.debug(String.format("Call ban endpoint for login %s", chatUserDto.getLogin()));
        banControllerService.addUserToBan(chatUserDto);
    }

    @CrossOrigin
    @DeleteMapping(value = "/ban/{login}")
    public void removeFromBan(@PathVariable("login") String login) {
        banControllerService.removeUserFromBan(login);
    }
}
