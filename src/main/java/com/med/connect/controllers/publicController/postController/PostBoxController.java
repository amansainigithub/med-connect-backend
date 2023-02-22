package com.med.connect.controllers.publicController.postController;

import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.domain.postDomain.MedPosts;
import com.med.connect.repository.postRepo.PostRepository;
import com.med.connect.services.publicService.postService.PostBoxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class PostBoxController {

    @Autowired
    private PostBoxService postBoxService;

    @Autowired
    private PostRepository postRepository;
    Logger logger = LoggerFactory.getLogger(PostBoxController.class);

    @PostMapping(UrlMappings.SUBMIT_POST)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> submitPost( @RequestParam(value = "file" , required = false) MultipartFile multipartFile ,
                                         @RequestParam(value = "jsonNode" , required = false) String jsonNode ) throws Exception
    {
       MedPosts medPosts = postBoxService.submitPostService(multipartFile , jsonNode);
       return ResponseEntity.ok(medPosts);
    }

    @GetMapping(UrlMappings.GET_POSTS)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> getPosts(@PathVariable int page) throws Exception
    {
        Page<MedPosts> medPostsList =postBoxService.getPostService(page);
        return ResponseEntity.ok(medPostsList);
    }

    @GetMapping("getAllPosts")
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> getAllPosts() throws Exception
    {
        return ResponseEntity.ok(postRepository.findAll());
    }
}
