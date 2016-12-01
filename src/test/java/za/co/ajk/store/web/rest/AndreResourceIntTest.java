package za.co.ajk.store.web.rest;

import za.co.ajk.store.HipsterstoreApp;

import za.co.ajk.store.domain.Andre;
import za.co.ajk.store.repository.AndreRepository;
import za.co.ajk.store.service.AndreService;
import za.co.ajk.store.service.dto.AndreDTO;
import za.co.ajk.store.service.mapper.AndreMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AndreResource REST controller.
 *
 * @see AndreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterstoreApp.class)
public class AndreResourceIntTest {

    private static final String DEFAULT_USER_NAME = "AAAAA";
    private static final String UPDATED_USER_NAME = "BBBBB";

    private static final String DEFAULT_PASSWD = "AAAAA";
    private static final String UPDATED_PASSWD = "BBBBB";

    @Inject
    private AndreRepository andreRepository;

    @Inject
    private AndreMapper andreMapper;

    @Inject
    private AndreService andreService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAndreMockMvc;

    private Andre andre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AndreResource andreResource = new AndreResource();
        ReflectionTestUtils.setField(andreResource, "andreService", andreService);
        this.restAndreMockMvc = MockMvcBuilders.standaloneSetup(andreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Andre createEntity(EntityManager em) {
        Andre andre = new Andre()
                .user_name(DEFAULT_USER_NAME)
                .passwd(DEFAULT_PASSWD);
        return andre;
    }

    @Before
    public void initTest() {
        andre = createEntity(em);
    }

    @Test
    @Transactional
    public void createAndre() throws Exception {
        int databaseSizeBeforeCreate = andreRepository.findAll().size();

        // Create the Andre
        AndreDTO andreDTO = andreMapper.andreToAndreDTO(andre);

        restAndreMockMvc.perform(post("/api/andres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(andreDTO)))
                .andExpect(status().isCreated());

        // Validate the Andre in the database
        List<Andre> andres = andreRepository.findAll();
        assertThat(andres).hasSize(databaseSizeBeforeCreate + 1);
        Andre testAndre = andres.get(andres.size() - 1);
        assertThat(testAndre.getUser_name()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testAndre.getPasswd()).isEqualTo(DEFAULT_PASSWD);
    }

    @Test
    @Transactional
    public void checkUser_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = andreRepository.findAll().size();
        // set the field null
        andre.setUser_name(null);

        // Create the Andre, which fails.
        AndreDTO andreDTO = andreMapper.andreToAndreDTO(andre);

        restAndreMockMvc.perform(post("/api/andres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(andreDTO)))
                .andExpect(status().isBadRequest());

        List<Andre> andres = andreRepository.findAll();
        assertThat(andres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAndres() throws Exception {
        // Initialize the database
        andreRepository.saveAndFlush(andre);

        // Get all the andres
        restAndreMockMvc.perform(get("/api/andres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(andre.getId().intValue())))
                .andExpect(jsonPath("$.[*].user_name").value(hasItem(DEFAULT_USER_NAME.toString())))
                .andExpect(jsonPath("$.[*].passwd").value(hasItem(DEFAULT_PASSWD.toString())));
    }

    @Test
    @Transactional
    public void getAndre() throws Exception {
        // Initialize the database
        andreRepository.saveAndFlush(andre);

        // Get the andre
        restAndreMockMvc.perform(get("/api/andres/{id}", andre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(andre.getId().intValue()))
            .andExpect(jsonPath("$.user_name").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.passwd").value(DEFAULT_PASSWD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAndre() throws Exception {
        // Get the andre
        restAndreMockMvc.perform(get("/api/andres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAndre() throws Exception {
        // Initialize the database
        andreRepository.saveAndFlush(andre);
        int databaseSizeBeforeUpdate = andreRepository.findAll().size();

        // Update the andre
        Andre updatedAndre = andreRepository.findOne(andre.getId());
        updatedAndre
                .user_name(UPDATED_USER_NAME)
                .passwd(UPDATED_PASSWD);
        AndreDTO andreDTO = andreMapper.andreToAndreDTO(updatedAndre);

        restAndreMockMvc.perform(put("/api/andres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(andreDTO)))
                .andExpect(status().isOk());

        // Validate the Andre in the database
        List<Andre> andres = andreRepository.findAll();
        assertThat(andres).hasSize(databaseSizeBeforeUpdate);
        Andre testAndre = andres.get(andres.size() - 1);
        assertThat(testAndre.getUser_name()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testAndre.getPasswd()).isEqualTo(UPDATED_PASSWD);
    }

    @Test
    @Transactional
    public void deleteAndre() throws Exception {
        // Initialize the database
        andreRepository.saveAndFlush(andre);
        int databaseSizeBeforeDelete = andreRepository.findAll().size();

        // Get the andre
        restAndreMockMvc.perform(delete("/api/andres/{id}", andre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Andre> andres = andreRepository.findAll();
        assertThat(andres).hasSize(databaseSizeBeforeDelete - 1);
    }
}
