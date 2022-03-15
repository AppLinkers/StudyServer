package com.example.studyBridge_server;

import com.example.studyBridge_server.domaion.Room;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.UserAndRoom;
import com.example.studyBridge_server.repository.RoomRepository;
import com.example.studyBridge_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    void userAndRoomTest() {
        // When
        UserAndRoom userAndRoom = new UserAndRoom();

        User user = new User();
        user.setId(1L);
        user.setLoginId("test_LoginId");
        user.setLoginPw("test_LoginPw");
        user.setName("test_Name");

        userAndRoom.setUser(user);

        Room room = new Room();
        room.setId(1L);


        room.addUser(userAndRoom);
    }


}
