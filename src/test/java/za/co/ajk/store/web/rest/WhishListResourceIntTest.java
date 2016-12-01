package za.co.ajk.store.web.rest;

import za.co.ajk.store.HipsterstoreApp;

import za.co.ajk.store.domain.WhishList;
import za.co.ajk.store.repository.WhishListRepository;
import za.co.ajk.store.service.WhishListService;
import za.co.ajk.store.service.dto.WhishListDTO;
import za.co.ajk.store.service.mapper.WhishListMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WhishListResource REST controller.
 *
 * @see WhishListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterstoreApp.class)
public class WhishListResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_HIDDEN = false;
    private static final Boolean UPDATED_HIDDEN = true;

    @Inject
    private WhishListRepository whishListRepository;

    @Inject
    private WhishListMapper whishListMapper;

    @Inject
    private WhishListService whishListService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWhishListMockMvc;

    private WhishList whishList;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WhishListResource whishListResource = new WhishListResource();
        ReflectionTestUtils.setField(whishListResource, "whishListService", whishListService);
        this.restWhishListMockMvc = MockMvcBuilders.standaloneSetup(whishListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WhishList createEntity(EntityManager em) {
        WhishList whishList = new WhishList()
                .name(DEFAULT_NAME)
                .creationDate(DEFAULT_CREATION_DATE)
                .hidden(DEFAULT_HIDDEN);
        return whishList;
    }

    @Before
    public void initTest() {
        whishList = createEntity(em);
    }

    @Test
    @Transactional
    public void createWhishList() throws Exception {
        int databaseSizeBeforeCreate = whishListRepository.findAll().size();

        // Create the WhishList
        WhishListDTO whishListDTO = whishListMapper.whishListToWhishListDTO(whishList);

        restWhishListMockMvc.perform(post("/api/whish-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(whishListDTO)))
                .andExpect(status().isCreated());

        // Validate the WhishList in the database
        List<WhishList> whishLists = whishListRepository.findAll();
        assertThat(whishLists).hasSize(databaseSizeBeforeCreate + 1);
        WhishList testWhishList = whishLists.get(whishLists.size() - 1);
        assertThat(testWhishList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWhishList.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testWhishList.isHidden()).isEqualTo(DEFAULT_HIDDEN);
    }

    @Test
    @Transactional
    public void getAllWhishLists() throws Exception {
        // Initialize the database
        whishListRepository.saveAndFlush(whishList);

        // Get all the whishLists
        restWhishListMockMvc.perform(get("/api/whish-lists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(whishList.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
                .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())));
    }

    @Test
    @Transactional
    public void getWhishList() throws Exception {
        // Initialize the database
        whishListRepository.saveAndFlush(whishList);

        // Get the whishList
        restWhishListMockMvc.perform(get("/api/whish-lists/{id}", whishList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(whishList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.hidden").value(DEFAULT_HIDDEN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWhishList() throws Exception {
        // Get the whishList
        restWhishListMockMvc.perform(get("/api/whish-lists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWhishList() throws Exception {
        // Initialize the database
        whishListRepository.saveAndFlush(whishList);
        int databaseSizeBeforeUpdate = whishListRepository.findAll().size();

        // Update the whishList
        WhishList updatedWhishList = whishListRepository.findOne(whishList.getId());
        updatedWhishList
                .name(UPDATED_NAME)
                .creationDate(UPDATED_CREATION_DATE)
                .hidden(UPDATED_HIDDEN);
        WhishListDTO whishListDTO = whishListMapper.whishListToWhishListDTO(updatedWhishList);

        restWhishListMockMvc.perform(put("/api/whish-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(whishListDTO)))
                .andExpect(status().isOk());

        // Validate the WhishList in the database
        List<WhishList> whishLists = whishListRepository.findAll();
        assertThat(whishLists).hasSize(databaseSizeBeforeUpdate);
        WhishList testWhishList = whishLists.get(whishLists.size() - 1);
        assertThat(testWhishList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWhishList.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testWhishList.isHidden()).isEqualTo(UPDATED_HIDDEN);
    }

    @Test
    @Transactional
    public void deleteWhishList() throws Exception {
        // Initialize the database
        whishListRepository.saveAndFlush(whishList);
        int databaseSizeBeforeDelete = whishListRepository.findAll().size();

        // Get the whishList
        restWhishListMockMvc.perform(delete("/api/whish-lists/{id}", whishList.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WhishList> whishLists = whishListRepository.findAll();
        assertThat(whishLists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
