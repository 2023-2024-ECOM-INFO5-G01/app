package fr.uga.web.rest;

import fr.uga.domain.Alerte;
import fr.uga.repository.AlerteRepository;
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
 * REST controller for managing {@link fr.uga.domain.Alerte}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AlerteResource {

    private final Logger log = LoggerFactory.getLogger(AlerteResource.class);

    private static final String ENTITY_NAME = "alerte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlerteRepository alerteRepository;

    public AlerteResource(AlerteRepository alerteRepository) {
        this.alerteRepository = alerteRepository;
    }

    /**
     * {@code POST  /alertes} : Create a new alerte.
     *
     * @param alerte the alerte to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alerte, or with status {@code 400 (Bad Request)} if the alerte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alertes")
    public ResponseEntity<Alerte> createAlerte(@RequestBody Alerte alerte) throws URISyntaxException {
        log.debug("REST request to save Alerte : {}", alerte);
        if (alerte.getId() != null) {
            throw new BadRequestAlertException("A new alerte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Alerte result = alerteRepository.save(alerte);
        return ResponseEntity
            .created(new URI("/api/alertes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alertes/:id} : Updates an existing alerte.
     *
     * @param id the id of the alerte to save.
     * @param alerte the alerte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerte,
     * or with status {@code 400 (Bad Request)} if the alerte is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alerte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alertes/{id}")
    public ResponseEntity<Alerte> updateAlerte(@PathVariable(value = "id", required = false) final Long id, @RequestBody Alerte alerte)
        throws URISyntaxException {
        log.debug("REST request to update Alerte : {}, {}", id, alerte);
        if (alerte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Alerte result = alerteRepository.save(alerte);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerte.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alertes/:id} : Partial updates given fields of an existing alerte, field will ignore if it is null
     *
     * @param id the id of the alerte to save.
     * @param alerte the alerte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerte,
     * or with status {@code 400 (Bad Request)} if the alerte is not valid,
     * or with status {@code 404 (Not Found)} if the alerte is not found,
     * or with status {@code 500 (Internal Server Error)} if the alerte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/alertes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Alerte> partialUpdateAlerte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Alerte alerte
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alerte partially : {}, {}", id, alerte);
        if (alerte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alerte> result = alerteRepository
            .findById(alerte.getId())
            .map(existingAlerte -> {
                if (alerte.getDate() != null) {
                    existingAlerte.setDate(alerte.getDate());
                }
                if (alerte.getAction() != null) {
                    existingAlerte.setAction(alerte.getAction());
                }
                if (alerte.getVerif() != null) {
                    existingAlerte.setVerif(alerte.getVerif());
                }

                return existingAlerte;
            })
            .map(alerteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerte.getId().toString())
        );
    }

    /**
     * {@code GET  /alertes} : get all the alertes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertes in body.
     */
    @GetMapping("/alertes")
    public List<Alerte> getAllAlertes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Alertes");
        if (eagerload) {
            return alerteRepository.findAllWithEagerRelationships();
        } else {
            return alerteRepository.findAll();
        }
    }

    /**
     * {@code GET  /alertes/:id} : get the "id" alerte.
     *
     * @param id the id of the alerte to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alerte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alertes/{id}")
    public ResponseEntity<Alerte> getAlerte(@PathVariable Long id) {
        log.debug("REST request to get Alerte : {}", id);
        Optional<Alerte> alerte = alerteRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(alerte);
    }

    /**
     * {@code DELETE  /alertes/:id} : delete the "id" alerte.
     *
     * @param id the id of the alerte to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alertes/{id}")
    public ResponseEntity<Void> deleteAlerte(@PathVariable Long id) {
        log.debug("REST request to delete Alerte : {}", id);
        alerteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
 * GET  /alertes/user/{login} : get all the alertes of a specific user.
 *
 * @param login the login of the user.
 * @return the ResponseEntity with status 200 (OK) and the list of alertes in body.
 */
@GetMapping("/alertes/user/{login}")
public ResponseEntity<List<Alerte>> getAllAlertesByUser(@PathVariable String login) {
    log.debug("REST request to get all Alertes of user: {}", login);
    
    List<Alerte> alertes = alerteRepository.findByUser_LoginOrderByDateDesc(login);
    log.info("Alertes: {}", alertes);
    return ResponseEntity.ok().body(alertes);
}

/**
 * GET  /alertes/patient/{id}{login} : get all the alertes of a specific patient of a specific user.
 * @param id the id of the patient 
 * @param login the login of the user.
 * @return the ResponseEntity with status 200 (OK) and the list of alertes in body.
 */
    @GetMapping("/alertes/patient/{id}/{login}")
    public ResponseEntity<List<Alerte>> getAllAlertesByPatientAndUser(@PathVariable Long id, @PathVariable String login) {
        log.debug("REST request to get all Alertes of patient: {}", id);
        
        List<Alerte> alertes = alerteRepository.findByPatient_IdAndUser_LoginOrderByDateDesc(id, login);

        return ResponseEntity.ok().body(alertes);
    }

    /**
     * PUT /alertes/{id}/toggle-verif : Toggle the verification status of a specific alert.
     *
     * @param id the id of the alerte to toggle verification status.
     * @return the ResponseEntity with status 200 (OK) and with body the updated alerte,
     * or with status 404 (Not Found) if the alerte is not found.
     */
    @PutMapping("/alertes/{id}/toggle-verif")
    public ResponseEntity<Alerte> toggleVerificationStatus(@PathVariable Long id) {
        log.debug("REST request to toggle verification status of Alerte : {}", id);

        Optional<Alerte> alerteOptional = alerteRepository.findById(id);
        
        if (!alerteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Alerte alerte = alerteOptional.get();

        alerte.setVerif(!alerte.getVerif());

        Alerte result = alerteRepository.save(alerte);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerte.getId().toString()))
            .body(result);
    }

}
