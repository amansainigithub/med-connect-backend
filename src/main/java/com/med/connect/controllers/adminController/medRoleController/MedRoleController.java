package com.med.connect.controllers.adminController.medRoleController;

import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.services.adminServices.medRoleService.MedRoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(UrlMappings.ADMIN_BASE_URL)
@Slf4j
@RestController
@Api("Api for  Med Role")
public class MedRoleController {


    @Autowired
    private MedRoleService medRoleService;


    @GetMapping(UrlMappings.GET_MED_ROLES)
    public ResponseEntity<?> getMedRoles()
    {
        return medRoleService.getMedRoles();
    }
}
