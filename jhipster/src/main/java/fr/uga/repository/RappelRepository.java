package fr.uga.repository;

import fr.uga.domain.Rappel;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rappel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RappelRepository extends JpaRepository<Rappel, Long> {
    @Query("select rappel from Rappel rappel where rappel.user.login = ?#{principal.username}")
    List<Rappel> findByUserIsCurrentUser();
}
