package com.med.connect.controllers.publicController.questionController;

import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.domain.questionDomain.Questions;
import com.med.connect.services.publicService.questionService.QuestionService;
import com.med.connect.services.publicService.questionService.impl.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionServiceImpl questionServiceImpl;

    @PostMapping(UrlMappings.ADD_QUESTIONS)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> addQuestion( @RequestParam(value = "file" , required = false) MultipartFile multipartFile ,
                                          @RequestParam(value = "jsonNode" , required = false) String jsonNode ) throws Exception
    {
        Questions medPosts = questionService.addQuestionService(multipartFile , jsonNode);
        return ResponseEntity.ok(medPosts);
    }


    @GetMapping(UrlMappings.GET_QUESTIONS)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> getQuestions(@PathVariable int page) throws Exception
    {
//        Thread.sleep(2000);
        List<Map<Object ,Object>> questions =questionService.getQuestions(page);
        return ResponseEntity.ok(questions);
    }

    @PostMapping(UrlMappings.QUESTION_VIEWS)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> questionViews(@PathVariable Long questionId  ) throws Exception
    {
        return ResponseEntity.ok(questionServiceImpl.questionViewsService(questionId));
    }

}
