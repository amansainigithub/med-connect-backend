package com.med.connect.controllers.publicController.profilePictureController;

import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.services.adminServices.UserService.UserService;
import com.med.connect.services.publicService.profilePicService.ProfilePicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class ProfilePictureController {

    @Autowired
    private ProfilePicService profilePicService;


    @PostMapping(UrlMappings.UPLOAD_PROFILE_PICTURE)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("email") String email )
    {
        return profilePicService.uploadProfilePicture(file,email);
    }

}
