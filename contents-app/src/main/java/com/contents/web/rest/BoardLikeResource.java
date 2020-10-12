package com.contents.web.rest;

import com.contents.service.BoardLikeService;
import com.contents.web.rest.errors.BadRequestAlertException;
import com.contents.service.dto.BoardLikeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.contents.domain.BoardLike}.
 */
@RestController
@RequestMapping("/api")
public class BoardLikeResource {

    private final Logger log = LoggerFactory.getLogger(BoardLikeResource.class);

    private static final String ENTITY_NAME = "boardLike";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardLikeService boardLikeService;

    public BoardLikeResource(BoardLikeService boardLikeService) {
        this.boardLikeService = boardLikeService;
    }

    /**
     * {@code POST  /board-likes} : Create a new boardLike.
     *
     * @param boardLikeDTO the boardLikeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardLikeDTO, or with status {@code 400 (Bad Request)} if the boardLike has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-likes")
    public ResponseEntity<BoardLikeDTO> createBoardLike(@Valid @RequestBody BoardLikeDTO boardLikeDTO) throws URISyntaxException {
        log.debug("REST request to save BoardLike : {}", boardLikeDTO);
        if (boardLikeDTO.getId() != null) {
            throw new BadRequestAlertException("A new boardLike cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardLikeDTO result = boardLikeService.save(boardLikeDTO);
        return ResponseEntity.created(new URI("/api/board-likes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-likes} : Updates an existing boardLike.
     *
     * @param boardLikeDTO the boardLikeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardLikeDTO,
     * or with status {@code 400 (Bad Request)} if the boardLikeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardLikeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-likes")
    public ResponseEntity<BoardLikeDTO> updateBoardLike(@Valid @RequestBody BoardLikeDTO boardLikeDTO) throws URISyntaxException {
        log.debug("REST request to update BoardLike : {}", boardLikeDTO);
        if (boardLikeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BoardLikeDTO result = boardLikeService.save(boardLikeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardLikeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /board-likes} : get all the boardLikes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardLikes in body.
     */
    @GetMapping("/board-likes")
    public List<BoardLikeDTO> getAllBoardLikes() {
        log.debug("REST request to get all BoardLikes");
        return boardLikeService.findAll();
    }

    /**
     * {@code GET  /board-likes/:id} : get the "id" boardLike.
     *
     * @param id the id of the boardLikeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardLikeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-likes/{id}")
    public ResponseEntity<BoardLikeDTO> getBoardLike(@PathVariable Long id) {
        log.debug("REST request to get BoardLike : {}", id);
        Optional<BoardLikeDTO> boardLikeDTO = boardLikeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardLikeDTO);
    }

    /**
     * {@code DELETE  /board-likes/:id} : delete the "id" boardLike.
     *
     * @param id the id of the boardLikeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-likes/{id}")
    public ResponseEntity<Void> deleteBoardLike(@PathVariable Long id) {
        log.debug("REST request to delete BoardLike : {}", id);
        boardLikeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
