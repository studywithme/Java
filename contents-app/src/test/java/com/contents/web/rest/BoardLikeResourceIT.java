package com.contents.web.rest;

import com.contents.ContentsApp;
import com.contents.domain.BoardLike;
import com.contents.repository.BoardLikeRepository;
import com.contents.service.BoardLikeService;
import com.contents.service.dto.BoardLikeDTO;
import com.contents.service.mapper.BoardLikeMapper;
import com.contents.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.contents.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link BoardLikeResource} REST controller.
 */
@SpringBootTest(classes = ContentsApp.class)
public class BoardLikeResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    @Autowired
    private BoardLikeMapper boardLikeMapper;

    @Autowired
    private BoardLikeService boardLikeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restBoardLikeMockMvc;

    private BoardLike boardLike;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoardLikeResource boardLikeResource = new BoardLikeResource(boardLikeService);
        this.restBoardLikeMockMvc = MockMvcBuilders.standaloneSetup(boardLikeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardLike createEntity(EntityManager em) {
        BoardLike boardLike = new BoardLike()
            .userId(DEFAULT_USER_ID)
            .createdDate(DEFAULT_CREATED_DATE);
        return boardLike;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardLike createUpdatedEntity(EntityManager em) {
        BoardLike boardLike = new BoardLike()
            .userId(UPDATED_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);
        return boardLike;
    }

    @BeforeEach
    public void initTest() {
        boardLike = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoardLike() throws Exception {
        int databaseSizeBeforeCreate = boardLikeRepository.findAll().size();

        // Create the BoardLike
        BoardLikeDTO boardLikeDTO = boardLikeMapper.toDto(boardLike);
        restBoardLikeMockMvc.perform(post("/api/board-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardLikeDTO)))
            .andExpect(status().isCreated());

        // Validate the BoardLike in the database
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(databaseSizeBeforeCreate + 1);
        BoardLike testBoardLike = boardLikeList.get(boardLikeList.size() - 1);
        assertThat(testBoardLike.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBoardLike.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createBoardLikeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boardLikeRepository.findAll().size();

        // Create the BoardLike with an existing ID
        boardLike.setId(1L);
        BoardLikeDTO boardLikeDTO = boardLikeMapper.toDto(boardLike);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardLikeMockMvc.perform(post("/api/board-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardLikeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BoardLike in the database
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardLikeRepository.findAll().size();
        // set the field null
        boardLike.setUserId(null);

        // Create the BoardLike, which fails.
        BoardLikeDTO boardLikeDTO = boardLikeMapper.toDto(boardLike);

        restBoardLikeMockMvc.perform(post("/api/board-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardLikeDTO)))
            .andExpect(status().isBadRequest());

        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoardLikes() throws Exception {
        // Initialize the database
        boardLikeRepository.saveAndFlush(boardLike);

        // Get all the boardLikeList
        restBoardLikeMockMvc.perform(get("/api/board-likes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardLike.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getBoardLike() throws Exception {
        // Initialize the database
        boardLikeRepository.saveAndFlush(boardLike);

        // Get the boardLike
        restBoardLikeMockMvc.perform(get("/api/board-likes/{id}", boardLike.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boardLike.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBoardLike() throws Exception {
        // Get the boardLike
        restBoardLikeMockMvc.perform(get("/api/board-likes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoardLike() throws Exception {
        // Initialize the database
        boardLikeRepository.saveAndFlush(boardLike);

        int databaseSizeBeforeUpdate = boardLikeRepository.findAll().size();

        // Update the boardLike
        BoardLike updatedBoardLike = boardLikeRepository.findById(boardLike.getId()).get();
        // Disconnect from session so that the updates on updatedBoardLike are not directly saved in db
        em.detach(updatedBoardLike);
        updatedBoardLike
            .userId(UPDATED_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);
        BoardLikeDTO boardLikeDTO = boardLikeMapper.toDto(updatedBoardLike);

        restBoardLikeMockMvc.perform(put("/api/board-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardLikeDTO)))
            .andExpect(status().isOk());

        // Validate the BoardLike in the database
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(databaseSizeBeforeUpdate);
        BoardLike testBoardLike = boardLikeList.get(boardLikeList.size() - 1);
        assertThat(testBoardLike.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBoardLike.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBoardLike() throws Exception {
        int databaseSizeBeforeUpdate = boardLikeRepository.findAll().size();

        // Create the BoardLike
        BoardLikeDTO boardLikeDTO = boardLikeMapper.toDto(boardLike);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardLikeMockMvc.perform(put("/api/board-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardLikeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BoardLike in the database
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoardLike() throws Exception {
        // Initialize the database
        boardLikeRepository.saveAndFlush(boardLike);

        int databaseSizeBeforeDelete = boardLikeRepository.findAll().size();

        // Delete the boardLike
        restBoardLikeMockMvc.perform(delete("/api/board-likes/{id}", boardLike.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardLike.class);
        BoardLike boardLike1 = new BoardLike();
        boardLike1.setId(1L);
        BoardLike boardLike2 = new BoardLike();
        boardLike2.setId(boardLike1.getId());
        assertThat(boardLike1).isEqualTo(boardLike2);
        boardLike2.setId(2L);
        assertThat(boardLike1).isNotEqualTo(boardLike2);
        boardLike1.setId(null);
        assertThat(boardLike1).isNotEqualTo(boardLike2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardLikeDTO.class);
        BoardLikeDTO boardLikeDTO1 = new BoardLikeDTO();
        boardLikeDTO1.setId(1L);
        BoardLikeDTO boardLikeDTO2 = new BoardLikeDTO();
        assertThat(boardLikeDTO1).isNotEqualTo(boardLikeDTO2);
        boardLikeDTO2.setId(boardLikeDTO1.getId());
        assertThat(boardLikeDTO1).isEqualTo(boardLikeDTO2);
        boardLikeDTO2.setId(2L);
        assertThat(boardLikeDTO1).isNotEqualTo(boardLikeDTO2);
        boardLikeDTO1.setId(null);
        assertThat(boardLikeDTO1).isNotEqualTo(boardLikeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(boardLikeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(boardLikeMapper.fromId(null)).isNull();
    }
}
