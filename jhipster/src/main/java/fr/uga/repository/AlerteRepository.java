package fr.uga.repository;

import fr.uga.domain.Alerte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alerte entity.
 *
 * When extending this class, extend AlerteRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AlerteRepository extends AlerteRepositoryWithBagRelationships, JpaRepository<Alerte, Long> {
    default Optional<Alerte> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Alerte> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Alerte> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
    List<Alerte> findByUsers_LoginOrderByDateDesc(String login);

    List<Alerte> findByPatient_IdAndUsers_LoginOrderByDateDesc(Long id, String login);
}
