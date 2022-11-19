package com.med.connect.services.publicService.postService.impl;

import com.med.connect.bucket.bucketModels.BucketModel;
import com.med.connect.bucket.bucketService.BucketService;
import com.med.connect.domain.postDomain.MedPosts;
import com.med.connect.repository.UserRepository;
import com.med.connect.repository.postRepo.PostRepository;
import com.med.connect.services.publicService.postService.PostBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PostBoxServiceImpl implements PostBoxService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserRepository userRepository;

    public MedPosts submitPostService(MultipartFile file , String jsonNode)
    {
        try {

            //SAVE IMAGE TO S3-BUCKET
            BucketModel bucketModel =  bucketService.uploadFile(file);

            MedPosts medPosts = new MedPosts();
            medPosts.setFileUrl(bucketModel.getBucketUrl());
            medPosts.setFileName(file.getOriginalFilename());
            medPosts.setFileSize(String.valueOf(file.getSize()));
            medPosts.setContentType(file.getContentType());
            medPosts.setByteSize(String.valueOf(file.getBytes()));
            medPosts.setCurrentPostDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            medPosts.setCurrentPostTime(String.valueOf(java.time.LocalTime.now()));

            MedPosts res = this.postRepository.save(medPosts);
            return res;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }



}
