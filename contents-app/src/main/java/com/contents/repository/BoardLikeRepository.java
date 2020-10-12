package com.contents.repository;

import com.contents.domain.BoardLike;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BoardLike entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

}
