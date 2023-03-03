package com.med.connect.domain.questionDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class QuestionFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileUrl;

    private String fileName;

    private String fileSize;

    private String contentType;

    private String byteSize;

    private long questionId;

    @ManyToOne
    @JoinColumn( referencedColumnName = "id" )
    @JsonIgnore
    private Questions questions;
}
