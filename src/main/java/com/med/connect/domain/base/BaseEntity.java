package com.med.connect.domain.base;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(nullable = false, updatable = true)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
