package fr.uga.web.rest;

import fr.uga.domain.Ehpad;
import fr.uga.repository.EhpadRepository;
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
 * REST controller for managing {@link fr.uga.domain.Ehpad}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EhpadResource {

    private final Logger log = LoggerFactory.getLogger(EhpadResource.class);

    private static final String ENTITY_NAME = "ehpad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EhpadRepository ehpadRepository;

    public EhpadResource(EhpadRepository ehpadRepository) {
        this.ehpadRepository = ehpadRepository;
    }

    /**
     * {@code POST  /ehpads} : Create a new ehpad.
     *
     * @param ehpad the ehpad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ehpad, or with status {@code 400 (Bad Request)} if the ehpad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ehpads")
    public ResponseEntity<Ehpad> createEhpad(@RequestBody Ehpad ehpad) throws URISyntaxException {
        log.debug("REST request to save Ehpad : {}", ehpad);
        if (ehpad.getId() != null) {
            throw new BadRequestAlertException("A new ehpad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ehpad result = ehpadRepository.save(ehpad);
        return ResponseEntity
            .created(new URI("/api/ehpads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ehpads/:id} : Updates an existing ehpad.
     *
     * @param id the id of the ehpad to save.
     * @param ehpad the ehpad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ehpad,
     * or with status {@code 400 (Bad Request)} if the ehpad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ehpad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ehpads/{id}")
    public ResponseEntity<Ehpad> updateEhpad(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ehpad ehpad)
        throws URISyntaxException {
        log.debug("REST request to update Ehpad : {}, {}", id, ehpad);
        if (ehpad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ehpad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ehpadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ehpad result = ehpadRepository.save(ehpad);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ehpad.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ehpads/:id} : Partial updates given fields of an existing ehpad, field will ignore if it is null
     *
     * @param id the id of the ehpad to save.
     * @param ehpad the ehpad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ehpad,
     * or with status {@code 400 (Bad Request)} if the ehpad is not valid,
     * or with status {@code 404 (Not Found)} if the ehpad is not found,
     * or with status {@code 500 (Internal Server Error)} if the ehpad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ehpads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ehpad> partialUpdateEhpad(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ehpad ehpad)
        throws URISyntaxException {
        log.debug("REST request to partial update Ehpad partially : {}, {}", id, ehpad);
        if (ehpad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ehpad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ehpadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ehpad> result = ehpadRepository
            .findById(ehpad.getId())
            .map(existingEhpad -> {
                if (ehpad.getNom() != null) {
                    existingEhpad.setNom(ehpad.getNom());
                }

                return existingEhpad;
            })
            .map(ehpadRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ehpad.getId().toString())
        );
    }

    /**
     * {@code GET  /ehpads} : get all the ehpads.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ehpads in body.
     */
    @GetMapping("/ehpads")
    public List<Ehpad> getAllEhpads(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Ehpads");
        if (eagerload) {
            return ehpadRepository.findAllWithEagerRelationships();
        } else {
            return ehpadRepository.findAll();
        }
    }

    /**
     * {@code GET  /ehpads/:id} : get the "id" ehpad.
     *
     * @param id the id of the ehpad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ehpad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ehpads/{id}")
    public ResponseEntity<Ehpad> getEhpad(@PathVariable Long id) {
        log.debug("REST request to get Ehpad : {}", id);
        Optional<Ehpad> ehpad = ehpadRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ehpad);
    }

    /**
     * {@code DELETE  /ehpads/:id} : delete the "id" ehpad.
     *
     * @param id the id of the ehpad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ehpads/{id}")
    public ResponseEntity<Void> deleteEhpad(@PathVariable Long id) {
        log.debug("REST request to delete Ehpad : {}", id);
        ehpadRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
