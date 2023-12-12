package fr.uga.web.rest;

import fr.uga.domain.Albumine;
import fr.uga.repository.AlbumineRepository;
import fr.uga.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.uga.domain.Albumine}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AlbumineResource {

    private final Logger log = LoggerFactory.getLogger(AlbumineResource.class);

    private static final String ENTITY_NAME = "albumine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlbumineRepository albumineRepository;

    public AlbumineResource(AlbumineRepository albumineRepository) {
        this.albumineRepository = albumineRepository;
    }

    /**
     * {@code POST  /albumines} : Create a new albumine.
     *
     * @param albumine the albumine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new albumine, or with status {@code 400 (Bad Request)} if the albumine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/albumines")
    public ResponseEntity<Albumine> createAlbumine(@RequestBody Albumine albumine) throws URISyntaxException {
        log.debug("REST request to save Albumine : {}", albumine);
        if (albumine.getId() != null) {
            throw new BadRequestAlertException("A new albumine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Albumine result = albumineRepository.save(albumine);
        return ResponseEntity
            .created(new URI("/api/albumines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /albumines/:id} : Updates an existing albumine.
     *
     * @param id the id of the albumine to save.
     * @param albumine the albumine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated albumine,
     * or with status {@code 400 (Bad Request)} if the albumine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the albumine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/albumines/{id}")
    public ResponseEntity<Albumine> updateAlbumine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Albumine albumine
    ) throws URISyntaxException {
        log.debug("REST request to update Albumine : {}, {}", id, albumine);
        if (albumine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, albumine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!albumineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Albumine result = albumineRepository.save(albumine);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, albumine.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /albumines/:id} : Partial updates given fields of an existing albumine, field will ignore if it is null
     *
     * @param id the id of the albumine to save.
     * @param albumine the albumine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated albumine,
     * or with status {@code 400 (Bad Request)} if the albumine is not valid,
     * or with status {@code 404 (Not Found)} if the albumine is not found,
     * or with status {@code 500 (Internal Server Error)} if the albumine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/albumines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Albumine> partialUpdateAlbumine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Albumine albumine
    ) throws URISyntaxException {
        log.debug("REST request to partial update Albumine partially : {}, {}", id, albumine);
        if (albumine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, albumine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!albumineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Albumine> result = albumineRepository
            .findById(albumine.getId())
            .map(existingAlbumine -> {
                if (albumine.getAlbu() != null) {
                    existingAlbumine.setAlbu(albumine.getAlbu());
                }
                if (albumine.getDate() != null) {
                    existingAlbumine.setDate(albumine.getDate());
                }

                return existingAlbumine;
            })
            .map(albumineRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, albumine.getId().toString())
        );
    }

    /**
     * {@code GET  /albumines} : get all the albumines.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of albumines in body.
     */
    @GetMapping("/albumines")
    public List<Albumine> getAllAlbumines(@RequestParam(required = false) String filter) {
        if ("patient-is-null".equals(filter)) {
            log.debug("REST request to get all Albumines where patient is null");
            return StreamSupport
                .stream(albumineRepository.findAll().spliterator(), false)
                .filter(albumine -> albumine.getPatient() == null)
                .toList();
        }
        log.debug("REST request to get all Albumines");
        return albumineRepository.findAll();
    }

    /**
     * {@code GET  /albumines/:id} : get the "id" albumine.
     *
     * @param id the id of the albumine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the albumine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/albumines/{id}")
    public ResponseEntity<Albumine> getAlbumine(@PathVariable Long id) {
        log.debug("REST request to get Albumine : {}", id);
        Optional<Albumine> albumine = albumineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(albumine);
    }

    /**
     * {@code DELETE  /albumines/:id} : delete the "id" albumine.
     *
     * @param id the id of the albumine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/albumines/{id}")
    public ResponseEntity<Void> deleteAlbumine(@PathVariable Long id) {
        log.debug("REST request to delete Albumine : {}", id);
        albumineRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    
}
