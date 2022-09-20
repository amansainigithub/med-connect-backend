package com.med.connect.services.adminServices.UserService.impl;

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
    public ResponseEntity<?> getUserList(Integer page , Integer size) {
        try {
                Page<User> userList = this.userRepository.findAll(PageRequest.of(page - 1, size, Sort.by(AppConstants.SORT_BY_ID).descending()));
                return ResponseGenerator.generateSuccessResponse(userList, MessageResponse.USER_FETCH_SUCCESS);

        }
        catch (Exception e)
        {
            log.info("Error in fetching the record :::::::::::::: {}");
            log.error("Exception " , e);
            throw new NullPointerException(MessageResponse.DATA_NOT_FOUND);
        }
    }
}
