package fr.uga.repository;

import fr.uga.domain.Albumine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Albumine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumineRepository extends JpaRepository<Albumine, Long> {}
