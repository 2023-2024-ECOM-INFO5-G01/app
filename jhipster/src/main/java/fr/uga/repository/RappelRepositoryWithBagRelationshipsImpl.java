package fr.uga.repository;

import fr.uga.domain.Rappel;
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
public class RappelRepositoryWithBagRelationshipsImpl implements RappelRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Rappel> fetchBagRelationships(Optional<Rappel> rappel) {
        return rappel.map(this::fetchUsers);
    }

    @Override
    public Page<Rappel> fetchBagRelationships(Page<Rappel> rappels) {
        return new PageImpl<>(fetchBagRelationships(rappels.getContent()), rappels.getPageable(), rappels.getTotalElements());
    }

    @Override
    public List<Rappel> fetchBagRelationships(List<Rappel> rappels) {
        return Optional.of(rappels).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Rappel fetchUsers(Rappel result) {
        return entityManager
            .createQuery("select rappel from Rappel rappel left join fetch rappel.users where rappel.id = :id", Rappel.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Rappel> fetchUsers(List<Rappel> rappels) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, rappels.size()).forEach(index -> order.put(rappels.get(index).getId(), index));
        List<Rappel> result = entityManager
            .createQuery("select rappel from Rappel rappel left join fetch rappel.users where rappel in :rappels", Rappel.class)
            .setParameter("rappels", rappels)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
