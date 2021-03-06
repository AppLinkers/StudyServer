package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.domain.Message;
import com.example.studyBridge_server.dto.message.FindRoomRes;
import com.example.studyBridge_server.dto.message.MessageReq;
import com.example.studyBridge_server.dto.message.MessageRes;
import com.example.studyBridge_server.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/message/{room_id}")
    @ResponseBody
    public List<MessageRes> messageList(@PathVariable Long room_id) {
        return messageService.findByRoomId(room_id);
    }

    @MessageMapping("/chat/message")
    public void message(MessageReq message) {
        messageService.send(message);
    }

    @GetMapping("/room")
    public ResponseEntity<FindRoomRes> getRoom(@RequestParam Long studyId) {
        return ResponseEntity.status(201).body(messageService.findRoomByStudyId(studyId));
    }
}
