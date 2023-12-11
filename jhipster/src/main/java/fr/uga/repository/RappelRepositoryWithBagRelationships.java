package fr.uga.repository;

import fr.uga.domain.Rappel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RappelRepositoryWithBagRelationships {
    Optional<Rappel> fetchBagRelationships(Optional<Rappel> rappel);

    List<Rappel> fetchBagRelationships(List<Rappel> rappels);

    Page<Rappel> fetchBagRelationships(Page<Rappel> rappels);
}
