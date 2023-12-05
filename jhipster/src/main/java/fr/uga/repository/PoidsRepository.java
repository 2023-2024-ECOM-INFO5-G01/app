package fr.uga.repository;

import fr.uga.domain.Poids;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Spring Data JPA repository for the Poids entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoidsRepository extends JpaRepository<Poids, Long> {
    List <Poids> findByPatientIdOrderByDateAsc(Long patientId);
}
