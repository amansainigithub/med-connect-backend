package com.med.connect.dto.adminDto.posts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedPostDTO {

    private long id;

    private String content;

    private long userId;
}
