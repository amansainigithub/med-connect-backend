package com.med.connect.domain.questionDomain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.med.connect.domain.User;
import com.med.connect.domain.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
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

    private String fileUrl;

    private String fileName;

    private String fileSize;

    private String contentType;

    private String byteSize;

    private String questionDate;

    private String questionTime;

    private String questionYear;

    private String questionMonth;

    private String questionDay;

    private String questionType;

    private String tags;

    @ManyToOne()
    @JsonBackReference
    @JoinColumn( referencedColumnName = "id" )
    private User user;

    private String views;

    @OneToMany
    private List<QuestionViewer> questionViewer;
}
