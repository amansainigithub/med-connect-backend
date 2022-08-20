package com.med.connect.controllers;

import com.med.connect.constants.admin.UrlMappings;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class TestController {

  @GetMapping(UrlMappings.ALL)
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping(UrlMappings.MOD)
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping(UrlMappings.ADMIN)
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @GetMapping(UrlMappings.STUDENT)
  @PreAuthorize("hasRole('STUDENT')")
  public String studentAccess() {
    return "Student Board.";
  }

  @GetMapping(UrlMappings.DOCTOR)
  @PreAuthorize("hasRole('DOCTOR')")
  public String doctorAccess() {
    return "Doctor Board.";
  }

  @GetMapping(UrlMappings.USER)
  @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
  public String userAccess() {
    return "User Content.";
  }


}
