package com.example.studyBridge_server.domain;

import com.example.studyBridge_server.domain.listener.Auditable;
import com.example.studyBridge_server.domain.type.Role;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserAndStudy extends BaseEntity implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToOne
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Study study;

    @ManyToOne
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public UserAndStudy(Role role, Study study, User user) {
        this.role = role;
        this.study = study;
        this.user = user;
    }
}