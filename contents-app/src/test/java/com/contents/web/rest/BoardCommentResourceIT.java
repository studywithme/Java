package com.contents.web.rest;

import com.contents.ContentsApp;
import com.contents.domain.BoardComment;
import com.contents.repository.BoardCommentRepository;
import com.contents.service.BoardCommentService;
import com.contents.service.dto.BoardCommentDTO;
import com.contents.service.mapper.BoardCommentMapper;
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
 * Integration tests for the {@Link BoardCommentResource} REST controller.
 */
@SpringBootTest(classes = ContentsApp.class)
public class BoardCommentResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private BoardCommentMapper boardCommentMapper;

    @Autowired
    private BoardCommentService boardCommentService;

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

    private MockMvc restBoardCommentMockMvc;

    private BoardComment boardComment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoardCommentResource boardCommentResource = new BoardCommentResource(boardCommentService);
        this.restBoardCommentMockMvc = MockMvcBuilders.standaloneSetup(boardCommentResource)
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
    public static BoardComment createEntity(EntityManager em) {
        BoardComment boardComment = new BoardComment()
            .userId(DEFAULT_USER_ID)
            .nickname(DEFAULT_NICKNAME)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .content(DEFAULT_CONTENT)
            .createdDate(DEFAULT_CREATED_DATE);
        return boardComment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardComment createUpdatedEntity(EntityManager em) {
        BoardComment boardComment = new BoardComment()
            .userId(UPDATED_USER_ID)
            .nickname(UPDATED_NICKNAME)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE);
        return boardComment;
    }

    @BeforeEach
    public void initTest() {
        boardComment = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoardComment() throws Exception {
        int databaseSizeBeforeCreate = boardCommentRepository.findAll().size();

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);
        restBoardCommentMockMvc.perform(post("/api/board-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO)))
            .andExpect(status().isCreated());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeCreate + 1);
        BoardComment testBoardComment = boardCommentList.get(boardCommentList.size() - 1);
        assertThat(testBoardComment.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBoardComment.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testBoardComment.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testBoardComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBoardComment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createBoardCommentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boardCommentRepository.findAll().size();

        // Create the BoardComment with an existing ID
        boardComment.setId(1L);
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardCommentMockMvc.perform(post("/api/board-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardCommentRepository.findAll().size();
        // set the field null
        boardComment.setUserId(null);

        // Create the BoardComment, which fails.
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        restBoardCommentMockMvc.perform(post("/api/board-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO)))
            .andExpect(status().isBadRequest());

        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoardComments() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        // Get all the boardCommentList
        restBoardCommentMockMvc.perform(get("/api/board-comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getBoardComment() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        // Get the boardComment
        restBoardCommentMockMvc.perform(get("/api/board-comments/{id}", boardComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boardComment.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBoardComment() throws Exception {
        // Get the boardComment
        restBoardCommentMockMvc.perform(get("/api/board-comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoardComment() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();

        // Update the boardComment
        BoardComment updatedBoardComment = boardCommentRepository.findById(boardComment.getId()).get();
        // Disconnect from session so that the updates on updatedBoardComment are not directly saved in db
        em.detach(updatedBoardComment);
        updatedBoardComment
            .userId(UPDATED_USER_ID)
            .nickname(UPDATED_NICKNAME)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE);
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(updatedBoardComment);

        restBoardCommentMockMvc.perform(put("/api/board-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO)))
            .andExpect(status().isOk());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
        BoardComment testBoardComment = boardCommentList.get(boardCommentList.size() - 1);
        assertThat(testBoardComment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBoardComment.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testBoardComment.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testBoardComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoardComment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBoardComment() throws Exception {
        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardCommentMockMvc.perform(put("/api/board-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoardComment() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        int databaseSizeBeforeDelete = boardCommentRepository.findAll().size();

        // Delete the boardComment
        restBoardCommentMockMvc.perform(delete("/api/board-comments/{id}", boardComment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardComment.class);
        BoardComment boardComment1 = new BoardComment();
        boardComment1.setId(1L);
        BoardComment boardComment2 = new BoardComment();
        boardComment2.setId(boardComment1.getId());
        assertThat(boardComment1).isEqualTo(boardComment2);
        boardComment2.setId(2L);
        assertThat(boardComment1).isNotEqualTo(boardComment2);
        boardComment1.setId(null);
        assertThat(boardComment1).isNotEqualTo(boardComment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardCommentDTO.class);
        BoardCommentDTO boardCommentDTO1 = new BoardCommentDTO();
        boardCommentDTO1.setId(1L);
        BoardCommentDTO boardCommentDTO2 = new BoardCommentDTO();
        assertThat(boardCommentDTO1).isNotEqualTo(boardCommentDTO2);
        boardCommentDTO2.setId(boardCommentDTO1.getId());
        assertThat(boardCommentDTO1).isEqualTo(boardCommentDTO2);
        boardCommentDTO2.setId(2L);
        assertThat(boardCommentDTO1).isNotEqualTo(boardCommentDTO2);
        boardCommentDTO1.setId(null);
        assertThat(boardCommentDTO1).isNotEqualTo(boardCommentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(boardCommentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(boardCommentMapper.fromId(null)).isNull();
    }
}
