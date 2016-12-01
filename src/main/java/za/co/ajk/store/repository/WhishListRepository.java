package za.co.ajk.store.repository;

import za.co.ajk.store.domain.WhishList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WhishList entity.
 */
@SuppressWarnings("unused")
public interface WhishListRepository extends JpaRepository<WhishList,Long> {

    @Query("select whishList from WhishList whishList where whishList.user.login = ?#{principal.username}")
    List<WhishList> findByUserIsCurrentUser();

}
