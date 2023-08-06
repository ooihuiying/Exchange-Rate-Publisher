package com.example.demo;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/*
 * To send message to Websocket
 * Note that the topic here is not referring to kafka
 */
@Component
public class MessageHandler implements Serializable {

    private transient SimpMessagingTemplate messagingTemplate;

    public MessageHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String topicName, String value) {
        String payload = "{\"content\": " + value + "}";
        messagingTemplate.convertAndSend("/topic/" + topicName, payload);
    }
}

