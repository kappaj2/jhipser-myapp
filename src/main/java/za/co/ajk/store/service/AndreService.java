package za.co.ajk.store.service;

import za.co.ajk.store.service.dto.AndreDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Andre.
 */
public interface AndreService {

    /**
     * Save a andre.
     *
     * @param andreDTO the entity to save
     * @return the persisted entity
     */
    AndreDTO save(AndreDTO andreDTO);

    /**
     *  Get all the andres.
     *  
     *  @return the list of entities
     */
    List<AndreDTO> findAll();

    /**
     *  Get the "id" andre.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AndreDTO findOne(Long id);

    /**
     *  Delete the "id" andre.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
