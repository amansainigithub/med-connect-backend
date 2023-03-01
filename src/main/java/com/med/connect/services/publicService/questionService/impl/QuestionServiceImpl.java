package com.med.connect.services.publicService.questionService.impl;

import com.med.connect.domain.questionDomain.Questions;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Component
public interface QuestionServiceImpl {
    Questions addQuestionService(MultipartFile multipartFile, String jsonNode);

    List<Map<Object ,Object>> getQuestions(int page);
}
