package fr.uga.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.IntegrationTest;
import fr.uga.domain.EPA;
import fr.uga.repository.EPARepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EPAResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EPAResourceIT {

    private static final Float DEFAULT_EPA = 1F;
    private static final Float UPDATED_EPA = 2F;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/epas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EPARepository ePARepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEPAMockMvc;

    private EPA ePA;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPA createEntity(EntityManager em) {
        EPA ePA = new EPA().epa(DEFAULT_EPA).date(DEFAULT_DATE);
        return ePA;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPA createUpdatedEntity(EntityManager em) {
        EPA ePA = new EPA().epa(UPDATED_EPA).date(UPDATED_DATE);
        return ePA;
    }

    @BeforeEach
    public void initTest() {
        ePA = createEntity(em);
    }

    @Test
    @Transactional
    void createEPA() throws Exception {
        int databaseSizeBeforeCreate = ePARepository.findAll().size();
        // Create the EPA
        restEPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePA)))
            .andExpect(status().isCreated());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeCreate + 1);
        EPA testEPA = ePAList.get(ePAList.size() - 1);
        assertThat(testEPA.getEpa()).isEqualTo(DEFAULT_EPA);
        assertThat(testEPA.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createEPAWithExistingId() throws Exception {
        // Create the EPA with an existing ID
        ePA.setId(1L);

        int databaseSizeBeforeCreate = ePARepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePA)))
            .andExpect(status().isBadRequest());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEPAS() throws Exception {
        // Initialize the database
        ePARepository.saveAndFlush(ePA);

        // Get all the ePAList
        restEPAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePA.getId().intValue())))
            .andExpect(jsonPath("$.[*].epa").value(hasItem(DEFAULT_EPA.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getEPA() throws Exception {
        // Initialize the database
        ePARepository.saveAndFlush(ePA);

        // Get the ePA
        restEPAMockMvc
            .perform(get(ENTITY_API_URL_ID, ePA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ePA.getId().intValue()))
            .andExpect(jsonPath("$.epa").value(DEFAULT_EPA.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEPA() throws Exception {
        // Get the ePA
        restEPAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEPA() throws Exception {
        // Initialize the database
        ePARepository.saveAndFlush(ePA);

        int databaseSizeBeforeUpdate = ePARepository.findAll().size();

        // Update the ePA
        EPA updatedEPA = ePARepository.findById(ePA.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEPA are not directly saved in db
        em.detach(updatedEPA);
        updatedEPA.epa(UPDATED_EPA).date(UPDATED_DATE);

        restEPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEPA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEPA))
            )
            .andExpect(status().isOk());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
        EPA testEPA = ePAList.get(ePAList.size() - 1);
        assertThat(testEPA.getEpa()).isEqualTo(UPDATED_EPA);
        assertThat(testEPA.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEPA() throws Exception {
        int databaseSizeBeforeUpdate = ePARepository.findAll().size();
        ePA.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ePA.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePA))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEPA() throws Exception {
        int databaseSizeBeforeUpdate = ePARepository.findAll().size();
        ePA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePA))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEPA() throws Exception {
        int databaseSizeBeforeUpdate = ePARepository.findAll().size();
        ePA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPAMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEPAWithPatch() throws Exception {
        // Initialize the database
        ePARepository.saveAndFlush(ePA);

        int databaseSizeBeforeUpdate = ePARepository.findAll().size();

        // Update the ePA using partial update
        EPA partialUpdatedEPA = new EPA();
        partialUpdatedEPA.setId(ePA.getId());

        partialUpdatedEPA.date(UPDATED_DATE);

        restEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPA))
            )
            .andExpect(status().isOk());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
        EPA testEPA = ePAList.get(ePAList.size() - 1);
        assertThat(testEPA.getEpa()).isEqualTo(DEFAULT_EPA);
        assertThat(testEPA.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEPAWithPatch() throws Exception {
        // Initialize the database
        ePARepository.saveAndFlush(ePA);

        int databaseSizeBeforeUpdate = ePARepository.findAll().size();

        // Update the ePA using partial update
        EPA partialUpdatedEPA = new EPA();
        partialUpdatedEPA.setId(ePA.getId());

        partialUpdatedEPA.epa(UPDATED_EPA).date(UPDATED_DATE);

        restEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPA))
            )
            .andExpect(status().isOk());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
        EPA testEPA = ePAList.get(ePAList.size() - 1);
        assertThat(testEPA.getEpa()).isEqualTo(UPDATED_EPA);
        assertThat(testEPA.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEPA() throws Exception {
        int databaseSizeBeforeUpdate = ePARepository.findAll().size();
        ePA.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ePA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePA))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEPA() throws Exception {
        int databaseSizeBeforeUpdate = ePARepository.findAll().size();
        ePA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePA))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEPA() throws Exception {
        int databaseSizeBeforeUpdate = ePARepository.findAll().size();
        ePA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPAMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ePA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPA in the database
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEPA() throws Exception {
        // Initialize the database
        ePARepository.saveAndFlush(ePA);

        int databaseSizeBeforeDelete = ePARepository.findAll().size();

        // Delete the ePA
        restEPAMockMvc.perform(delete(ENTITY_API_URL_ID, ePA.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EPA> ePAList = ePARepository.findAll();
        assertThat(ePAList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
