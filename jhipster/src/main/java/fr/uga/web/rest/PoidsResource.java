package fr.uga.web.rest;

import fr.uga.domain.Poids;
import fr.uga.repository.PoidsRepository;
import fr.uga.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.uga.domain.Poids}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PoidsResource {

    private final Logger log = LoggerFactory.getLogger(PoidsResource.class);

    private static final String ENTITY_NAME = "poids";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoidsRepository poidsRepository;

    public PoidsResource(PoidsRepository poidsRepository) {
        this.poidsRepository = poidsRepository;
    }

    /**
     * {@code POST  /poids} : Create a new poids.
     *
     * @param poids the poids to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poids, or with status {@code 400 (Bad Request)} if the poids has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/poids")
    public ResponseEntity<Poids> createPoids(@RequestBody Poids poids) throws URISyntaxException {
        log.debug("REST request to save Poids : {}", poids);
        if (poids.getId() != null) {
            throw new BadRequestAlertException("A new poids cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Poids result = poidsRepository.save(poids);
        return ResponseEntity
            .created(new URI("/api/poids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poids/:id} : Updates an existing poids.
     *
     * @param id the id of the poids to save.
     * @param poids the poids to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poids,
     * or with status {@code 400 (Bad Request)} if the poids is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poids couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/poids/{id}")
    public ResponseEntity<Poids> updatePoids(@PathVariable(value = "id", required = false) final Long id, @RequestBody Poids poids)
        throws URISyntaxException {
        log.debug("REST request to update Poids : {}, {}", id, poids);
        if (poids.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poids.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poidsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Poids result = poidsRepository.save(poids);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poids.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poids/:id} : Partial updates given fields of an existing poids, field will ignore if it is null
     *
     * @param id the id of the poids to save.
     * @param poids the poids to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poids,
     * or with status {@code 400 (Bad Request)} if the poids is not valid,
     * or with status {@code 404 (Not Found)} if the poids is not found,
     * or with status {@code 500 (Internal Server Error)} if the poids couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/poids/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Poids> partialUpdatePoids(@PathVariable(value = "id", required = false) final Long id, @RequestBody Poids poids)
        throws URISyntaxException {
        log.debug("REST request to partial update Poids partially : {}, {}", id, poids);
        if (poids.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poids.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poidsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Poids> result = poidsRepository
            .findById(poids.getId())
            .map(existingPoids -> {
                if (poids.getPoids() != null) {
                    existingPoids.setPoids(poids.getPoids());
                }
                if (poids.getDate() != null) {
                    existingPoids.setDate(poids.getDate());
                }

                return existingPoids;
            })
            .map(poidsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poids.getId().toString())
        );
    }

    /**
     * {@code GET  /poids} : get all the poids.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of poids in body.
     */
    @GetMapping("/poids")
    public List<Poids> getAllPoids() {
        log.debug("REST request to get all Poids");
        return poidsRepository.findAll();
    }

    /**
     * {@code GET  /poids/:id} : get the "id" poids.
     *
     * @param id the id of the poids to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poids, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/poids/{id}")
    public ResponseEntity<Poids> getPoids(@PathVariable Long id) {
        log.debug("REST request to get Poids : {}", id);
        Optional<Poids> poids = poidsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(poids);
    }

    /**
     * {@code GET /poids/patient/:id} : get all the poids of a patient.
     * 
     * @param id the id of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of poids in body.
     */
    @GetMapping("/poids/patient/{id}")
    public List<Poids> getAllPoidsByPatient(@PathVariable Long id) {
        log.debug("REST request to get all Poids of a patient");
        return poidsRepository.findByPatientIdOrderByDateAsc(id);
    }

    /**
     * {@code DELETE  /poids/:id} : delete the "id" poids.
     *
     * @param id the id of the poids to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/poids/{id}")
    public ResponseEntity<Void> deletePoids(@PathVariable Long id) {
        log.debug("REST request to delete Poids : {}", id);
        poidsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
