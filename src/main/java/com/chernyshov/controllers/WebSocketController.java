package com.chernyshov.controllers;


import com.chernyshov.services.controllerServices.BanControllerService;
import com.chernyshov.services.controllerServices.WebSocketServiceController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.xml.validation.Validator;

@Slf4j
public class WebSocketController extends TextWebSocketHandler {

    @Autowired
    private WebSocketServiceController webSocketServiceController;

    @Autowired
    private BanControllerService banControllerService;

    @Autowired
    private Validator validator;




}
