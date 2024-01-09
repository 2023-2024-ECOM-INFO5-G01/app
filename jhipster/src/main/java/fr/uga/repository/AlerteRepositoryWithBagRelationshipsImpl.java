package fr.uga.repository;

import fr.uga.domain.Alerte;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AlerteRepositoryWithBagRelationshipsImpl implements AlerteRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Alerte> fetchBagRelationships(Optional<Alerte> alerte) {
        return alerte.map(this::fetchUsers);
    }

    @Override
    public Page<Alerte> fetchBagRelationships(Page<Alerte> alertes) {
        return new PageImpl<>(fetchBagRelationships(alertes.getContent()), alertes.getPageable(), alertes.getTotalElements());
    }

    @Override
    public List<Alerte> fetchBagRelationships(List<Alerte> alertes) {
        return Optional.of(alertes).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Alerte fetchUsers(Alerte result) {
        return entityManager
            .createQuery("select alerte from Alerte alerte left join fetch alerte.users where alerte.id = :id", Alerte.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Alerte> fetchUsers(List<Alerte> alertes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alertes.size()).forEach(index -> order.put(alertes.get(index).getId(), index));
        List<Alerte> result = entityManager
            .createQuery("select alerte from Alerte alerte left join fetch alerte.users where alerte in :alertes", Alerte.class)
            .setParameter("alertes", alertes)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
