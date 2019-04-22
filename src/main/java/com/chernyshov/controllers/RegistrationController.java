package com.chernyshov.controllers;

import com.chernyshov.dto.ChatUserDto;
import com.chernyshov.services.controllerServices.RegistrationServices;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
public class RegistrationController {

    private RegistrationServices registrationServices;

    public RegistrationController(RegistrationServices registrationServices) {
        this.registrationServices = registrationServices;
    }

    @PostMapping(value = "/registration")
    public void registration(@Valid @RequestBody ChatUserDto chatUserDto) {
        registrationServices.saveUser(chatUserDto);
    }

    @GetMapping(value = "/users")
    public List<ChatUserDto> getAllUsers() {
        return registrationServices.getAllUser();
    }
}
