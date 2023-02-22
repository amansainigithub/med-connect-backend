package com.med.connect.services.publicService.postService;

import com.med.connect.domain.postDomain.MedPosts;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface PostBoxService {

    public MedPosts submitPostService(MultipartFile file , String jsonNode);

    public Page<MedPosts> getPostService(int page);
}
