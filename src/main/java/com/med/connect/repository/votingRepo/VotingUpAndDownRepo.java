package com.med.connect.repository.votingRepo;

import com.med.connect.domain.questionDomain.VotingUpAndDownInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingUpAndDownRepo extends JpaRepository< VotingUpAndDownInfo , Long> {

    public Optional<VotingUpAndDownInfo> findByUserIdAndQuestionId(String userId ,
                                                                   String questionId);

    @Query("SELECT COUNT(u) FROM VotingUpAndDownInfo u WHERE u.questionId=:questionId and u.vote=:vote")
    long countFromQuestionIdAndVote(@Param("questionId") String questionId ,
                             @Param("vote") String vote );


    VotingUpAndDownInfo findByQuestionIdAndUserId( String questionId ,
                                                   String userId );
}
