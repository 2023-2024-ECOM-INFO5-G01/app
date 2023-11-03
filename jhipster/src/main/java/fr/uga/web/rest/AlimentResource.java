package fr.uga.web.rest;

import fr.uga.domain.Aliment;
import fr.uga.repository.AlimentRepository;
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
 * REST controller for managing {@link fr.uga.domain.Aliment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AlimentResource {

    private final Logger log = LoggerFactory.getLogger(AlimentResource.class);

    private static final String ENTITY_NAME = "aliment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlimentRepository alimentRepository;

    public AlimentResource(AlimentRepository alimentRepository) {
        this.alimentRepository = alimentRepository;
    }

    /**
     * {@code POST  /aliments} : Create a new aliment.
     *
     * @param aliment the aliment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aliment, or with status {@code 400 (Bad Request)} if the aliment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aliments")
    public ResponseEntity<Aliment> createAliment(@RequestBody Aliment aliment) throws URISyntaxException {
        log.debug("REST request to save Aliment : {}", aliment);
        if (aliment.getId() != null) {
            throw new BadRequestAlertException("A new aliment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aliment result = alimentRepository.save(aliment);
        return ResponseEntity
            .created(new URI("/api/aliments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aliments/:id} : Updates an existing aliment.
     *
     * @param id the id of the aliment to save.
     * @param aliment the aliment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aliment,
     * or with status {@code 400 (Bad Request)} if the aliment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aliment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aliments/{id}")
    public ResponseEntity<Aliment> updateAliment(@PathVariable(value = "id", required = false) final Long id, @RequestBody Aliment aliment)
        throws URISyntaxException {
        log.debug("REST request to update Aliment : {}, {}", id, aliment);
        if (aliment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aliment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Aliment result = alimentRepository.save(aliment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aliment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aliments/:id} : Partial updates given fields of an existing aliment, field will ignore if it is null
     *
     * @param id the id of the aliment to save.
     * @param aliment the aliment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aliment,
     * or with status {@code 400 (Bad Request)} if the aliment is not valid,
     * or with status {@code 404 (Not Found)} if the aliment is not found,
     * or with status {@code 500 (Internal Server Error)} if the aliment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aliments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aliment> partialUpdateAliment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Aliment aliment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aliment partially : {}, {}", id, aliment);
        if (aliment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aliment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alimentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aliment> result = alimentRepository
            .findById(aliment.getId())
            .map(existingAliment -> {
                if (aliment.getNom() != null) {
                    existingAliment.setNom(aliment.getNom());
                }
                if (aliment.getCalories() != null) {
                    existingAliment.setCalories(aliment.getCalories());
                }

                return existingAliment;
            })
            .map(alimentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aliment.getId().toString())
        );
    }

    /**
     * {@code GET  /aliments} : get all the aliments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aliments in body.
     */
    @GetMapping("/aliments")
    public List<Aliment> getAllAliments() {
        log.debug("REST request to get all Aliments");
        return alimentRepository.findAll();
    }

    /**
     * {@code GET  /aliments/:id} : get the "id" aliment.
     *
     * @param id the id of the aliment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aliment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aliments/{id}")
    public ResponseEntity<Aliment> getAliment(@PathVariable Long id) {
        log.debug("REST request to get Aliment : {}", id);
        Optional<Aliment> aliment = alimentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aliment);
    }

    /**
     * {@code DELETE  /aliments/:id} : delete the "id" aliment.
     *
     * @param id the id of the aliment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aliments/{id}")
    public ResponseEntity<Void> deleteAliment(@PathVariable Long id) {
        log.debug("REST request to delete Aliment : {}", id);
        alimentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
