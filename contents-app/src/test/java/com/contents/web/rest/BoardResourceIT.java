package com.contents.web.rest;

import com.contents.ContentsApp;
import com.contents.domain.Board;
import com.contents.repository.BoardRepository;
import com.contents.service.BoardService;
import com.contents.service.dto.BoardDTO;
import com.contents.service.mapper.BoardMapper;
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
 * Integration tests for the {@Link BoardResource} REST controller.
 */
@SpringBootTest(classes = ContentsApp.class)
public class BoardResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final String DEFAULT_BOARD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BOARD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT_VIEW = 1;
    private static final Integer UPDATED_COUNT_VIEW = 2;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private BoardService boardService;

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

    private MockMvc restBoardMockMvc;

    private Board board;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoardResource boardResource = new BoardResource(boardService);
        this.restBoardMockMvc = MockMvcBuilders.standaloneSetup(boardResource)
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
    public static Board createEntity(EntityManager em) {
        Board board = new Board()
            .userId(DEFAULT_USER_ID)
            .CategoryId(DEFAULT_CATEGORY_ID)
            .boardType(DEFAULT_BOARD_TYPE)
            .nickname(DEFAULT_NICKNAME)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .countView(DEFAULT_COUNT_VIEW)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return board;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Board createUpdatedEntity(EntityManager em) {
        Board board = new Board()
            .userId(UPDATED_USER_ID)
            .CategoryId(UPDATED_CATEGORY_ID)
            .boardType(UPDATED_BOARD_TYPE)
            .nickname(UPDATED_NICKNAME)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .countView(UPDATED_COUNT_VIEW)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return board;
    }

    @BeforeEach
    public void initTest() {
        board = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoard() throws Exception {
        int databaseSizeBeforeCreate = boardRepository.findAll().size();

        // Create the Board
        BoardDTO boardDTO = boardMapper.toDto(board);
        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isCreated());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate + 1);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBoard.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testBoard.getBoardType()).isEqualTo(DEFAULT_BOARD_TYPE);
        assertThat(testBoard.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testBoard.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testBoard.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBoard.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBoard.getCountView()).isEqualTo(DEFAULT_COUNT_VIEW);
        assertThat(testBoard.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBoard.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createBoardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boardRepository.findAll().size();

        // Create the Board with an existing ID
        board.setId(1L);
        BoardDTO boardDTO = boardMapper.toDto(board);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardRepository.findAll().size();
        // set the field null
        board.setUserId(null);

        // Create the Board, which fails.
        BoardDTO boardDTO = boardMapper.toDto(board);

        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardRepository.findAll().size();
        // set the field null
        board.setCategoryId(null);

        // Create the Board, which fails.
        BoardDTO boardDTO = boardMapper.toDto(board);

        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBoardTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardRepository.findAll().size();
        // set the field null
        board.setBoardType(null);

        // Create the Board, which fails.
        BoardDTO boardDTO = boardMapper.toDto(board);

        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardRepository.findAll().size();
        // set the field null
        board.setTitle(null);

        // Create the Board, which fails.
        BoardDTO boardDTO = boardMapper.toDto(board);

        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoards() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList
        restBoardMockMvc.perform(get("/api/boards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(board.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].CategoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].boardType").value(hasItem(DEFAULT_BOARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].countView").value(hasItem(DEFAULT_COUNT_VIEW)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get the board
        restBoardMockMvc.perform(get("/api/boards/{id}", board.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(board.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.CategoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.boardType").value(DEFAULT_BOARD_TYPE.toString()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.countView").value(DEFAULT_COUNT_VIEW))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBoard() throws Exception {
        // Get the board
        restBoardMockMvc.perform(get("/api/boards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board
        Board updatedBoard = boardRepository.findById(board.getId()).get();
        // Disconnect from session so that the updates on updatedBoard are not directly saved in db
        em.detach(updatedBoard);
        updatedBoard
            .userId(UPDATED_USER_ID)
            .CategoryId(UPDATED_CATEGORY_ID)
            .boardType(UPDATED_BOARD_TYPE)
            .nickname(UPDATED_NICKNAME)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .countView(UPDATED_COUNT_VIEW)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        BoardDTO boardDTO = boardMapper.toDto(updatedBoard);

        restBoardMockMvc.perform(put("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBoard.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testBoard.getBoardType()).isEqualTo(UPDATED_BOARD_TYPE);
        assertThat(testBoard.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testBoard.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testBoard.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBoard.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoard.getCountView()).isEqualTo(UPDATED_COUNT_VIEW);
        assertThat(testBoard.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBoard.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Create the Board
        BoardDTO boardDTO = boardMapper.toDto(board);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardMockMvc.perform(put("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        int databaseSizeBeforeDelete = boardRepository.findAll().size();

        // Delete the board
        restBoardMockMvc.perform(delete("/api/boards/{id}", board.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Board.class);
        Board board1 = new Board();
        board1.setId(1L);
        Board board2 = new Board();
        board2.setId(board1.getId());
        assertThat(board1).isEqualTo(board2);
        board2.setId(2L);
        assertThat(board1).isNotEqualTo(board2);
        board1.setId(null);
        assertThat(board1).isNotEqualTo(board2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardDTO.class);
        BoardDTO boardDTO1 = new BoardDTO();
        boardDTO1.setId(1L);
        BoardDTO boardDTO2 = new BoardDTO();
        assertThat(boardDTO1).isNotEqualTo(boardDTO2);
        boardDTO2.setId(boardDTO1.getId());
        assertThat(boardDTO1).isEqualTo(boardDTO2);
        boardDTO2.setId(2L);
        assertThat(boardDTO1).isNotEqualTo(boardDTO2);
        boardDTO1.setId(null);
        assertThat(boardDTO1).isNotEqualTo(boardDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(boardMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(boardMapper.fromId(null)).isNull();
    }
}
