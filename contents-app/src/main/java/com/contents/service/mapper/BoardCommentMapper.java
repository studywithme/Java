package com.contents.service.mapper;

import com.contents.domain.*;
import com.contents.service.dto.BoardCommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BoardComment} and its DTO {@link BoardCommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {BoardMapper.class})
public interface BoardCommentMapper extends EntityMapper<BoardCommentDTO, BoardComment> {

    @Mapping(source = "board.id", target = "boardId")
    BoardCommentDTO toDto(BoardComment boardComment);

    @Mapping(source = "boardId", target = "board")
    BoardComment toEntity(BoardCommentDTO boardCommentDTO);

    default BoardComment fromId(Long id) {
        if (id == null) {
            return null;
        }
        BoardComment boardComment = new BoardComment();
        boardComment.setId(id);
        return boardComment;
    }
}
