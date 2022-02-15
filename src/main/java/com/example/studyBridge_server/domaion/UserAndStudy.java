package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.listener.Auditable;
import com.example.studyBridge_server.domaion.type.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
}