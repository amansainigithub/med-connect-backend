package com.med.connect.repository.questionRepo;

import com.med.connect.domain.questionDomain.QuestionFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionFilesRepo extends JpaRepository<QuestionFiles , Long> {

    List<QuestionFiles> findByQuestionId(Long id);

}
