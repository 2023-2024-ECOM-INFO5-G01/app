package fr.uga.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.IntegrationTest;
import fr.uga.domain.Albumine;
import fr.uga.repository.AlbumineRepository;
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
 * Integration tests for the {@link AlbumineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlbumineResourceIT {

    private static final Float DEFAULT_ALBU = 1F;
    private static final Float UPDATED_ALBU = 2F;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/albumines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlbumineRepository albumineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlbumineMockMvc;

    private Albumine albumine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Albumine createEntity(EntityManager em) {
        Albumine albumine = new Albumine().albu(DEFAULT_ALBU).date(DEFAULT_DATE);
        return albumine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Albumine createUpdatedEntity(EntityManager em) {
        Albumine albumine = new Albumine().albu(UPDATED_ALBU).date(UPDATED_DATE);
        return albumine;
    }

    @BeforeEach
    public void initTest() {
        albumine = createEntity(em);
    }

    @Test
    @Transactional
    void createAlbumine() throws Exception {
        int databaseSizeBeforeCreate = albumineRepository.findAll().size();
        // Create the Albumine
        restAlbumineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumine)))
            .andExpect(status().isCreated());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeCreate + 1);
        Albumine testAlbumine = albumineList.get(albumineList.size() - 1);
        assertThat(testAlbumine.getAlbu()).isEqualTo(DEFAULT_ALBU);
        assertThat(testAlbumine.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createAlbumineWithExistingId() throws Exception {
        // Create the Albumine with an existing ID
        albumine.setId(1L);

        int databaseSizeBeforeCreate = albumineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumine)))
            .andExpect(status().isBadRequest());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlbumines() throws Exception {
        // Initialize the database
        albumineRepository.saveAndFlush(albumine);

        // Get all the albumineList
        restAlbumineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(albumine.getId().intValue())))
            .andExpect(jsonPath("$.[*].albu").value(hasItem(DEFAULT_ALBU.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlbumine() throws Exception {
        // Initialize the database
        albumineRepository.saveAndFlush(albumine);

        // Get the albumine
        restAlbumineMockMvc
            .perform(get(ENTITY_API_URL_ID, albumine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(albumine.getId().intValue()))
            .andExpect(jsonPath("$.albu").value(DEFAULT_ALBU.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAlbumine() throws Exception {
        // Get the albumine
        restAlbumineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlbumine() throws Exception {
        // Initialize the database
        albumineRepository.saveAndFlush(albumine);

        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();

        // Update the albumine
        Albumine updatedAlbumine = albumineRepository.findById(albumine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlbumine are not directly saved in db
        em.detach(updatedAlbumine);
        updatedAlbumine.albu(UPDATED_ALBU).date(UPDATED_DATE);

        restAlbumineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlbumine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAlbumine))
            )
            .andExpect(status().isOk());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
        Albumine testAlbumine = albumineList.get(albumineList.size() - 1);
        assertThat(testAlbumine.getAlbu()).isEqualTo(UPDATED_ALBU);
        assertThat(testAlbumine.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();
        albumine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, albumine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();
        albumine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();
        albumine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlbumineWithPatch() throws Exception {
        // Initialize the database
        albumineRepository.saveAndFlush(albumine);

        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();

        // Update the albumine using partial update
        Albumine partialUpdatedAlbumine = new Albumine();
        partialUpdatedAlbumine.setId(albumine.getId());

        restAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbumine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbumine))
            )
            .andExpect(status().isOk());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
        Albumine testAlbumine = albumineList.get(albumineList.size() - 1);
        assertThat(testAlbumine.getAlbu()).isEqualTo(DEFAULT_ALBU);
        assertThat(testAlbumine.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAlbumineWithPatch() throws Exception {
        // Initialize the database
        albumineRepository.saveAndFlush(albumine);

        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();

        // Update the albumine using partial update
        Albumine partialUpdatedAlbumine = new Albumine();
        partialUpdatedAlbumine.setId(albumine.getId());

        partialUpdatedAlbumine.albu(UPDATED_ALBU).date(UPDATED_DATE);

        restAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbumine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbumine))
            )
            .andExpect(status().isOk());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
        Albumine testAlbumine = albumineList.get(albumineList.size() - 1);
        assertThat(testAlbumine.getAlbu()).isEqualTo(UPDATED_ALBU);
        assertThat(testAlbumine.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();
        albumine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, albumine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(albumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();
        albumine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(albumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = albumineRepository.findAll().size();
        albumine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(albumine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Albumine in the database
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlbumine() throws Exception {
        // Initialize the database
        albumineRepository.saveAndFlush(albumine);

        int databaseSizeBeforeDelete = albumineRepository.findAll().size();

        // Delete the albumine
        restAlbumineMockMvc
            .perform(delete(ENTITY_API_URL_ID, albumine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Albumine> albumineList = albumineRepository.findAll();
        assertThat(albumineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
