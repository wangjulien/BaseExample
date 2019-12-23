package com.centreon.map.web.rest;

import com.centreon.map.BaseExampleApp;
import com.centreon.map.domain.View;
import com.centreon.map.repository.ViewRepository;
import com.centreon.map.service.ViewService;
import com.centreon.map.service.dto.ViewDTO;
import com.centreon.map.service.mapper.ViewMapper;
import com.centreon.map.web.rest.errors.ExceptionTranslator;
import com.centreon.map.service.dto.ViewCriteria;
import com.centreon.map.service.ViewQueryService;

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
import java.util.List;

import static com.centreon.map.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ViewResource} REST controller.
 */
@SpringBootTest(classes = BaseExampleApp.class)
public class ViewResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LOCKED = false;
    private static final Boolean UPDATED_IS_LOCKED = true;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private ViewMapper viewMapper;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ViewQueryService viewQueryService;

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

    private MockMvc restViewMockMvc;

    private View view;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ViewResource viewResource = new ViewResource(viewService, viewQueryService);
        this.restViewMockMvc = MockMvcBuilders.standaloneSetup(viewResource)
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
    public static View createEntity(EntityManager em) {
        View view = new View()
            .title(DEFAULT_TITLE)
            .isLocked(DEFAULT_IS_LOCKED);
        return view;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static View createUpdatedEntity(EntityManager em) {
        View view = new View()
            .title(UPDATED_TITLE)
            .isLocked(UPDATED_IS_LOCKED);
        return view;
    }

    @BeforeEach
    public void initTest() {
        view = createEntity(em);
    }

    @Test
    @Transactional
    public void createView() throws Exception {
        int databaseSizeBeforeCreate = viewRepository.findAll().size();

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);
        restViewMockMvc.perform(post("/api/views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isCreated());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeCreate + 1);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testView.isIsLocked()).isEqualTo(DEFAULT_IS_LOCKED);
    }

    @Test
    @Transactional
    public void createViewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = viewRepository.findAll().size();

        // Create the View with an existing ID
        view.setId(1L);
        ViewDTO viewDTO = viewMapper.toDto(view);

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewMockMvc.perform(post("/api/views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewRepository.findAll().size();
        // set the field null
        view.setTitle(null);

        // Create the View, which fails.
        ViewDTO viewDTO = viewMapper.toDto(view);

        restViewMockMvc.perform(post("/api/views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isBadRequest());

        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsLockedIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewRepository.findAll().size();
        // set the field null
        view.setIsLocked(null);

        // Create the View, which fails.
        ViewDTO viewDTO = viewMapper.toDto(view);

        restViewMockMvc.perform(post("/api/views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isBadRequest());

        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllViews() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList
        restViewMockMvc.perform(get("/api/views?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(view.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isLocked").value(hasItem(DEFAULT_IS_LOCKED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get the view
        restViewMockMvc.perform(get("/api/views/{id}", view.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(view.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.isLocked").value(DEFAULT_IS_LOCKED.booleanValue()));
    }


    @Test
    @Transactional
    public void getViewsByIdFiltering() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        Long id = view.getId();

        defaultViewShouldBeFound("id.equals=" + id);
        defaultViewShouldNotBeFound("id.notEquals=" + id);

        defaultViewShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultViewShouldNotBeFound("id.greaterThan=" + id);

        defaultViewShouldBeFound("id.lessThanOrEqual=" + id);
        defaultViewShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllViewsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where title equals to DEFAULT_TITLE
        defaultViewShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the viewList where title equals to UPDATED_TITLE
        defaultViewShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllViewsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where title not equals to DEFAULT_TITLE
        defaultViewShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the viewList where title not equals to UPDATED_TITLE
        defaultViewShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllViewsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultViewShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the viewList where title equals to UPDATED_TITLE
        defaultViewShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllViewsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where title is not null
        defaultViewShouldBeFound("title.specified=true");

        // Get all the viewList where title is null
        defaultViewShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllViewsByTitleContainsSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where title contains DEFAULT_TITLE
        defaultViewShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the viewList where title contains UPDATED_TITLE
        defaultViewShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllViewsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where title does not contain DEFAULT_TITLE
        defaultViewShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the viewList where title does not contain UPDATED_TITLE
        defaultViewShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllViewsByIsLockedIsEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where isLocked equals to DEFAULT_IS_LOCKED
        defaultViewShouldBeFound("isLocked.equals=" + DEFAULT_IS_LOCKED);

        // Get all the viewList where isLocked equals to UPDATED_IS_LOCKED
        defaultViewShouldNotBeFound("isLocked.equals=" + UPDATED_IS_LOCKED);
    }

    @Test
    @Transactional
    public void getAllViewsByIsLockedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where isLocked not equals to DEFAULT_IS_LOCKED
        defaultViewShouldNotBeFound("isLocked.notEquals=" + DEFAULT_IS_LOCKED);

        // Get all the viewList where isLocked not equals to UPDATED_IS_LOCKED
        defaultViewShouldBeFound("isLocked.notEquals=" + UPDATED_IS_LOCKED);
    }

    @Test
    @Transactional
    public void getAllViewsByIsLockedIsInShouldWork() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where isLocked in DEFAULT_IS_LOCKED or UPDATED_IS_LOCKED
        defaultViewShouldBeFound("isLocked.in=" + DEFAULT_IS_LOCKED + "," + UPDATED_IS_LOCKED);

        // Get all the viewList where isLocked equals to UPDATED_IS_LOCKED
        defaultViewShouldNotBeFound("isLocked.in=" + UPDATED_IS_LOCKED);
    }

    @Test
    @Transactional
    public void getAllViewsByIsLockedIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where isLocked is not null
        defaultViewShouldBeFound("isLocked.specified=true");

        // Get all the viewList where isLocked is null
        defaultViewShouldNotBeFound("isLocked.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultViewShouldBeFound(String filter) throws Exception {
        restViewMockMvc.perform(get("/api/views?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(view.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isLocked").value(hasItem(DEFAULT_IS_LOCKED.booleanValue())));

        // Check, that the count call also returns 1
        restViewMockMvc.perform(get("/api/views/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultViewShouldNotBeFound(String filter) throws Exception {
        restViewMockMvc.perform(get("/api/views?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restViewMockMvc.perform(get("/api/views/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingView() throws Exception {
        // Get the view
        restViewMockMvc.perform(get("/api/views/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        int databaseSizeBeforeUpdate = viewRepository.findAll().size();

        // Update the view
        View updatedView = viewRepository.findById(view.getId()).get();
        // Disconnect from session so that the updates on updatedView are not directly saved in db
        em.detach(updatedView);
        updatedView
            .title(UPDATED_TITLE)
            .isLocked(UPDATED_IS_LOCKED);
        ViewDTO viewDTO = viewMapper.toDto(updatedView);

        restViewMockMvc.perform(put("/api/views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isOk());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testView.isIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
    }

    @Test
    @Transactional
    public void updateNonExistingView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewMockMvc.perform(put("/api/views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        int databaseSizeBeforeDelete = viewRepository.findAll().size();

        // Delete the view
        restViewMockMvc.perform(delete("/api/views/{id}", view.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
