package com.med.connect.services.publicService.questionService.impl;

import com.med.connect.domain.postDomain.MedPosts;
import com.med.connect.domain.questionDomain.Questions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface QuestionServiceImpl {
    Questions addQuestionService(MultipartFile multipartFile, String jsonNode);

    Page<Questions> getQuestions(int page);
}
