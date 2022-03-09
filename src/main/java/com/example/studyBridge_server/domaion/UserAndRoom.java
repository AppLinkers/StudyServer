package com.example.studyBridge_server.domaion;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class UserAndRoom {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public UserAndRoom(User user, Room room) {
        this.user = user;
        this.room = room;
    }
}
