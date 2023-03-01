package com.med.connect.services.publicService.questionService.impl;

import com.med.connect.domain.questionDomain.VotingUpAndDownInfo;
import com.med.connect.payload.request.VotingUpAndDownPayload;
import org.springframework.stereotype.Component;

@Component
public interface VotingUpAndDownServiceImpl {

    public VotingUpAndDownInfo votingUpAndDownInfoService(VotingUpAndDownPayload votingUpAndDownPayload);
}
