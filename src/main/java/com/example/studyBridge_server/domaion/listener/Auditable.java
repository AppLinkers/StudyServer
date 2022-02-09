package com.example.studyBridge_server.domaion.listener;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public interface Auditable {

    LocalDateTime getCreatedAt();

    void setCreatedAt(LocalDateTime createdAt);

    LocalDateTime getUpdatedAt();

    void setUpdatedAt(LocalDateTime updatedAt);

}
