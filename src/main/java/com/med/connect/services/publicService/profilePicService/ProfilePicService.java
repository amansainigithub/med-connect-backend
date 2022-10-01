package com.med.connect.services.publicService.profilePicService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface ProfilePicService {
    ResponseEntity<?> uploadProfilePicture(MultipartFile file, String email);
}
