package com.med.connect.services.publicService.postService.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.connect.bucket.bucketModels.BucketModel;
import com.med.connect.bucket.bucketService.BucketService;
import com.med.connect.domain.User;
import com.med.connect.domain.postDomain.MedPosts;
import com.med.connect.repository.UserRepository;
import com.med.connect.repository.postRepo.PostRepository;
import com.med.connect.services.publicService.postService.PostBoxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
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
            Map<Object,Object> mapper =  new ObjectMapper().readValue(jsonNode , LinkedHashMap.class);
            mapper.forEach((k,v)->{
                log.info("Key mapper :: " + k);
                log.info("Value mapper :: " + v);
            });

            User user = this.userRepository.findById(Long.parseLong(String.valueOf(mapper.get("userId")))).get();

            //Creating Med Post Object and Set Data To Object
            MedPosts medPosts = new MedPosts();

            //Checking Post Type
            checkingPostType(file, String.valueOf(mapper.get("content")), medPosts);

            //Check if File is Empty
            if(file != null)
            {
                log.info("File :: {} " + file);
                //SAVE IMAGE TO S3-BUCKET
                BucketModel bucketModel =  bucketService.uploadFile(file);

                medPosts.setByteSize(String.valueOf(file.getBytes()));
                medPosts.setFileUrl(bucketModel.getBucketUrl());
                medPosts.setFileName(file.getOriginalFilename());
                medPosts.setFileSize(String.valueOf(file.getSize()));
                medPosts.setContentType(file.getContentType());

//                double kilobytes = (file.getBytes().length / 1024);
//                System.out.println("KB :: " + kilobytes);
            }

            medPosts.setUser(user);
            medPosts.setContent(String.valueOf(mapper.get("content")));

            LocalDate currentDate = LocalDate.now();
            int currentDay = currentDate.getDayOfMonth();
            medPosts.setCurrentPostDay(String.valueOf(currentDay));

            Month currentMonth = currentDate.getMonth();
            medPosts.setCurrentPostMonth(String.valueOf(currentMonth));

            int currentYear = currentDate.getYear();
            medPosts.setCurrentPostYear(String.valueOf(currentYear));

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

    @Override
    public Page<MedPosts> getPostService(int page) {
        Page<MedPosts> medPostsList = null;
       try {
           Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
           medPostsList =  this.postRepository.findAll(pageable);
       }
       catch (Exception e)
       {
            e.printStackTrace();
            log.info("Something Went Wrong No Data Found Here !!!");
       }
       return medPostsList;
    }

    private void checkingPostType(MultipartFile multipartFile , String content, MedPosts medPosts)
    {
        //Only Image Conditions
       if(multipartFile == null)
       {
           //Only Content Conditions
           medPosts.setPostType("C");
       } else if (content.isEmpty() || content == null || content == "null") {
           //Only Image
           medPosts.setPostType("I");
       }
       else {
           //Content + Image Conditions
           medPosts.setPostType("C + I");
       }
    }



}
