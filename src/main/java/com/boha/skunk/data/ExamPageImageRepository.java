package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamPageImageRepository extends JpaRepository<ExamPageImage, Long> {


    List<ExamPageImage> findByExamLinkId(Long examLinkId);
}
