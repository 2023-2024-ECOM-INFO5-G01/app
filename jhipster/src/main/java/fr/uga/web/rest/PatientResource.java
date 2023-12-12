package fr.uga.web.rest;

import fr.uga.domain.Patient;
import fr.uga.repository.PatientRepository;
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
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import fr.uga.repository.IMCRepository;
import fr.uga.repository.PoidsRepository;
import fr.uga.repository.AlbumineRepository;
import fr.uga.domain.IMC;
import fr.uga.domain.Poids;
import fr.uga.domain.Albumine;
import java.time.LocalDate;
import java.time.ZoneId;
import fr.uga.domain.Alerte;
import fr.uga.repository.AlerteRepository;
import java.time.Instant;
/**
 * REST controller for managing {@link fr.uga.domain.Patient}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "patient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientRepository patientRepository;

    private final IMCRepository imcRepository;

    private final AlbumineRepository albumineRepository;

    private final PoidsRepository poidsRepository;

    private final AlerteRepository alerteRepository;

    public PatientResource(PatientRepository patientRepository, IMCRepository imcRepository, AlbumineRepository albumineRepository, PoidsRepository poidsRepository) {
        this.patientRepository = patientRepository;
        this.imcRepository = imcRepository;
        this.albumineRepository = albumineRepository;
        this.poidsRepository = poidsRepository;
    }

    /**
     * {@code POST  /patients} : Create a new patient.
     *
     * @param patient the patient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new patient, or with status {@code 400 (Bad Request)} if the
     *         patient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if (patient.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Patient result = patientRepository.save(patient);
        log.info("Patient created: {}", result);
        return ResponseEntity
                .created(new URI("/api/patients/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /patients/:id} : Updates an existing patient.
     *
     * @param id      the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated patient,
     *         or with status {@code 400 (Bad Request)} if the patient is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the patient
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patients/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable(value = "id", required = false) final Long id,
            @RequestBody Patient patient)
            throws URISyntaxException {
        log.debug("REST request to update Patient : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Patient result = patientRepository.save(patient);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        patient.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /patients/:id} : Partial updates given fields of an existing
     * patient, field will ignore if it is null
     *
     * @param id      the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated patient,
     *         or with status {@code 400 (Bad Request)} if the patient is not valid,
     *         or with status {@code 404 (Not Found)} if the patient is not found,
     *         or with status {@code 500 (Internal Server Error)} if the patient
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Patient> partialUpdatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Patient patient
    ) throws URISyntaxException { 
        log.debug("REST request to partial update Patient partially : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Patient> result = patientRepository
                .findById(patient.getId())
                .map(existingPatient -> {
                    if (patient.getNom() != null) {
                        existingPatient.setNom(patient.getNom());
                    }
                    if (patient.getPrenom() != null) {
                        existingPatient.setPrenom(patient.getPrenom());
                    }
                    if (patient.getStatut() != null) {
                        existingPatient.setStatut(patient.getStatut());
                    }
                    if (patient.getDateNaissance() != null) {
                        existingPatient.setDateNaissance(patient.getDateNaissance());
                    }
                    if (patient.getTaille() != null) {
                        existingPatient.setTaille(patient.getTaille());
                    }
                    if (patient.getDatearrive() != null) {
                        existingPatient.setDatearrive(patient.getDatearrive());
                    }

                    return existingPatient;
                })
                .map(patientRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()));
    }

    /**
     * {@code GET  /patients} : get all the patients.
     *
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of patients in body.
     */
    @GetMapping("/patients")
    public List<Patient> getAllPatients(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Patients");
        if (eagerload) {
            return patientRepository.findAllWithEagerRelationships();
        } else {
            return patientRepository.findAll();
        }
    }

    /**
     * {@code GET  /patients/suggestions} : get the patient that match the query.
     * 
     * @param query the query of the patient to retrieve.
     * @param login the login of the user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the patient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patients/suggestions/{query}/{login}")
    public ResponseEntity<List<Patient>> suggestPatients(@PathVariable String query, @PathVariable String login) {
        log.debug("REST request to get Patients starting with: {}, with login: {}", query, login);
        List<Patient> patientsByNom = patientRepository.findByNomStartingWithIgnoreCaseAndUsers_Login(query, login);
        List<Patient> patientsByPrenom = patientRepository.findByPrenomStartingWithIgnoreCaseAndUsers_Login(query,
                login);
        Set<Patient> uniquePatients = new HashSet<>();
        uniquePatients.addAll(patientsByNom);
        uniquePatients.addAll(patientsByPrenom);

        // Convertissez l'ensemble en liste avant de renvoyer la réponse
        List<Patient> uniquePatientsList = new ArrayList<>(uniquePatients);
        log.info("Patients suggestions: {}", uniquePatientsList);
        return ResponseEntity.ok().body(uniquePatientsList);
    }

    /**
     * GET /patients/user/{login} : get all the patients of a specific user.
     *
     * @param login the login of the user.
     * @return the ResponseEntity with status 200 (OK) and the list of patients in
     *         body.
     */
    @GetMapping("/patients/user/{login}")
    public ResponseEntity<List<Patient>> getAllPatientsByUser(@PathVariable String login) {
        log.debug("REST request to get all Patients of user: {}", login);

        List<Patient> patients = patientRepository.findByUsers_Login(login);
        log.info("Patients of user: {}", patients);
        return ResponseEntity.ok().body(patients);
    }

    /**
     * {@code GET  /patients/:id} : get the "id" patient.
     *
     * @param id the id of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the patient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        Optional<Patient> patient = patientRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(patient);
    }

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patient.
     *
     * @param id the id of the patient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /** 
     * PUT /patient/updateStatut: update patient entity status.
     * @param id the id of the patient to update status.
     * @param status the status to change
     * @return the ResponseEntity with status 200 (OK) and with body the updated patient,
        or with status 404 (Not Found) if the patient is not found.
    */
    @PutMapping("/patients/updatestatut/{id}/{statut}")
    public ResponseEntity<Patient> updateStatus(@PathVariable Long id, @PathVariable String statut) {
        log.debug("REST request to update Patient : {} with statut : {}", id, statut);

        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Patient patient = patientOptional.get();
        patient.setStatut(statut);

        Patient result = patientRepository.save(patient);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()))
            .body(result);
    }

    @GetMapping("/patients/malnutrition/{id}")
