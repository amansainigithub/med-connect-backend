package com.med.connect.domain.questionDomain;

import com.med.connect.domain.base.BaseEntity;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class VotingUpAndDownInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userId;

    private String questionId;

    private String vote; //Up [U] and Down [D]

    private Boolean isVoter = Boolean.FALSE;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(nullable = false, updatable = true)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
