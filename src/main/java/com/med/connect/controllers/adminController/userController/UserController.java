package com.med.connect.controllers.adminController.userController;

import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.services.adminServices.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(UrlMappings.GET_MED_USER_LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMedUserList(@RequestParam Integer page , @RequestParam  Integer size, HttpServletRequest request)
    {
        System.out.println( "TOKEN ::::::::: {} "+request.getHeader("Authorization"));
        System.out.println( "getContextPath ::::::::: {} "+request.getContextPath());
        System.out.println( "getPathInfo ::::::::: {} "+request.getPathInfo());
        System.out.println( "getAuthType ::::::::: {} "+request.getAuthType());
        System.out.println( "getMethod ::::::::: {} "+request.getMethod());
        System.out.println( "getRemoteUser ::::::::: {} "+request.getRemoteUser());
        System.out.println( "getRequestURI ::::::::: {} "+request.getRequestURI());
        System.out.println( "getLocalAddr ::::::::: {} "+request.getLocalAddr());
        System.out.println( "getRequestURL ::::::::: {} "+request.getRequestURL());

        return userService.getMedUserList(page,size);
    }

    @PostMapping(UrlMappings.MED_USER_LOCKED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> medUserLocked(@PathVariable Long id)
    {
        return userService.medUserLocked(id);
    }





}
