package com.med.connect.services.adminServices.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface UserService {

    public ResponseEntity<?> getMedUserList(Integer page , Integer size);

    ResponseEntity<?> medUserLocked(Long id);


}
