package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.listener.Auditable;
import com.example.studyBridge_server.domaion.type.Gender;
import com.example.studyBridge_server.domaion.type.Role;
import com.example.studyBridge_server.support.BooleanToYNConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User extends BaseEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String loginId;

    @NonNull
    private String loginPw;

    @NonNull
    private String name;

    private String phone;

    private int coin;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean subscribe;

    private String profileImg;

    private String location;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<UserAndStudy> userAndStudyList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "mentee_id")
    @ToString.Exclude
    private List<ToDo> toDoList = new ArrayList<>();

    @Builder
    public User(@NonNull String loginId, @NonNull String loginPw, @NonNull String name, String phone, String profileImg, String location, Gender gender, Role role) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.phone = phone;
        this.coin = 0;
        this.subscribe = false;
        this.profileImg = profileImg;
        this.location = location;
        this.gender = gender;
        this.role = role;
    }
}
