package com.med.connect.domain.questionDomain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class QuestionViewer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String userId;

//    @ManyToOne
//    private Questions questions;

}
