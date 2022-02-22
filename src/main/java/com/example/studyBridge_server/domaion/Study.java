package com.example.studyBridge_server.domaion;


import com.example.studyBridge_server.domaion.listener.Auditable;
import com.example.studyBridge_server.domaion.type.StudyStatus;
import lombok.*;

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

    private String info; // 한줄 설명

    private String type; // subject

    private String place; // study 위치

    private Integer maxNum; // 최대 인원 수

    @Enumerated(value = EnumType.STRING)
    private StudyStatus status;

    @Column(name = "maker_id")
    private Long makerId;

    @Column(name = "mentor_id")
    private Long mentorId;

    @OneToMany
    @JoinColumn(name = "study_id")
    @ToString.Exclude
    private List<UserAndStudy> userAndStudyList = new ArrayList<>();

    public void addUserAndStudy(UserAndStudy userAndStudy) {
        this.userAndStudyList.add(userAndStudy);
    }

    @Builder
    public Study(String name, String info, String type, String place, Integer maxNum, StudyStatus status, Long makerId) {
        this.name = name;
        this.info = info;
        this.type = type;
        this.place = place;
        this.maxNum = maxNum;
        this.status = status;
        this.makerId = makerId;
    }
}
