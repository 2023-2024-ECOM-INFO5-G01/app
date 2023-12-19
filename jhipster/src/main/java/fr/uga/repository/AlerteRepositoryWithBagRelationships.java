package fr.uga.repository;

import fr.uga.domain.Alerte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlerteRepositoryWithBagRelationships {
    Optional<Alerte> fetchBagRelationships(Optional<Alerte> alerte);

    List<Alerte> fetchBagRelationships(List<Alerte> alertes);

    Page<Alerte> fetchBagRelationships(Page<Alerte> alertes);
}
