package com.med.connect.services.adminServices.UserService.impl;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.med.connect.constants.admin.AppConstants;
import com.med.connect.domain.User;
import com.med.connect.messageResp.MessageResponse;
import com.med.connect.repository.UserRepository;
import com.med.connect.services.adminServices.UserService.UserService;
import com.med.connect.utils.ResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getMedUserList(Integer page , Integer size) {
        try {
                Page<User> userList = this.userRepository.findAll(PageRequest.of(page, size , Sort.by(AppConstants.SORT_BY_ID).descending()));
                return ResponseGenerator.generateSuccessResponse(userList, MessageResponse.USER_FETCH_SUCCESS);
        }
        catch (Exception e)
        {
            log.info("Error in fetching the record :::::::::::::: {}");
            log.error("Exception " , e);
            throw new NullPointerException(MessageResponse.DATA_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> medUserLocked(Long id) {
        try {
              User user =   this.userRepository.findById(id).orElseThrow(()-> new  UserNotFoundException(MessageResponse.USER_NOT_FOUND) );
            if(user.getIsLocked())
                user.setIsLocked(Boolean.FALSE);
            else
                user.setIsLocked(Boolean.TRUE);

            this.userRepository.save(user);
            return ResponseEntity.ok(MessageResponse.SAVE_SUCCESS);
        }
        catch (Exception e)
        {
            log.error("Exception " , e);
            return ResponseEntity.badRequest().build();

        }
    }


}
