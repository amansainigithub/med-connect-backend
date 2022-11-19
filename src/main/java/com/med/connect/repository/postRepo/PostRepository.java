package com.med.connect.repository.postRepo;

import com.med.connect.domain.postDomain.MedPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<MedPosts,Long> {
}
