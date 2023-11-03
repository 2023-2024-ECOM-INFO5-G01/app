package fr.uga.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.IntegrationTest;
import fr.uga.domain.IMC;
import fr.uga.repository.IMCRepository;
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
 * Integration tests for the {@link IMCResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IMCResourceIT {

    private static final Float DEFAULT_IMC = 1F;
    private static final Float UPDATED_IMC = 2F;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/imcs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IMCRepository iMCRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIMCMockMvc;

    private IMC iMC;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IMC createEntity(EntityManager em) {
        IMC iMC = new IMC().imc(DEFAULT_IMC).date(DEFAULT_DATE);
        return iMC;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IMC createUpdatedEntity(EntityManager em) {
        IMC iMC = new IMC().imc(UPDATED_IMC).date(UPDATED_DATE);
        return iMC;
    }

    @BeforeEach
    public void initTest() {
        iMC = createEntity(em);
    }

    @Test
    @Transactional
    void createIMC() throws Exception {
        int databaseSizeBeforeCreate = iMCRepository.findAll().size();
        // Create the IMC
        restIMCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iMC)))
            .andExpect(status().isCreated());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeCreate + 1);
        IMC testIMC = iMCList.get(iMCList.size() - 1);
        assertThat(testIMC.getImc()).isEqualTo(DEFAULT_IMC);
        assertThat(testIMC.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createIMCWithExistingId() throws Exception {
        // Create the IMC with an existing ID
        iMC.setId(1L);

        int databaseSizeBeforeCreate = iMCRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIMCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iMC)))
            .andExpect(status().isBadRequest());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIMCS() throws Exception {
        // Initialize the database
        iMCRepository.saveAndFlush(iMC);

        // Get all the iMCList
        restIMCMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iMC.getId().intValue())))
            .andExpect(jsonPath("$.[*].imc").value(hasItem(DEFAULT_IMC.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getIMC() throws Exception {
        // Initialize the database
        iMCRepository.saveAndFlush(iMC);

        // Get the iMC
        restIMCMockMvc
            .perform(get(ENTITY_API_URL_ID, iMC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iMC.getId().intValue()))
            .andExpect(jsonPath("$.imc").value(DEFAULT_IMC.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIMC() throws Exception {
        // Get the iMC
        restIMCMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIMC() throws Exception {
        // Initialize the database
        iMCRepository.saveAndFlush(iMC);

        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();

        // Update the iMC
        IMC updatedIMC = iMCRepository.findById(iMC.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIMC are not directly saved in db
        em.detach(updatedIMC);
        updatedIMC.imc(UPDATED_IMC).date(UPDATED_DATE);

        restIMCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIMC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIMC))
            )
            .andExpect(status().isOk());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
        IMC testIMC = iMCList.get(iMCList.size() - 1);
        assertThat(testIMC.getImc()).isEqualTo(UPDATED_IMC);
        assertThat(testIMC.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingIMC() throws Exception {
        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();
        iMC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIMCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iMC.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iMC))
            )
            .andExpect(status().isBadRequest());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIMC() throws Exception {
        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();
        iMC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIMCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iMC))
            )
            .andExpect(status().isBadRequest());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIMC() throws Exception {
        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();
        iMC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIMCMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iMC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIMCWithPatch() throws Exception {
        // Initialize the database
        iMCRepository.saveAndFlush(iMC);

        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();

        // Update the iMC using partial update
        IMC partialUpdatedIMC = new IMC();
        partialUpdatedIMC.setId(iMC.getId());

        partialUpdatedIMC.imc(UPDATED_IMC);

        restIMCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIMC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIMC))
            )
            .andExpect(status().isOk());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
        IMC testIMC = iMCList.get(iMCList.size() - 1);
        assertThat(testIMC.getImc()).isEqualTo(UPDATED_IMC);
        assertThat(testIMC.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateIMCWithPatch() throws Exception {
        // Initialize the database
        iMCRepository.saveAndFlush(iMC);

        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();

        // Update the iMC using partial update
        IMC partialUpdatedIMC = new IMC();
        partialUpdatedIMC.setId(iMC.getId());

        partialUpdatedIMC.imc(UPDATED_IMC).date(UPDATED_DATE);

        restIMCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIMC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIMC))
            )
            .andExpect(status().isOk());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
        IMC testIMC = iMCList.get(iMCList.size() - 1);
        assertThat(testIMC.getImc()).isEqualTo(UPDATED_IMC);
        assertThat(testIMC.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingIMC() throws Exception {
        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();
        iMC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIMCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iMC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iMC))
            )
            .andExpect(status().isBadRequest());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIMC() throws Exception {
        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();
        iMC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIMCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iMC))
            )
            .andExpect(status().isBadRequest());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIMC() throws Exception {
        int databaseSizeBeforeUpdate = iMCRepository.findAll().size();
        iMC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIMCMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(iMC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IMC in the database
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIMC() throws Exception {
        // Initialize the database
        iMCRepository.saveAndFlush(iMC);

        int databaseSizeBeforeDelete = iMCRepository.findAll().size();

        // Delete the iMC
        restIMCMockMvc.perform(delete(ENTITY_API_URL_ID, iMC.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IMC> iMCList = iMCRepository.findAll();
        assertThat(iMCList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
