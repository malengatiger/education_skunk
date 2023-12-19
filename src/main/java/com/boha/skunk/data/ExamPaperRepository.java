package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPaperRepository extends JpaRepository<ExamPaper, Long> {

    @Modifying
    @Query("UPDATE ExamPaper ep SET ep.tablesString = :tablesString WHERE ep.id = :id")
    void updateTablesStringById(Long id, String tablesString);


}
