package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domain.Message;
import com.example.studyBridge_server.domain.Room;
import com.example.studyBridge_server.domain.User;
import com.example.studyBridge_server.domain.type.MessageType;
import com.example.studyBridge_server.dto.message.FindRoomRes;
import com.example.studyBridge_server.dto.message.MessageReq;
import com.example.studyBridge_server.dto.message.MessageRes;
import com.example.studyBridge_server.repository.MessageRepository;
import com.example.studyBridge_server.repository.RoomRepository;
import com.example.studyBridge_server.repository.UserAndRoomRepository;
import com.example.studyBridge_server.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public void send(MessageReq messageReq) {

        User user = userRepository.findById(messageReq.getUserId()).get();
        Room room = roomRepository.findById(messageReq.getRoomId()).get();

        Message message = new Message();

        message.setMessageType(messageReq.getMessageType());
        message.setRoom(room);
        message.setUser(user);

        if (messageReq.getMessageType().equals(MessageType.ENTER)) {

            // 입장 메시지 전달
            message.setMessage("[알림] " + user.getName() + " 님이 입장하였습니다.");
            messageRepository.save(message);

            MessageRes messageRes = MessageToMessageRes(message);

            messagingTemplate.convertAndSend("/sub/chat/room/" + messageRes.getRoomId(), messageRes);

        } else if (message.getMessageType().equals(MessageType.TALK) || message.getMessageType().equals(MessageType.PHOTO)) {

            message.setMessage(messageReq.getMessage());
            // 채팅 처리
            messageRepository.save(message);

            MessageRes messageRes = MessageToMessageRes(message);

            messagingTemplate.convertAndSend("/sub/chat/room/" + messageRes.getRoomId(), messageRes);

        } else if(message.getMessageType().equals(MessageType.EXIT)) {

            // 채팅방 퇴장 메시지 전달
            message.setMessage("[알림]" + user.getName() + " 님이 퇴장하였습니다.");
            messageRepository.save(message);

            MessageRes messageRes = MessageToMessageRes(message);

            messagingTemplate.convertAndSend("/sub/chat/room/" + messageRes.getRoomId(), messageRes);

            userAndRoomRepository.deleteUserAndRoomByRoomIdAndUserId(message.getRoom().getId(), message.getUser().getId());

        }

    }

    private MessageRes MessageToMessageRes(Message message) {
        return MessageRes.builder()
                .messageType(message.getMessageType())
                .roomId(message.getRoom().getId())
                .userId(message.getUser().getId())
                .userName(message.getUser().getName())
                .userProfileImg(message.getUser().getProfileImg())
                .message(message.getMessage())
                .build();
    }

    public List<MessageRes> findByRoomId(Long room_id) {
        List<MessageRes> result = new ArrayList<>();
        Optional<List<Message>> messageList = messageRepository.findByRoom(roomRepository.findById(room_id).get());

        if (messageList.isPresent()) {
            messageList.get().forEach(
                    message -> {
                        MessageRes messageRes = MessageRes.builder()
                                .messageType(message.getMessageType())
                                .roomId(message.getRoom().getId())
                                .userId(message.getUser().getId())
                                .userName(message.getUser().getName())
                                .userProfileImg(message.getUser().getProfileImg())
                                .message(message.getMessage())
                                .build();

                        result.add(messageRes);
                    }
            );

            return result;
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