public ResponseEntity<Alerte> evaluerMalnutrition(@PathVariable Long id) {
    Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent()) {
           // return "Patient not found";
              return ResponseEntity.notFound().build();
        }

        Patient patient = patientOptional.get();
IMC latestimc = null;
    Albumine albumine = null;
    Poids poidsOneMonthago = null;
    Poids poidsSixMonthago = null;
    Poids latestPoids = null;

        Optional<IMC> latestimc1 = imcRepository.findFirstByPatientIdOrderByDateDesc(id);
        if (!latestimc1.isPresent()) {
            //return "Pas de données IMC pour ce patient";
            return ResponseEntity.notFound().build();

        }
        else {
             latestimc = latestimc1.get();
        }
        Optional <Albumine> albumine1 = albumineRepository.findFirstByPatientIdOrderByDateDesc(id);
        if (!albumine1.isPresent()) {
            //return "Pas de données albumine pour ce patient";
            return ResponseEntity.notFound().build();

        }
        else {
             albumine = albumine1.get();
        }
        ZoneId defaultZoneId = ZoneId.systemDefault();

List<Poids> poidsList = poidsRepository.findByPatientIdAndDateBeforeOrderByDateDesc(id, LocalDate.now().minusMonths(1).atStartOfDay(defaultZoneId).toInstant());
if (poidsList.isEmpty()) {
    //return "Pas de données poids il ya au moins 1 mois pour ce patient";
     return ResponseEntity.notFound().build();

}
else {
    poidsOneMonthago = poidsList.get(0);
}
List <Poids> poidsSixMonthago1 = poidsRepository.findByPatientIdAndDateBeforeOrderByDateDesc(id, LocalDate.now().minusMonths(6).atStartOfDay(defaultZoneId).toInstant());
if (poidsSixMonthago1.isEmpty()) {
    //return "Pas de données poids il ya au moins 6 mois pour ce patient";
      return ResponseEntity.notFound().build();

}
else {
    poidsSixMonthago = poidsSixMonthago1.get(0);
}


       Optional <Poids> latestPoids1 = poidsRepository.findFirstByPatientIdOrderByDateDesc(id);
        if (!latestPoids1.isPresent()) {
            //return "Pas de données poids pour ce patient";
            return ResponseEntity.notFound().build();

        }
        else {
             latestPoids = latestPoids1.get();
        }
        if (latestimc.getImc() < 18.5 && latestimc.getImc() > 17) {
        Alerte newAlerte = new Alerte();
    newAlerte.setAction("dénutrition modérée car IMC est entre 17 et 18.5 : " + "latestimc : " + latestimc.getImc());
    newAlerte.setDate(Instant.now());
    newAlerte.setVerif(false);
    newAlerte.setUser(patient.getUser()); 
    newAlerte.setPatient(patient);

    Alerte result = alerteRepository.save(newAlerte);

    return ResponseEntity
        .created(new URI("/api/alertes/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
}
   /* return "dénutrition modérée car IMC est entre 17 et 18.5 : " + "latestimc : " + latestimc.getImc();
}
else if (latestPoids.getPoids() < poidsOneMonthago.getPoids() * 0.95) {
    return "dénutrition modérée car le poids a diminué de plus de 5% en un mois : " + "latestPoids : " + latestPoids.getPoids() + " poidsOneMonthago : " + poidsOneMonthago.getPoids();
}
else if (latestPoids.getPoids() < poidsSixMonthago.getPoids() * 0.9) {
    return "dénutrition modérée car le poids a diminué de plus de 10% en six mois : " + "latestPoids : " + latestPoids.getPoids() + " poidsSixMonthago : " + poidsSixMonthago.getPoids();
}
else if (albumine.getAlbu() > 30 && albumine.getAlbu() < 35) {
    return "dénutrition modérée car le niveau d'albumine est entre 30 et 35 : " + "albumine : " + albumine.getAlbu();
}
else if (latestimc.getImc() <= 17) {
    return "dénutrition sévère car IMC est inférieur ou égal à 17 : " + "latestimc : " + latestimc.getImc();
}
else if (latestPoids.getPoids() < poidsOneMonthago.getPoids() * 0.9) {
    return "dénutrition sévère car le poids a diminué de plus de 10% en un mois : " + "latestPoids : " + latestPoids.getPoids() + " poidsOneMonthago : " + poidsOneMonthago.getPoids();
}
else if (latestPoids.getPoids() < poidsSixMonthago.getPoids() * 0.85) {
    return "dénutrition sévère car le poids a diminué de plus de 15% en six mois : " + "latestPoids : " + latestPoids.getPoids() + " poidsSixMonthago : " + poidsSixMonthago.getPoids();
}
else if (albumine.getAlbu() <= 30) {
    return "dénutrition sévère car le niveau d'albumine est inférieur ou égal à 30 : " + "albumine : " + albumine.getAlbu();
}
else {
    return "pas de dénutrition";
}*/
}
}
