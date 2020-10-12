package com.contents.service;

import com.contents.domain.BoardComment;
import com.contents.repository.BoardCommentRepository;
import com.contents.service.dto.BoardCommentDTO;
import com.contents.service.mapper.BoardCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BoardComment}.
 */
@Service
@Transactional
public class BoardCommentService {

    private final Logger log = LoggerFactory.getLogger(BoardCommentService.class);

    private final BoardCommentRepository boardCommentRepository;

    private final BoardCommentMapper boardCommentMapper;

    public BoardCommentService(BoardCommentRepository boardCommentRepository, BoardCommentMapper boardCommentMapper) {
        this.boardCommentRepository = boardCommentRepository;
        this.boardCommentMapper = boardCommentMapper;
    }

    /**
     * Save a boardComment.
     *
     * @param boardCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public BoardCommentDTO save(BoardCommentDTO boardCommentDTO) {
        log.debug("Request to save BoardComment : {}", boardCommentDTO);
        BoardComment boardComment = boardCommentMapper.toEntity(boardCommentDTO);
        boardComment = boardCommentRepository.save(boardComment);
        return boardCommentMapper.toDto(boardComment);
    }

    /**
     * Get all the boardComments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BoardCommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BoardComments");
        return boardCommentRepository.findAll(pageable)
            .map(boardCommentMapper::toDto);
    }


    /**
     * Get one boardComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BoardCommentDTO> findOne(Long id) {
        log.debug("Request to get BoardComment : {}", id);
        return boardCommentRepository.findById(id)
            .map(boardCommentMapper::toDto);
    }

    /**
     * Delete the boardComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardComment : {}", id);
        boardCommentRepository.deleteById(id);
    }
}
