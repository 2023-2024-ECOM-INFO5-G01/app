package fr.uga.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.IntegrationTest;
import fr.uga.domain.Aliment;
import fr.uga.repository.AlimentRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AlimentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlimentResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Float DEFAULT_CALORIES = 1F;
    private static final Float UPDATED_CALORIES = 2F;

    private static final String ENTITY_API_URL = "/api/aliments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlimentRepository alimentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlimentMockMvc;

    private Aliment aliment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aliment createEntity(EntityManager em) {
        Aliment aliment = new Aliment().nom(DEFAULT_NOM).calories(DEFAULT_CALORIES);
        return aliment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aliment createUpdatedEntity(EntityManager em) {
        Aliment aliment = new Aliment().nom(UPDATED_NOM).calories(UPDATED_CALORIES);
        return aliment;
    }

    @BeforeEach
    public void initTest() {
        aliment = createEntity(em);
    }

    @Test
    @Transactional
    void createAliment() throws Exception {
        int databaseSizeBeforeCreate = alimentRepository.findAll().size();
        // Create the Aliment
        restAlimentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aliment)))
            .andExpect(status().isCreated());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeCreate + 1);
        Aliment testAliment = alimentList.get(alimentList.size() - 1);
        assertThat(testAliment.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAliment.getCalories()).isEqualTo(DEFAULT_CALORIES);
    }

    @Test
    @Transactional
    void createAlimentWithExistingId() throws Exception {
        // Create the Aliment with an existing ID
        aliment.setId(1L);

        int databaseSizeBeforeCreate = alimentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlimentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aliment)))
            .andExpect(status().isBadRequest());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAliments() throws Exception {
        // Initialize the database
        alimentRepository.saveAndFlush(aliment);

        // Get all the alimentList
        restAlimentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aliment.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].calories").value(hasItem(DEFAULT_CALORIES.doubleValue())));
    }

    @Test
    @Transactional
    void getAliment() throws Exception {
        // Initialize the database
        alimentRepository.saveAndFlush(aliment);

        // Get the aliment
        restAlimentMockMvc
            .perform(get(ENTITY_API_URL_ID, aliment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aliment.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.calories").value(DEFAULT_CALORIES.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAliment() throws Exception {
        // Get the aliment
        restAlimentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAliment() throws Exception {
        // Initialize the database
        alimentRepository.saveAndFlush(aliment);

        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();

        // Update the aliment
        Aliment updatedAliment = alimentRepository.findById(aliment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAliment are not directly saved in db
        em.detach(updatedAliment);
        updatedAliment.nom(UPDATED_NOM).calories(UPDATED_CALORIES);

        restAlimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAliment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAliment))
            )
            .andExpect(status().isOk());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
        Aliment testAliment = alimentList.get(alimentList.size() - 1);
        assertThat(testAliment.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAliment.getCalories()).isEqualTo(UPDATED_CALORIES);
    }

    @Test
    @Transactional
    void putNonExistingAliment() throws Exception {
        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();
        aliment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aliment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aliment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAliment() throws Exception {
        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();
        aliment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlimentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aliment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAliment() throws Exception {
        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();
        aliment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlimentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aliment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlimentWithPatch() throws Exception {
        // Initialize the database
        alimentRepository.saveAndFlush(aliment);

        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();

        // Update the aliment using partial update
        Aliment partialUpdatedAliment = new Aliment();
        partialUpdatedAliment.setId(aliment.getId());

        partialUpdatedAliment.nom(UPDATED_NOM).calories(UPDATED_CALORIES);

        restAlimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAliment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAliment))
            )
            .andExpect(status().isOk());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
        Aliment testAliment = alimentList.get(alimentList.size() - 1);
        assertThat(testAliment.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAliment.getCalories()).isEqualTo(UPDATED_CALORIES);
    }

    @Test
    @Transactional
    void fullUpdateAlimentWithPatch() throws Exception {
        // Initialize the database
        alimentRepository.saveAndFlush(aliment);

        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();

        // Update the aliment using partial update
        Aliment partialUpdatedAliment = new Aliment();
        partialUpdatedAliment.setId(aliment.getId());

        partialUpdatedAliment.nom(UPDATED_NOM).calories(UPDATED_CALORIES);

        restAlimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAliment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAliment))
            )
            .andExpect(status().isOk());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
        Aliment testAliment = alimentList.get(alimentList.size() - 1);
        assertThat(testAliment.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAliment.getCalories()).isEqualTo(UPDATED_CALORIES);
    }

    @Test
    @Transactional
    void patchNonExistingAliment() throws Exception {
        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();
        aliment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aliment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aliment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAliment() throws Exception {
        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();
        aliment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlimentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aliment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAliment() throws Exception {
        int databaseSizeBeforeUpdate = alimentRepository.findAll().size();
        aliment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlimentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aliment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aliment in the database
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAliment() throws Exception {
        // Initialize the database
        alimentRepository.saveAndFlush(aliment);

        int databaseSizeBeforeDelete = alimentRepository.findAll().size();

        // Delete the aliment
        restAlimentMockMvc
            .perform(delete(ENTITY_API_URL_ID, aliment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Aliment> alimentList = alimentRepository.findAll();
        assertThat(alimentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
