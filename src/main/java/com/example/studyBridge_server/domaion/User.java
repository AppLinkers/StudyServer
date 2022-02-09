package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.listener.Auditable;
import com.example.studyBridge_server.support.BooleanToYNConverter;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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

    private int coin = 0;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean subscribe = false;

    private String profileImg;

    private String location;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private Role role;
}
