package com.contents.service.mapper;

import com.contents.domain.*;
import com.contents.service.dto.BoardLikeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BoardLike} and its DTO {@link BoardLikeDTO}.
 */
@Mapper(componentModel = "spring", uses = {BoardMapper.class})
public interface BoardLikeMapper extends EntityMapper<BoardLikeDTO, BoardLike> {

    @Mapping(source = "board.id", target = "boardId")
    BoardLikeDTO toDto(BoardLike boardLike);

    @Mapping(source = "boardId", target = "board")
    BoardLike toEntity(BoardLikeDTO boardLikeDTO);

    default BoardLike fromId(Long id) {
        if (id == null) {
            return null;
        }
        BoardLike boardLike = new BoardLike();
        boardLike.setId(id);
        return boardLike;
    }
}
