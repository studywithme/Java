package com.contents.web.rest;

import com.contents.service.BoardCommentService;
import com.contents.web.rest.errors.BadRequestAlertException;
import com.contents.service.dto.BoardCommentDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.contents.domain.BoardComment}.
 */
@RestController
@RequestMapping("/api")
public class BoardCommentResource {

    private final Logger log = LoggerFactory.getLogger(BoardCommentResource.class);

    private static final String ENTITY_NAME = "boardComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardCommentService boardCommentService;

    public BoardCommentResource(BoardCommentService boardCommentService) {
        this.boardCommentService = boardCommentService;
    }

    /**
     * {@code POST  /board-comments} : Create a new boardComment.
     *
     * @param boardCommentDTO the boardCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardCommentDTO, or with status {@code 400 (Bad Request)} if the boardComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-comments")
    public ResponseEntity<BoardCommentDTO> createBoardComment(@Valid @RequestBody BoardCommentDTO boardCommentDTO) throws URISyntaxException {
        log.debug("REST request to save BoardComment : {}", boardCommentDTO);
        if (boardCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new boardComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardCommentDTO result = boardCommentService.save(boardCommentDTO);
        return ResponseEntity.created(new URI("/api/board-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-comments} : Updates an existing boardComment.
     *
     * @param boardCommentDTO the boardCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardCommentDTO,
     * or with status {@code 400 (Bad Request)} if the boardCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-comments")
    public ResponseEntity<BoardCommentDTO> updateBoardComment(@Valid @RequestBody BoardCommentDTO boardCommentDTO) throws URISyntaxException {
        log.debug("REST request to update BoardComment : {}", boardCommentDTO);
        if (boardCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BoardCommentDTO result = boardCommentService.save(boardCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /board-comments} : get all the boardComments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardComments in body.
     */
    @GetMapping("/board-comments")
    public ResponseEntity<List<BoardCommentDTO>> getAllBoardComments(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of BoardComments");
        Page<BoardCommentDTO> page = boardCommentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /board-comments/:id} : get the "id" boardComment.
     *
     * @param id the id of the boardCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-comments/{id}")
    public ResponseEntity<BoardCommentDTO> getBoardComment(@PathVariable Long id) {
        log.debug("REST request to get BoardComment : {}", id);
        Optional<BoardCommentDTO> boardCommentDTO = boardCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardCommentDTO);
    }

    /**
     * {@code DELETE  /board-comments/:id} : delete the "id" boardComment.
     *
     * @param id the id of the boardCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-comments/{id}")
    public ResponseEntity<Void> deleteBoardComment(@PathVariable Long id) {
        log.debug("REST request to delete BoardComment : {}", id);
        boardCommentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
