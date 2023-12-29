package com.boha.skunk.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findBySubjectId(Long subjectId);
    List<Tag> findBySubjectIdAndTagType(Long subjectId, int tagType);

}
