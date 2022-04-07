package com.example.studyBridge_server.domain;

import com.example.studyBridge_server.domain.listener.Auditable;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MentorProfile extends BaseEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    private String info;

    private String nickName;

    private String subject;

    private String school;

    private String schoolImg;

    private String experience;

    private String curriculum;

    private String appeal;

    @Builder(builderClassName = "initialize", builderMethodName = "initialize")
    public MentorProfile(User user) {
        this.user = user;
    }
}
