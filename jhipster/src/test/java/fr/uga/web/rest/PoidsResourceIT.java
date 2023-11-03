package fr.uga.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.IntegrationTest;
import fr.uga.domain.Poids;
import fr.uga.repository.PoidsRepository;
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
 * Integration tests for the {@link PoidsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoidsResourceIT {

    private static final Float DEFAULT_POIDS = 1F;
    private static final Float UPDATED_POIDS = 2F;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/poids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoidsRepository poidsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoidsMockMvc;

    private Poids poids;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poids createEntity(EntityManager em) {
        Poids poids = new Poids().poids(DEFAULT_POIDS).date(DEFAULT_DATE);
        return poids;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poids createUpdatedEntity(EntityManager em) {
        Poids poids = new Poids().poids(UPDATED_POIDS).date(UPDATED_DATE);
        return poids;
    }

    @BeforeEach
    public void initTest() {
        poids = createEntity(em);
    }

    @Test
    @Transactional
    void createPoids() throws Exception {
        int databaseSizeBeforeCreate = poidsRepository.findAll().size();
        // Create the Poids
        restPoidsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poids)))
            .andExpect(status().isCreated());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeCreate + 1);
        Poids testPoids = poidsList.get(poidsList.size() - 1);
        assertThat(testPoids.getPoids()).isEqualTo(DEFAULT_POIDS);
        assertThat(testPoids.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createPoidsWithExistingId() throws Exception {
        // Create the Poids with an existing ID
        poids.setId(1L);

        int databaseSizeBeforeCreate = poidsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoidsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poids)))
            .andExpect(status().isBadRequest());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPoids() throws Exception {
        // Initialize the database
        poidsRepository.saveAndFlush(poids);

        // Get all the poidsList
        restPoidsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poids.getId().intValue())))
            .andExpect(jsonPath("$.[*].poids").value(hasItem(DEFAULT_POIDS.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPoids() throws Exception {
        // Initialize the database
        poidsRepository.saveAndFlush(poids);

        // Get the poids
        restPoidsMockMvc
            .perform(get(ENTITY_API_URL_ID, poids.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poids.getId().intValue()))
            .andExpect(jsonPath("$.poids").value(DEFAULT_POIDS.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPoids() throws Exception {
        // Get the poids
        restPoidsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPoids() throws Exception {
        // Initialize the database
        poidsRepository.saveAndFlush(poids);

        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();

        // Update the poids
        Poids updatedPoids = poidsRepository.findById(poids.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPoids are not directly saved in db
        em.detach(updatedPoids);
        updatedPoids.poids(UPDATED_POIDS).date(UPDATED_DATE);

        restPoidsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPoids.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPoids))
            )
            .andExpect(status().isOk());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
        Poids testPoids = poidsList.get(poidsList.size() - 1);
        assertThat(testPoids.getPoids()).isEqualTo(UPDATED_POIDS);
        assertThat(testPoids.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPoids() throws Exception {
        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();
        poids.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoidsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poids.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poids))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoids() throws Exception {
        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();
        poids.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoidsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poids))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoids() throws Exception {
        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();
        poids.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoidsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poids)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoidsWithPatch() throws Exception {
        // Initialize the database
        poidsRepository.saveAndFlush(poids);

        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();

        // Update the poids using partial update
        Poids partialUpdatedPoids = new Poids();
        partialUpdatedPoids.setId(poids.getId());

        partialUpdatedPoids.poids(UPDATED_POIDS);

        restPoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoids.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoids))
            )
            .andExpect(status().isOk());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
        Poids testPoids = poidsList.get(poidsList.size() - 1);
        assertThat(testPoids.getPoids()).isEqualTo(UPDATED_POIDS);
        assertThat(testPoids.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePoidsWithPatch() throws Exception {
        // Initialize the database
        poidsRepository.saveAndFlush(poids);

        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();

        // Update the poids using partial update
        Poids partialUpdatedPoids = new Poids();
        partialUpdatedPoids.setId(poids.getId());

        partialUpdatedPoids.poids(UPDATED_POIDS).date(UPDATED_DATE);

        restPoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoids.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoids))
            )
            .andExpect(status().isOk());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
        Poids testPoids = poidsList.get(poidsList.size() - 1);
        assertThat(testPoids.getPoids()).isEqualTo(UPDATED_POIDS);
        assertThat(testPoids.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPoids() throws Exception {
        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();
        poids.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poids.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poids))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoids() throws Exception {
        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();
        poids.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poids))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoids() throws Exception {
        int databaseSizeBeforeUpdate = poidsRepository.findAll().size();
        poids.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoidsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poids)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poids in the database
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoids() throws Exception {
        // Initialize the database
        poidsRepository.saveAndFlush(poids);

        int databaseSizeBeforeDelete = poidsRepository.findAll().size();

        // Delete the poids
        restPoidsMockMvc
            .perform(delete(ENTITY_API_URL_ID, poids.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Poids> poidsList = poidsRepository.findAll();
        assertThat(poidsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
