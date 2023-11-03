package fr.uga.repository;

import fr.uga.domain.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PatientRepositoryWithBagRelationshipsImpl implements PatientRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Patient> fetchBagRelationships(Optional<Patient> patient) {
        return patient.map(this::fetchUsers);
    }

    @Override
    public Page<Patient> fetchBagRelationships(Page<Patient> patients) {
        return new PageImpl<>(fetchBagRelationships(patients.getContent()), patients.getPageable(), patients.getTotalElements());
    }

    @Override
    public List<Patient> fetchBagRelationships(List<Patient> patients) {
        return Optional.of(patients).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Patient fetchUsers(Patient result) {
        return entityManager
            .createQuery("select patient from Patient patient left join fetch patient.users where patient.id = :id", Patient.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Patient> fetchUsers(List<Patient> patients) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, patients.size()).forEach(index -> order.put(patients.get(index).getId(), index));
        List<Patient> result = entityManager
            .createQuery("select patient from Patient patient left join fetch patient.users where patient in :patients", Patient.class)
            .setParameter("patients", patients)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
