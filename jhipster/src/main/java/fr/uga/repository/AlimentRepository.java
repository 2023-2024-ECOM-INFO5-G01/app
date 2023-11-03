package fr.uga.repository;

import fr.uga.domain.Aliment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Aliment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {}
