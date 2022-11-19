package com.med.connect.services.publicService.postService;

import com.med.connect.domain.postDomain.MedPosts;
import com.med.connect.dto.adminDto.posts.MedPostDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface PostBoxService {

    public MedPosts submitPostService(MultipartFile file , String jsonNode);
}
