package fr.uga.repository;

import fr.uga.domain.EPA;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for the EPA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPARepository extends JpaRepository<EPA, Long> {
        List <EPA> findByPatientIdOrderByDateDesc(Long patientId);

}
