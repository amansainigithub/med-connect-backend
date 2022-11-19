package com.med.connect.controllers.publicController.postController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.domain.postDomain.MedPosts;
import com.med.connect.services.publicService.postService.PostBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class PostBoxController {

    @Autowired
    private PostBoxService postBoxService;

    @PostMapping(UrlMappings.SUBMIT_POST)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> submitPost( @RequestParam("file") MultipartFile multipartFile, @RequestParam("jsonNode") String jsonNode ) throws Exception
    {

       Map<Object,Object> mapper =  new ObjectMapper().readValue(jsonNode , LinkedHashMap.class);
        System.out.println(mapper);
        System.out.println("============");
        mapper.forEach((k,v)->{
            System.out.println(k);
            System.out.println(v);
        });

       MedPosts medPosts = postBoxService.submitPostService(multipartFile , jsonNode);


        return ResponseEntity.ok(medPosts);
    }
}
