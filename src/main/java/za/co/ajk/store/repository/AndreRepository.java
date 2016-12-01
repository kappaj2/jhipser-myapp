package za.co.ajk.store.repository;

import za.co.ajk.store.domain.Andre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Andre entity.
 */
@SuppressWarnings("unused")
public interface AndreRepository extends JpaRepository<Andre,Long> {

}
