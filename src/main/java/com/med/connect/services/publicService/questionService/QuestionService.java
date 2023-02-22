package com.med.connect.services.publicService.questionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.connect.bucket.bucketModels.BucketModel;
import com.med.connect.bucket.bucketService.BucketService;
import com.med.connect.domain.User;
import com.med.connect.domain.postDomain.MedPosts;
import com.med.connect.domain.questionDomain.Questions;
import com.med.connect.repository.UserRepository;
import com.med.connect.repository.questionRepo.QuestionRepo;
import com.med.connect.services.publicService.questionService.impl.QuestionServiceImpl;
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
public class QuestionService implements QuestionServiceImpl {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Questions addQuestionService(MultipartFile file, String jsonNode) {
        try {
            Map<Object,Object> mapper =  new ObjectMapper().readValue(jsonNode , LinkedHashMap.class);
            mapper.forEach((k,v)->{
                log.info("Key mapper :: " + k);
                log.info("Value mapper :: " + v);
            });

            User user = this.userRepository.findById(Long.parseLong(String.valueOf(mapper.get("userId")))).get();

            //Creating Med Post Object and Set Data To Object
            Questions questions = new Questions();

            //Checking Post Type
            checkingPostType(file, String.valueOf(mapper.get("content")), questions);

            //Check if File is Empty
            if(file != null)
            {
                log.info("File :: {} " + file);
                //SAVE IMAGE TO S3-BUCKET
                BucketModel bucketModel =  bucketService.uploadFile(file);

                questions.setByteSize(String.valueOf(file.getBytes()));
                questions.setFileUrl(bucketModel.getBucketUrl());
                questions.setFileName(file.getOriginalFilename());
                questions.setFileSize(String.valueOf(file.getSize()));
                questions.setContentType(file.getContentType());

//                double kilobytes = (file.getBytes().length / 1024);
//                System.out.println("KB :: " + kilobytes);
            }

            questions.setUser(user);
            questions.setTitle(String.valueOf(mapper.get("title")));
            questions.setContent(String.valueOf(mapper.get("content")));

            LocalDate currentDate = LocalDate.now();
            int currentDay = currentDate.getDayOfMonth();
            questions.setQuestionDay(String.valueOf(currentDay));

            Month currentMonth = currentDate.getMonth();
            questions.setQuestionMonth(String.valueOf(currentMonth));

            int currentYear = currentDate.getYear();
            questions.setQuestionYear(String.valueOf(currentYear));

            questions.setQuestionDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            questions.setQuestionTime(String.valueOf(java.time.LocalTime.now()));

            Questions response = this.questionRepo.save(questions);
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Page<Questions> getQuestions(int page) {
        Page<Questions> questions = null;
        try {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
            questions =  this.questionRepo.findAll(pageable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error("Something Went Wrong No Data Found Here !!!");
        }
        return questions;
    }

    private void checkingPostType(MultipartFile multipartFile , String content, Questions questions)
    {
        //Only Image Conditions
        if(multipartFile == null)
        {
            //Only Content Conditions
            questions.setQuestionType("C");
        } else if (content.isEmpty() || content == null || content == "null") {
            //Only Image
            questions.setQuestionType("I");
        }
        else {
            //Content + Image Conditions
            questions.setQuestionType("C + I");
        }
    }
}
