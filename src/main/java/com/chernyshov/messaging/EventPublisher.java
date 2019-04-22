package com.chernyshov.messaging;


import com.chernyshov.dto.ChatUserDto;
import com.chernyshov.dto.Payload;
import com.chernyshov.exceptions.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.cloud.stream.annotation.EnableBinding;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableBinding
@Slf4j
public class EventPublisher {

    private BinderAwareChannelResolver resolver;

    public EventPublisher(BinderAwareChannelResolver resolver) {
        this.resolver = resolver;
    }

    @EventListener
    public void publishObject(ChatUserDto chatUserDto) {
        publish(chatUserDto);
    }

    private void publish(ChatUserDto chatUserDto) {
        Payload payload = new Payload();
        payload.setChatUserDto(chatUserDto);
        payload.setEvent("CREATE");

        Map<String, String> headers = new HashMap<>();
        headers.put("EventVersion", "v1");
        headers.put("EntityVersion", "v2");

        Message<Payload> message = MessageBuilder
                .withPayload(payload)
                .copyHeaders(headers)
                .build();

        MessageChannel channel = resolver.resolveDestination("user-event-output");
        if(!channel.send(message)) {
            String errorMessage = "Message can not send to keycloak";
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }

    }
}
