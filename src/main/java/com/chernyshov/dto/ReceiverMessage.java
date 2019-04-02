package com.chernyshov.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceiverMessage {

    private String type;

    private String message;

    private String receiver;

}
