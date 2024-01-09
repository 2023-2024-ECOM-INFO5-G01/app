package fr.uga.repository;

import fr.uga.domain.Poids;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
/**
 * Spring Data JPA repository for the Poids entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoidsRepository extends JpaRepository<Poids, Long> {
    List <Poids> findByPatientIdOrderByDateAsc(Long patientId);


    Optional <Poids> findFirstByPatientIdOrderByDateDesc(Long patientId);
    List <Poids> findByPatientIdAndDateBeforeOrderByDateDesc(Long patientId, Instant date);
}
