package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExamLinkRepository extends JpaRepository<ExamLink, Long> {
    @Query("SELECT e.id, e.documentTitle FROM ExamLink e WHERE e.id IN (SELECT MIN(e2.id) FROM ExamLink e2 WHERE e2.documentTitle IS NOT NULL GROUP BY e2.documentTitle) ORDER BY e.documentTitle ASC")
    List<Object[]> findDistinctDocumentTitlesWithId();

    @Modifying
    @Transactional
    @Query("UPDATE ExamLink e SET e.pageImageZipUrl = :pageImageZipUrl WHERE e.id = :id")
    void updatePageImageZipUrlById(@Param("id") Long id, @Param("pageImageZipUrl") String pageImageZipUrl);

    List<ExamLink> findBySubjectId(Long subjectId);
}
