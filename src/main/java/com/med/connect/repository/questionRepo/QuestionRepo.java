package com.med.connect.repository.questionRepo;

import com.med.connect.domain.questionDomain.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface QuestionRepo extends JpaRepository<Questions,Long> {


}
