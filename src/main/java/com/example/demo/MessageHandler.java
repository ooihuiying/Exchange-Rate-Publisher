package com.example.demo;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class MessageHandler implements Serializable {

    private transient SimpMessagingTemplate messagingTemplate;

    public MessageHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // TODO: Let user define topic
    public void sendMessage(String value) {
        messagingTemplate.convertAndSend("/topic/rates", "{\"content\": " + "\"" + value + "\"" + "}");
    }
}

