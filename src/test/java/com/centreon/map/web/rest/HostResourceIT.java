package com.centreon.map.web.rest;

import com.centreon.map.BaseExampleApp;
import com.centreon.map.domain.Host;
import com.centreon.map.repository.HostRepository;
import com.centreon.map.service.HostService;
import com.centreon.map.service.dto.HostDTO;
import com.centreon.map.service.mapper.HostMapper;
import com.centreon.map.web.rest.errors.ExceptionTranslator;

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

import com.centreon.map.domain.enumeration.Status;
/**
 * Integration tests for the {@link HostResource} REST controller.
 */
@SpringBootTest(classes = BaseExampleApp.class)
public class HostResourceIT {

    private static final String DEFAULT_HOST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HOST_NAME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.UP;
    private static final Status UPDATED_STATUS = Status.DOWN;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private HostMapper hostMapper;

    @Autowired
    private HostService hostService;

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

    private MockMvc restHostMockMvc;

    private Host host;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HostResource hostResource = new HostResource(hostService);
        this.restHostMockMvc = MockMvcBuilders.standaloneSetup(hostResource)
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
    public static Host createEntity(EntityManager em) {
        Host host = new Host()
            .hostName(DEFAULT_HOST_NAME)
            .status(DEFAULT_STATUS);
        return host;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Host createUpdatedEntity(EntityManager em) {
        Host host = new Host()
            .hostName(UPDATED_HOST_NAME)
            .status(UPDATED_STATUS);
        return host;
    }

    @BeforeEach
    public void initTest() {
        host = createEntity(em);
    }

    @Test
    @Transactional
    public void createHost() throws Exception {
        int databaseSizeBeforeCreate = hostRepository.findAll().size();

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);
        restHostMockMvc.perform(post("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isCreated());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeCreate + 1);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostName()).isEqualTo(DEFAULT_HOST_NAME);
        assertThat(testHost.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createHostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hostRepository.findAll().size();

        // Create the Host with an existing ID
        host.setId(1L);
        HostDTO hostDTO = hostMapper.toDto(host);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHostMockMvc.perform(post("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkHostNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hostRepository.findAll().size();
        // set the field null
        host.setHostName(null);

        // Create the Host, which fails.
        HostDTO hostDTO = hostMapper.toDto(host);

        restHostMockMvc.perform(post("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isBadRequest());

        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHosts() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        // Get all the hostList
        restHostMockMvc.perform(get("/api/hosts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(host.getId().intValue())))
            .andExpect(jsonPath("$.[*].hostName").value(hasItem(DEFAULT_HOST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        // Get the host
        restHostMockMvc.perform(get("/api/hosts/{id}", host.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(host.getId().intValue()))
            .andExpect(jsonPath("$.hostName").value(DEFAULT_HOST_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHost() throws Exception {
        // Get the host
        restHostMockMvc.perform(get("/api/hosts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeUpdate = hostRepository.findAll().size();

        // Update the host
        Host updatedHost = hostRepository.findById(host.getId()).get();
        // Disconnect from session so that the updates on updatedHost are not directly saved in db
        em.detach(updatedHost);
        updatedHost
            .hostName(UPDATED_HOST_NAME)
            .status(UPDATED_STATUS);
        HostDTO hostDTO = hostMapper.toDto(updatedHost);

        restHostMockMvc.perform(put("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isOk());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostName()).isEqualTo(UPDATED_HOST_NAME);
        assertThat(testHost.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostMockMvc.perform(put("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeDelete = hostRepository.findAll().size();

        // Delete the host
        restHostMockMvc.perform(delete("/api/hosts/{id}", host.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
