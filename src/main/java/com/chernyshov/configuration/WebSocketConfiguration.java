package com.chernyshov.configuration;


import com.chernyshov.controllers.WebSocketController;
import com.chernyshov.interceptors.WebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketController(), "/socket")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketInterceptor())
                .withSockJS();
    }


    @Bean
    public WebSocketController getWebSocketController() {
        return new WebSocketController();
    }
}
