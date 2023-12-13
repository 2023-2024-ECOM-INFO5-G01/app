package fr.uga.repository;

import fr.uga.domain.Patient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Patient entity.
 *
 * When extending this class, extend PatientRepositoryWithBagRelationships too.
 * For more information refer to
 * https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PatientRepository extends PatientRepositoryWithBagRelationships, JpaRepository<Patient, Long> {
    default Optional<Patient> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Patient> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Patient> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    List<Patient> findByNomStartingWithIgnoreCaseAndUsers_Login(String query, String login);

    List<Patient> findByPrenomStartingWithIgnoreCaseAndUsers_Login(String query, String login);

    List<Patient> findByUsers_Login(String login);


}
