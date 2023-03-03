package com.med.connect.domain.questionDomain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.med.connect.domain.User;
import com.med.connect.domain.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Questions  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String questionTitle;

    private String title;

    private String content;

    private String questionDate;

    private String questionTime;

    private String questionYear;

    private String questionMonth;

    private String questionDay;

    private String questionType;

    private String tags;

    //Total Number of Votes Up
    private String voteUp;

    //Total Number of Votes Down
    private String voteDown;


    @ManyToOne()
    @JsonBackReference
    @JoinColumn( referencedColumnName = "id" )
    private User user;

    private Long views = 0l;

    @OneToMany
    private List<QuestionViewer> questionViewer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<VotingUpAndDownInfo> votingUpAndDownInfo;

    @OneToMany(cascade=CascadeType.ALL)
    @JsonBackReference
    private List<QuestionFiles> questionFiles;

}
