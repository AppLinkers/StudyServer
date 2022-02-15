package com.example.studyBridge_server.domaion;


import com.example.studyBridge_server.domaion.listener.Auditable;
import com.example.studyBridge_server.domaion.type.StudyStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Study extends BaseEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Enumerated(value = EnumType.STRING)
    private StudyStatus status;

    @Column(name = "maker_id")
    private Long makerId;

    @Column(name = "mentor_id")
    private long mentorId;

    @OneToMany
    @JoinColumn(name = "study_id")
    @ToString.Exclude
    private List<UserAndStudy> userAndStudyList = new ArrayList<>();

}
