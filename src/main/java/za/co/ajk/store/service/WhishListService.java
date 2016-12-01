package za.co.ajk.store.service;

import za.co.ajk.store.service.dto.WhishListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing WhishList.
 */
public interface WhishListService {

    /**
     * Save a whishList.
     *
     * @param whishListDTO the entity to save
     * @return the persisted entity
     */
    WhishListDTO save(WhishListDTO whishListDTO);

    /**
     *  Get all the whishLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WhishListDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" whishList.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WhishListDTO findOne(Long id);

    /**
     *  Delete the "id" whishList.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
