package com.med.connect.services.adminServices.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserService {

    public ResponseEntity<?> getMedUserList(Integer page , Integer size);
}
