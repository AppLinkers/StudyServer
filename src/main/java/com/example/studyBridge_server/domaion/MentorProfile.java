package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.listener.Auditable;
import com.example.studyBridge_server.domaion.type.Subject;
import com.example.studyBridge_server.support.StringArrayConverter;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

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

    //    @Nullable
//    @Enumerated(value = EnumType.STRING)
//    private Subject subject;
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
