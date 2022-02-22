package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.listener.Auditable;
import com.example.studyBridge_server.domaion.type.Role;
import lombok.*;

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
    private Study study;

    @ManyToOne
    private User user;

    @Builder
    public UserAndStudy(Role role, Study study, User user) {
        this.role = role;
        this.study = study;
        this.user = user;
    }
}