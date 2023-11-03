package fr.uga.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.IntegrationTest;
import fr.uga.domain.Ehpad;
import fr.uga.repository.EhpadRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EhpadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EhpadResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ehpads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EhpadRepository ehpadRepository;

    @Mock
    private EhpadRepository ehpadRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEhpadMockMvc;

    private Ehpad ehpad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ehpad createEntity(EntityManager em) {
        Ehpad ehpad = new Ehpad().nom(DEFAULT_NOM);
        return ehpad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ehpad createUpdatedEntity(EntityManager em) {
        Ehpad ehpad = new Ehpad().nom(UPDATED_NOM);
        return ehpad;
    }

    @BeforeEach
    public void initTest() {
        ehpad = createEntity(em);
    }

    @Test
    @Transactional
    void createEhpad() throws Exception {
        int databaseSizeBeforeCreate = ehpadRepository.findAll().size();
        // Create the Ehpad
        restEhpadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ehpad)))
            .andExpect(status().isCreated());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeCreate + 1);
        Ehpad testEhpad = ehpadList.get(ehpadList.size() - 1);
        assertThat(testEhpad.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createEhpadWithExistingId() throws Exception {
        // Create the Ehpad with an existing ID
        ehpad.setId(1L);

        int databaseSizeBeforeCreate = ehpadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEhpadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ehpad)))
            .andExpect(status().isBadRequest());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEhpads() throws Exception {
        // Initialize the database
        ehpadRepository.saveAndFlush(ehpad);

        // Get all the ehpadList
        restEhpadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ehpad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEhpadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ehpadRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEhpadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ehpadRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEhpadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ehpadRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEhpadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ehpadRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEhpad() throws Exception {
        // Initialize the database
        ehpadRepository.saveAndFlush(ehpad);

        // Get the ehpad
        restEhpadMockMvc
            .perform(get(ENTITY_API_URL_ID, ehpad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ehpad.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingEhpad() throws Exception {
        // Get the ehpad
        restEhpadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEhpad() throws Exception {
        // Initialize the database
        ehpadRepository.saveAndFlush(ehpad);

        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();

        // Update the ehpad
        Ehpad updatedEhpad = ehpadRepository.findById(ehpad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEhpad are not directly saved in db
        em.detach(updatedEhpad);
        updatedEhpad.nom(UPDATED_NOM);

        restEhpadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEhpad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEhpad))
            )
            .andExpect(status().isOk());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
        Ehpad testEhpad = ehpadList.get(ehpadList.size() - 1);
        assertThat(testEhpad.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingEhpad() throws Exception {
        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();
        ehpad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEhpadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ehpad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ehpad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEhpad() throws Exception {
        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();
        ehpad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEhpadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ehpad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEhpad() throws Exception {
        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();
        ehpad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEhpadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ehpad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEhpadWithPatch() throws Exception {
        // Initialize the database
        ehpadRepository.saveAndFlush(ehpad);

        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();

        // Update the ehpad using partial update
        Ehpad partialUpdatedEhpad = new Ehpad();
        partialUpdatedEhpad.setId(ehpad.getId());

        partialUpdatedEhpad.nom(UPDATED_NOM);

        restEhpadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEhpad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEhpad))
            )
            .andExpect(status().isOk());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
        Ehpad testEhpad = ehpadList.get(ehpadList.size() - 1);
        assertThat(testEhpad.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateEhpadWithPatch() throws Exception {
        // Initialize the database
        ehpadRepository.saveAndFlush(ehpad);

        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();

        // Update the ehpad using partial update
        Ehpad partialUpdatedEhpad = new Ehpad();
        partialUpdatedEhpad.setId(ehpad.getId());

        partialUpdatedEhpad.nom(UPDATED_NOM);

        restEhpadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEhpad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEhpad))
            )
            .andExpect(status().isOk());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
        Ehpad testEhpad = ehpadList.get(ehpadList.size() - 1);
        assertThat(testEhpad.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingEhpad() throws Exception {
        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();
        ehpad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEhpadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ehpad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ehpad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEhpad() throws Exception {
        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();
        ehpad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEhpadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ehpad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEhpad() throws Exception {
        int databaseSizeBeforeUpdate = ehpadRepository.findAll().size();
        ehpad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEhpadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ehpad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ehpad in the database
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEhpad() throws Exception {
        // Initialize the database
        ehpadRepository.saveAndFlush(ehpad);

        int databaseSizeBeforeDelete = ehpadRepository.findAll().size();

        // Delete the ehpad
        restEhpadMockMvc
            .perform(delete(ENTITY_API_URL_ID, ehpad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ehpad> ehpadList = ehpadRepository.findAll();
        assertThat(ehpadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
