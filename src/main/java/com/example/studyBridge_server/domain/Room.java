package com.example.studyBridge_server.domain;

import com.example.studyBridge_server.domain.listener.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Study study;

    public Room(@Nullable Study study) {
        this.study = study;
    }

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<UserAndRoom> userAndRoomList = new ArrayList<>();

}