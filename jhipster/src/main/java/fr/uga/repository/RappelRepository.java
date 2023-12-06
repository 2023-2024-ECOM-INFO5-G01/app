package fr.uga.repository;

import fr.uga.domain.Rappel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rappel entity.
 *
 * When extending this class, extend RappelRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface RappelRepository extends RappelRepositoryWithBagRelationships, JpaRepository<Rappel, Long> {
    default Optional<Rappel> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Rappel> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Rappel> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    List<Rappel> findByUsers_LoginOrderByDateDesc(String login);

    List<Rappel> findByPatient_IdAndUsers_LoginOrderByDateDesc(Long id, String login);

    List<Rappel> findByPatient_IdOrderByDateDesc(Long id);

}
