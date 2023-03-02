package com.med.connect.controllers.publicController.questionController;

import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.domain.questionDomain.VotingUpAndDownInfo;
import com.med.connect.payload.request.VotingUpAndDownPayload;
import com.med.connect.services.publicService.questionService.impl.VotingUpAndDownServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlMappings.ADMIN_BASE_URL)
public class VotingController {

    @Autowired
    private VotingUpAndDownServiceImpl votingUpAndDownServiceImpl;


    @PostMapping(UrlMappings.VOTE_UP_AND_DOWN)
    @PreAuthorize("hasRole('USER') or hasRole('DOCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> addQuestion(@RequestBody VotingUpAndDownPayload votingUpAndDownPayload) throws InterruptedException {
        Thread.sleep(1000);
        VotingUpAndDownInfo data = votingUpAndDownServiceImpl.votingUpAndDownInfoService(votingUpAndDownPayload);
        return ResponseEntity.ok(data);
    }


}
