package com.med.connect.services.adminServices.medRoleService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface MedRoleService {
    public  ResponseEntity<?> getMedRoles();
}
