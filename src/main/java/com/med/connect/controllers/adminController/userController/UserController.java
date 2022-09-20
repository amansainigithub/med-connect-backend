package com.med.connect.controllers.adminController.userController;

import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.services.adminServices.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(UrlMappings.GET_USER_LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserList(@RequestParam Integer page , @RequestParam  Integer size)
    {
        return userService.getUserList(page,size);
    }




}
