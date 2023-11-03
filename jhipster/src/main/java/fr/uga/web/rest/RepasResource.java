package fr.uga.web.rest;

import fr.uga.domain.Repas;
import fr.uga.repository.RepasRepository;
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
 * REST controller for managing {@link fr.uga.domain.Repas}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RepasResource {

    private final Logger log = LoggerFactory.getLogger(RepasResource.class);

    private static final String ENTITY_NAME = "repas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepasRepository repasRepository;

    public RepasResource(RepasRepository repasRepository) {
        this.repasRepository = repasRepository;
    }

    /**
     * {@code POST  /repas} : Create a new repas.
     *
     * @param repas the repas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repas, or with status {@code 400 (Bad Request)} if the repas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/repas")
    public ResponseEntity<Repas> createRepas(@RequestBody Repas repas) throws URISyntaxException {
        log.debug("REST request to save Repas : {}", repas);
        if (repas.getId() != null) {
            throw new BadRequestAlertException("A new repas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Repas result = repasRepository.save(repas);
        return ResponseEntity
            .created(new URI("/api/repas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /repas/:id} : Updates an existing repas.
     *
     * @param id the id of the repas to save.
     * @param repas the repas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repas,
     * or with status {@code 400 (Bad Request)} if the repas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/repas/{id}")
    public ResponseEntity<Repas> updateRepas(@PathVariable(value = "id", required = false) final Long id, @RequestBody Repas repas)
        throws URISyntaxException {
        log.debug("REST request to update Repas : {}, {}", id, repas);
        if (repas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Repas result = repasRepository.save(repas);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repas.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /repas/:id} : Partial updates given fields of an existing repas, field will ignore if it is null
     *
     * @param id the id of the repas to save.
     * @param repas the repas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repas,
     * or with status {@code 400 (Bad Request)} if the repas is not valid,
     * or with status {@code 404 (Not Found)} if the repas is not found,
     * or with status {@code 500 (Internal Server Error)} if the repas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/repas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Repas> partialUpdateRepas(@PathVariable(value = "id", required = false) final Long id, @RequestBody Repas repas)
        throws URISyntaxException {
        log.debug("REST request to partial update Repas partially : {}, {}", id, repas);
        if (repas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Repas> result = repasRepository
            .findById(repas.getId())
            .map(existingRepas -> {
                if (repas.getDate() != null) {
                    existingRepas.setDate(repas.getDate());
                }
                if (repas.getCalories() != null) {
                    existingRepas.setCalories(repas.getCalories());
                }

                return existingRepas;
            })
            .map(repasRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repas.getId().toString())
        );
    }

    /**
     * {@code GET  /repas} : get all the repas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repas in body.
     */
    @GetMapping("/repas")
    public List<Repas> getAllRepas() {
        log.debug("REST request to get all Repas");
        return repasRepository.findAll();
    }

    /**
     * {@code GET  /repas/:id} : get the "id" repas.
     *
     * @param id the id of the repas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/repas/{id}")
    public ResponseEntity<Repas> getRepas(@PathVariable Long id) {
        log.debug("REST request to get Repas : {}", id);
        Optional<Repas> repas = repasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(repas);
    }

    /**
     * {@code DELETE  /repas/:id} : delete the "id" repas.
     *
     * @param id the id of the repas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/repas/{id}")
    public ResponseEntity<Void> deleteRepas(@PathVariable Long id) {
        log.debug("REST request to delete Repas : {}", id);
        repasRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
