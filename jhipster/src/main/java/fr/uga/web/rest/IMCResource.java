package fr.uga.web.rest;

import fr.uga.domain.IMC;
import fr.uga.repository.IMCRepository;
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
 * REST controller for managing {@link fr.uga.domain.IMC}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IMCResource {

    private final Logger log = LoggerFactory.getLogger(IMCResource.class);

    private static final String ENTITY_NAME = "iMC";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IMCRepository iMCRepository;

    public IMCResource(IMCRepository iMCRepository) {
        this.iMCRepository = iMCRepository;
    }

    /**
     * {@code POST  /imcs} : Create a new iMC.
     *
     * @param iMC the iMC to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iMC, or with status {@code 400 (Bad Request)} if the iMC has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/imcs")
    public ResponseEntity<IMC> createIMC(@RequestBody IMC iMC) throws URISyntaxException {
        log.debug("REST request to save IMC : {}", iMC);
        if (iMC.getId() != null) {
            throw new BadRequestAlertException("A new iMC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IMC result = iMCRepository.save(iMC);
        return ResponseEntity
            .created(new URI("/api/imcs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /imcs/:id} : Updates an existing iMC.
     *
     * @param id the id of the iMC to save.
     * @param iMC the iMC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iMC,
     * or with status {@code 400 (Bad Request)} if the iMC is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iMC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/imcs/{id}")
    public ResponseEntity<IMC> updateIMC(@PathVariable(value = "id", required = false) final Long id, @RequestBody IMC iMC)
        throws URISyntaxException {
        log.debug("REST request to update IMC : {}, {}", id, iMC);
        if (iMC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iMC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iMCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IMC result = iMCRepository.save(iMC);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iMC.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /imcs/:id} : Partial updates given fields of an existing iMC, field will ignore if it is null
     *
     * @param id the id of the iMC to save.
     * @param iMC the iMC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iMC,
     * or with status {@code 400 (Bad Request)} if the iMC is not valid,
     * or with status {@code 404 (Not Found)} if the iMC is not found,
     * or with status {@code 500 (Internal Server Error)} if the iMC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/imcs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IMC> partialUpdateIMC(@PathVariable(value = "id", required = false) final Long id, @RequestBody IMC iMC)
        throws URISyntaxException {
        log.debug("REST request to partial update IMC partially : {}, {}", id, iMC);
        if (iMC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iMC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iMCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IMC> result = iMCRepository
            .findById(iMC.getId())
            .map(existingIMC -> {
                if (iMC.getImc() != null) {
                    existingIMC.setImc(iMC.getImc());
                }
                if (iMC.getDate() != null) {
                    existingIMC.setDate(iMC.getDate());
                }

                return existingIMC;
            })
            .map(iMCRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iMC.getId().toString())
        );
    }

    /**
     * {@code GET  /imcs} : get all the iMCS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iMCS in body.
     */
    @GetMapping("/imcs")
    public List<IMC> getAllIMCS() {
        log.debug("REST request to get all IMCS");
        return iMCRepository.findAll();
    }

    /**
     * {@code GET  /imcs/:id} : get the "id" iMC.
     *
     * @param id the id of the iMC to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iMC, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/imcs/{id}")
    public ResponseEntity<IMC> getIMC(@PathVariable Long id) {
        log.debug("REST request to get IMC : {}", id);
        Optional<IMC> iMC = iMCRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iMC);
    }

    /**
     * {@code GET /imcs/patient/:id} : get all the imcs of a patient by id sorted by date.
     * @param id the id of the patient.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imcs in body.
     */
    @GetMapping("/imcs/patient/{id}")
    public List<IMC> getAllIMCSByPatientId(@PathVariable Long id) {
        log.debug("REST request to get all IMCS of patient : {}", id);
        return iMCRepository.findByPatientIdOrderByDateDesc(id);
    }

    /**
     * {@code DELETE  /imcs/:id} : delete the "id" iMC.
     *
     * @param id the id of the iMC to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/imcs/{id}")
    public ResponseEntity<Void> deleteIMC(@PathVariable Long id) {
        log.debug("REST request to delete IMC : {}", id);
        iMCRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
