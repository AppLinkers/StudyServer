package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.listener.Auditable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LikeMentor extends BaseEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menteeId;

    private Long mentorId;

    @Builder
    public LikeMentor(Long menteeId, Long mentorId) {
        this.menteeId = menteeId;
        this.mentorId = mentorId;
    }
}
