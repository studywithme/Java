package com.contents.service;

import com.contents.domain.BoardLike;
import com.contents.repository.BoardLikeRepository;
import com.contents.service.dto.BoardLikeDTO;
import com.contents.service.mapper.BoardLikeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BoardLike}.
 */
@Service
@Transactional
public class BoardLikeService {

    private final Logger log = LoggerFactory.getLogger(BoardLikeService.class);

    private final BoardLikeRepository boardLikeRepository;

    private final BoardLikeMapper boardLikeMapper;

    public BoardLikeService(BoardLikeRepository boardLikeRepository, BoardLikeMapper boardLikeMapper) {
        this.boardLikeRepository = boardLikeRepository;
        this.boardLikeMapper = boardLikeMapper;
    }

    /**
     * Save a boardLike.
     *
     * @param boardLikeDTO the entity to save.
     * @return the persisted entity.
     */
    public BoardLikeDTO save(BoardLikeDTO boardLikeDTO) {
        log.debug("Request to save BoardLike : {}", boardLikeDTO);
        BoardLike boardLike = boardLikeMapper.toEntity(boardLikeDTO);
        boardLike = boardLikeRepository.save(boardLike);
        return boardLikeMapper.toDto(boardLike);
    }

    /**
     * Get all the boardLikes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BoardLikeDTO> findAll() {
        log.debug("Request to get all BoardLikes");
        return boardLikeRepository.findAll().stream()
            .map(boardLikeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one boardLike by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BoardLikeDTO> findOne(Long id) {
        log.debug("Request to get BoardLike : {}", id);
        return boardLikeRepository.findById(id)
            .map(boardLikeMapper::toDto);
    }

    /**
     * Delete the boardLike by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardLike : {}", id);
        boardLikeRepository.deleteById(id);
    }
}
