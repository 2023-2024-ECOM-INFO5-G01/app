package fr.uga.service;

import fr.uga.domain.Patient;
import fr.uga.repository.PatientRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.uga.domain.Patient}.
 */
@Service
@Transactional
public class PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Save a patient.
     *
     * @param patient the entity to save.
     * @return the persisted entity.
     */
    public Patient save(Patient patient) {
        log.debug("Request to save Patient : {}", patient);
        return patientRepository.save(patient);
    }

    /**
     * Update a patient.
     *
     * @param patient the entity to save.
     * @return the persisted entity.
     */
    public Patient update(Patient patient) {
        log.debug("Request to update Patient : {}", patient);
        return patientRepository.save(patient);
    }

    /**
     * Partially update a patient.
     *
     * @param patient the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Patient> partialUpdate(Patient patient) {
        log.debug("Request to partially update Patient : {}", patient);

        return patientRepository
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
    }

    /**
     * Get all the patients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        log.debug("Request to get all Patients");
        return patientRepository.findAll(pageable);
    }

    /**
     * Get all the patients with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Patient> findAllWithEagerRelationships(Pageable pageable) {
        return patientRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one patient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Patient> findOne(Long id) {
        log.debug("Request to get Patient : {}", id);
        return patientRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the patient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Patient : {}", id);
        patientRepository.deleteById(id);
    }
}
