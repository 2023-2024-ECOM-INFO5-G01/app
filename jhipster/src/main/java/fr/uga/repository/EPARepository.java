package fr.uga.repository;

import fr.uga.domain.EPA;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EPA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPARepository extends JpaRepository<EPA, Long> {}
