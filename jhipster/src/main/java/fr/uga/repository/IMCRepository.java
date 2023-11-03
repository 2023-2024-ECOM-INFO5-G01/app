package fr.uga.repository;

import fr.uga.domain.IMC;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Spring Data JPA repository for the IMC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IMCRepository extends JpaRepository<IMC, Long> {
    List <IMC> findByPatientIdOrderByDateDesc(Long patientId);
}
