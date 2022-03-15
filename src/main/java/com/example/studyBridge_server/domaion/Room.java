package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.listener.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Room extends BaseEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @OneToOne
    private Study study;

    public Room(@Nullable Study study) {
        this.study = study;
    }

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @JsonIgnore
    private List<UserAndRoom> userAndRoomList = new ArrayList<>();


    public void deleteUser(UserAndRoom userAndRoom) {
        this.userAndRoomList.remove(userAndRoom);
    }

    public void addUser(UserAndRoom userAndRoom) {
        this.userAndRoomList.add(userAndRoom);
    }
}