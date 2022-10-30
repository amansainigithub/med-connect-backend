package com.med.connect.services.adminServices.medRoleService.impl;

import com.med.connect.dto.adminDto.medRoleDto.MedRoleDTO;
import com.med.connect.exception.DataNotFoundException;
import com.med.connect.messageResp.GenericMessageResponse;
import com.med.connect.services.adminServices.medRoleService.MedRoleService;
import com.med.connect.utils.ResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MedRoleServiceImpl  implements MedRoleService {

    @Override
    public ResponseEntity<?>  getMedRoles() {
        try {
            List<MedRoleDTO> medRoleDTOList = new ArrayList<>();
            medRoleDTOList.add(new MedRoleDTO("USER"));
            medRoleDTOList.add(new MedRoleDTO("STUDENT"));
            medRoleDTOList.add(new MedRoleDTO("DOCTOR"));
            return ResponseGenerator.generateSuccessResponse(medRoleDTOList, GenericMessageResponse.ROLE_FETCH_SUCCESS);
        }
        catch (DataNotFoundException e)
        {
            log.info("Error in fetching the record :::::::::::::: {}");
            log.error("Exception " , e);
            throw new NullPointerException(GenericMessageResponse.DATA_NOT_FOUND);
        }
    }
}
