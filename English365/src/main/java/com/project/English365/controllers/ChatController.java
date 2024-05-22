package com.project.English365.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import com.project.English365.entities.ChatMessage;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {


    @MessageMapping("/message")
    @SendTo("/topic/return-to")
    public ChatMessage getContent(@RequestBody ChatMessage message) {

//        try {
//            //processing
//            Thread.sleep(500);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return message;
    }

}