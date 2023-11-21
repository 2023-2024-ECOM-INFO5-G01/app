package fr.uga.web.rest;

import fr.uga.domain.EPA;
import fr.uga.repository.EPARepository;
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
 * REST controller for managing {@link fr.uga.domain.EPA}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EPAResource {

    private final Logger log = LoggerFactory.getLogger(EPAResource.class);

    private static final String ENTITY_NAME = "ePA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EPARepository ePARepository;

    public EPAResource(EPARepository ePARepository) {
        this.ePARepository = ePARepository;
    }

    /**
     * {@code POST  /epas} : Create a new ePA.
     *
     * @param ePA the ePA to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ePA, or with status {@code 400 (Bad Request)} if the ePA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/epas")
    public ResponseEntity<EPA> createEPA(@RequestBody EPA ePA) throws URISyntaxException {
        log.debug("REST request to save EPA : {}", ePA);
        if (ePA.getId() != null) {
            throw new BadRequestAlertException("A new ePA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EPA result = ePARepository.save(ePA);
        return ResponseEntity
            .created(new URI("/api/epas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /epas/:id} : Updates an existing ePA.
     *
     * @param id the id of the ePA to save.
     * @param ePA the ePA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePA,
     * or with status {@code 400 (Bad Request)} if the ePA is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ePA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/epas/{id}")
    public ResponseEntity<EPA> updateEPA(@PathVariable(value = "id", required = false) final Long id, @RequestBody EPA ePA)
        throws URISyntaxException {
        log.debug("REST request to update EPA : {}, {}", id, ePA);
        if (ePA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EPA result = ePARepository.save(ePA);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePA.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /epas/:id} : Partial updates given fields of an existing ePA, field will ignore if it is null
     *
     * @param id the id of the ePA to save.
     * @param ePA the ePA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePA,
     * or with status {@code 400 (Bad Request)} if the ePA is not valid,
     * or with status {@code 404 (Not Found)} if the ePA is not found,
     * or with status {@code 500 (Internal Server Error)} if the ePA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/epas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EPA> partialUpdateEPA(@PathVariable(value = "id", required = false) final Long id, @RequestBody EPA ePA)
        throws URISyntaxException {
        log.debug("REST request to partial update EPA partially : {}, {}", id, ePA);
        if (ePA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EPA> result = ePARepository
            .findById(ePA.getId())
            .map(existingEPA -> {
                if (ePA.getEpa() != null) {
                    existingEPA.setEpa(ePA.getEpa());
                }
                if (ePA.getDate() != null) {
                    existingEPA.setDate(ePA.getDate());
                }

                return existingEPA;
            })
            .map(ePARepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePA.getId().toString())
        );
    }

    /**
     * {@code GET  /epas} : get all the ePAS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ePAS in body.
     */
    @GetMapping("/epas")
    public List<EPA> getAllEPAS() {
        log.debug("REST request to get all EPAS");
        return ePARepository.findAll();
    }

    /**
     * {@code GET  /epas/:id} : get the "id" ePA.
     *
     * @param id the id of the ePA to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ePA, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/epas/{id}")
    public ResponseEntity<EPA> getEPA(@PathVariable Long id) {
        log.debug("REST request to get EPA : {}", id);
        Optional<EPA> ePA = ePARepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ePA);
    }

    /**
     * {@code GET /epas/patient/:id} : get all the epa of a patient by id sorted by date.
     * @param id the id of the patient.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of epas in body.
     */
    @GetMapping("/epas/patient/{id}")
    public List<EPA> getAllEPASByPatientId(@PathVariable Long id) {
        log.debug("REST request to get all EPAS of patient : {}", id);
        return ePARepository.findByPatientIdOrderByDateDesc(id);
    }

    /**
     * {@code DELETE  /epas/:id} : delete the "id" ePA.
     *
     * @param id the id of the ePA to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/epas/{id}")
    public ResponseEntity<Void> deleteEPA(@PathVariable Long id) {
        log.debug("REST request to delete EPA : {}", id);
        ePARepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
