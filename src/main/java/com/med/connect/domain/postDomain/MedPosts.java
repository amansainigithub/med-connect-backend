package com.med.connect.domain.postDomain;

import com.med.connect.domain.User;
import com.med.connect.domain.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MedPosts  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String content;

    private String fileUrl;

    private String fileName;

    private String fileSize;

    private String contentType;

    private String byteSize;

    private String currentPostDate;

    private String currentPostTime;

    @ManyToOne
    @JoinColumn( referencedColumnName = "id")
    private User user;
}
