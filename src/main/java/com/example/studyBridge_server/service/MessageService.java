package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.Message;
import com.example.studyBridge_server.domaion.Room;
import com.example.studyBridge_server.domaion.type.MessageType;
import com.example.studyBridge_server.dto.message.FindRoomRes;
import com.example.studyBridge_server.repository.MessageRepository;
import com.example.studyBridge_server.repository.RoomRepository;
import com.example.studyBridge_server.repository.UserAndRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserAndRoomRepository userAndRoomRepository;

    @Transactional
    public void send(Message message) {
        String senderName = message.getSenderName();

        if (message.getMessageType().equals(MessageType.ENTER)) {

            // 입장 메시지 전달
            message.setMessage("[알림] " + senderName + " 님이 입장하였습니다.");
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getId(), message);
            messageRepository.save(message);

        } else if (message.getMessageType().equals(MessageType.TALK)) {

            // 채팅 처리
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getId(), message);
            messageRepository.save(message);

        } else {

            // 채팅방 퇴장 메시지 전달
            message.setMessage("[알림]" + senderName + " 님이 퇴장하였습니다.");
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getId(), message);
            messageRepository.save(message);

            userAndRoomRepository.deleteUserAndRoomByRoomIdAndUserId(message.getRoom().getId(), message.getSenderId());

        }

    }

    public List<Message> findByRoomId(Long room_id) {
        Optional<List<Message>> result = messageRepository.findByRoom(roomRepository.findById(room_id).get());

        if (result.isPresent()) {
            return result.get();
        } else {
            return new ArrayList<>();
        }
    }

    public FindRoomRes findRoomByStudyId(Long studyId) {
        Room room = roomRepository.findRoomByStudyId(studyId);

        return FindRoomRes.builder()
                .roomId(room.getId())
                .studyId(room.getStudy().getId())
                .build();
    }

}