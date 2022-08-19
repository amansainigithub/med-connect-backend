package com.med.connect.controllers;

import com.med.connect.constants.admin.UrlMappings;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(UrlMappings.BASE_URL)
public class TestController {

  @GetMapping(UrlMappings.ALL)
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping(UrlMappings.USER)
  @PreAuthorize("hasRole('USER')")
  public String userAccess() {
    return "User Content.";
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

}
