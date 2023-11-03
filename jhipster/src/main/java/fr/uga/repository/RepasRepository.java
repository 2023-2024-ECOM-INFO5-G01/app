package fr.uga.repository;

import fr.uga.domain.Repas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Repas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepasRepository extends JpaRepository<Repas, Long> {}
