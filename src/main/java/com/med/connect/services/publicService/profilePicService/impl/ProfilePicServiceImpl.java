package com.med.connect.services.publicService.profilePicService.impl;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.med.connect.bucket.bucketModels.BucketModel;
import com.med.connect.bucket.bucketService.BucketService;
import com.med.connect.domain.User;
import com.med.connect.helper.Validator;
import com.med.connect.messageResp.MessageResponse;
import com.med.connect.repository.UserRepository;
import com.med.connect.services.publicService.profilePicService.ProfilePicService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ProfilePicServiceImpl implements ProfilePicService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BucketService bucketService;

    @Override
    public ResponseEntity<?> uploadProfilePicture(MultipartFile file, String email) {
       try {
           //####  Validate Email ######
          if( Validator.emailValidator(email))
          {
           User user =  userRepository.findByEmail(
                            email).orElseThrow(
                           ()-> new UserNotFoundException(MessageResponse.USER_NOT_FOUND));

           BucketModel bucketModel = bucketService.uploadFile(file);
           user.setProfilePicUrl(bucketModel.getBucketUrl());
           user.setProfilePicName(file.getOriginalFilename());
           user.setProfilePicSize(String.valueOf(file.getSize()));
           user.setProfilePicContentType(file.getContentType());
           user.setProfilePicBytes(file.getBytes().toString());
           userRepository.save(user) ;

           log.info("Profile Pic upload Successfully ::::::::::::::::: {} SUCCESS");
           return ResponseEntity.ok(user);
          }
       }
       catch (Exception e)
       {
           e.printStackTrace();
           log.info("Exception :::::::::::::::::::::::::: {}" , e.getMessage());
       }
        return ResponseEntity.badRequest().build();
    }
}
