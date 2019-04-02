package com.chernyshov.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SendMessage {

    private String type;
    private String message;
    private String sender;
    private List<String> userActive;
}
