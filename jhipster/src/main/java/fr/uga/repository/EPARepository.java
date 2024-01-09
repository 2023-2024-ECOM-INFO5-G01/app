package fr.uga.repository;

import fr.uga.domain.EPA;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * Spring Data JPA repository for the EPA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPARepository extends JpaRepository<EPA, Long> {
        List <EPA> findByPatientIdOrderByDateAsc(Long patientId);

       Optional <EPA> findFirstByPatientIdOrderByDateDesc(Long patientId);
    void deleteByPatient_Id(Long id);

}
