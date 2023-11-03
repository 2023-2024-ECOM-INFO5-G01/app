package fr.uga.repository;

import fr.uga.domain.Ehpad;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EhpadRepositoryWithBagRelationships {
    Optional<Ehpad> fetchBagRelationships(Optional<Ehpad> ehpad);

    List<Ehpad> fetchBagRelationships(List<Ehpad> ehpads);

    Page<Ehpad> fetchBagRelationships(Page<Ehpad> ehpads);
}
