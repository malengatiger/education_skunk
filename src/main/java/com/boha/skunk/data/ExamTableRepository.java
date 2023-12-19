package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamTableRepository extends JpaRepository<ExamTable, Long> {
    List<ExamTable> findByExamPaperId(Long examPaperId);

}
