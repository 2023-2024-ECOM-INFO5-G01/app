package fr.uga.repository;

import fr.uga.domain.Ehpad;
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
public class EhpadRepositoryWithBagRelationshipsImpl implements EhpadRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ehpad> fetchBagRelationships(Optional<Ehpad> ehpad) {
        return ehpad.map(this::fetchUsers);
    }

    @Override
    public Page<Ehpad> fetchBagRelationships(Page<Ehpad> ehpads) {
        return new PageImpl<>(fetchBagRelationships(ehpads.getContent()), ehpads.getPageable(), ehpads.getTotalElements());
    }

    @Override
    public List<Ehpad> fetchBagRelationships(List<Ehpad> ehpads) {
        return Optional.of(ehpads).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Ehpad fetchUsers(Ehpad result) {
        return entityManager
            .createQuery("select ehpad from Ehpad ehpad left join fetch ehpad.users where ehpad.id = :id", Ehpad.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Ehpad> fetchUsers(List<Ehpad> ehpads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ehpads.size()).forEach(index -> order.put(ehpads.get(index).getId(), index));
        List<Ehpad> result = entityManager
            .createQuery("select ehpad from Ehpad ehpad left join fetch ehpad.users where ehpad in :ehpads", Ehpad.class)
            .setParameter("ehpads", ehpads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
