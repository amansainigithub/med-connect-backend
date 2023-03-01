package com.med.connect.payload.request;

import lombok.Data;

import javax.persistence.Entity;

@Data
public class VotingUpAndDownPayload {

    private String userId;

    private String voteResult;

    private String questionId;

}
