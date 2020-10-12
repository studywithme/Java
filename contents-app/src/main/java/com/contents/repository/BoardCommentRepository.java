package com.contents.repository;

import com.contents.domain.BoardComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BoardComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

}
