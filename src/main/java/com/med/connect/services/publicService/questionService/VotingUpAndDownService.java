package com.med.connect.services.publicService.questionService;

import com.med.connect.domain.questionDomain.Questions;
import com.med.connect.domain.questionDomain.VotingUpAndDownInfo;
import com.med.connect.exception.QuestionIdNotFoundException;
import com.med.connect.payload.request.VotingUpAndDownPayload;
import com.med.connect.repository.UserRepository;
import com.med.connect.repository.questionRepo.QuestionRepo;
import com.med.connect.repository.votingRepo.VotingUpAndDownRepo;
import com.med.connect.services.publicService.questionService.impl.VotingUpAndDownServiceImpl;
import jdk.nashorn.internal.runtime.options.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VotingUpAndDownService implements VotingUpAndDownServiceImpl {

    @Autowired
    private VotingUpAndDownRepo votingUpAndDownRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private UserRepository userRepository;




    public  VotingUpAndDownInfo  votingUpAndDownInfoService(VotingUpAndDownPayload votingUpAndDownPayload)
    {
        VotingUpAndDownInfo votingUpAndDownInfo = null;
            try {
                if( isVoteUpAndDown( votingUpAndDownPayload.getQuestionId() ,
                                      votingUpAndDownPayload.getUserId())){

                    updateUpAndDownVote(votingUpAndDownPayload);
                }
                else {
                    votingUpAndDownInfo = new VotingUpAndDownInfo();
                    votingUpAndDownInfo.setUserId(votingUpAndDownPayload.getUserId());
                    votingUpAndDownInfo.setVote(votingUpAndDownPayload.getVoteResult());
                    votingUpAndDownInfo.setQuestionId(votingUpAndDownPayload.getQuestionId());
                    votingUpAndDownInfo.setIsVoter(Boolean.TRUE);

                    votingUpAndDownInfo = this.votingUpAndDownRepo.save(votingUpAndDownInfo);
                    log.info("Data save Success !!! ");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                log.error("Something went wrong in votingUpAndDownInfoService ");
            }
            return  votingUpAndDownInfo;
    }


    public boolean isVoteUpAndDown(String questionId , String userId)
    {
       Optional<VotingUpAndDownInfo> votingUpAndDownInfo = this.votingUpAndDownRepo
                                                            .findByUserIdAndQuestionId( userId , questionId );
       if(votingUpAndDownInfo.isPresent()){
           return Boolean.TRUE;
       }
       else {
           return Boolean.FALSE;
       }
    }

    public void updateUpAndDownVote(VotingUpAndDownPayload votingUpAndDownPayload)
    {
        Optional<VotingUpAndDownInfo> vi = this.votingUpAndDownRepo
                                            .findByUserIdAndQuestionId(
                                                    votingUpAndDownPayload.getUserId(),
                                                       votingUpAndDownPayload.getQuestionId() );
        if(vi.isPresent())
        {
            VotingUpAndDownInfo downAndUp = vi.get();
            if( !votingUpAndDownPayload.getVoteResult().equals(downAndUp.getVote()) ){
                downAndUp.setVote(votingUpAndDownPayload.getVoteResult());
                votingUpAndDownRepo.save(downAndUp);
                log.info("Vote Up and Down Updated Success !!! ");
            }else {
                votingUpAndDownRepo.delete(downAndUp);
                log.info("Delete Success !!! ");
            }
        }
    }


}
