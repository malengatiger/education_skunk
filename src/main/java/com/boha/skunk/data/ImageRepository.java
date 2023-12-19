package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ExamImage, Long> {

    List<ExamImage> findByExamPaperId(Long examPaperId);
}
