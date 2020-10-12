package com.contents.service.mapper;

import com.contents.domain.*;
import com.contents.service.dto.BoardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Board} and its DTO {@link BoardDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BoardMapper extends EntityMapper<BoardDTO, Board> {


    @Mapping(target = "comments", ignore = true)
    Board toEntity(BoardDTO boardDTO);

    default Board fromId(Long id) {
        if (id == null) {
            return null;
        }
        Board board = new Board();
        board.setId(id);
        return board;
    }
}
