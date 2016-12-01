package za.co.ajk.store.service.impl;

import za.co.ajk.store.service.AndreService;
import za.co.ajk.store.domain.Andre;
import za.co.ajk.store.repository.AndreRepository;
import za.co.ajk.store.service.dto.AndreDTO;
import za.co.ajk.store.service.mapper.AndreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Andre.
 */
@Service
@Transactional
public class AndreServiceImpl implements AndreService{

    private final Logger log = LoggerFactory.getLogger(AndreServiceImpl.class);
    
    @Inject
    private AndreRepository andreRepository;

    @Inject
    private AndreMapper andreMapper;

    /**
     * Save a andre.
     *
     * @param andreDTO the entity to save
     * @return the persisted entity
     */
    public AndreDTO save(AndreDTO andreDTO) {
        log.debug("Request to save Andre : {}", andreDTO);
        Andre andre = andreMapper.andreDTOToAndre(andreDTO);
        andre = andreRepository.save(andre);
        AndreDTO result = andreMapper.andreToAndreDTO(andre);
        return result;
    }

    /**
     *  Get all the andres.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<AndreDTO> findAll() {
        log.debug("Request to get all Andres");
        List<AndreDTO> result = andreRepository.findAll().stream()
            .map(andreMapper::andreToAndreDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one andre by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AndreDTO findOne(Long id) {
        log.debug("Request to get Andre : {}", id);
        Andre andre = andreRepository.findOne(id);
        AndreDTO andreDTO = andreMapper.andreToAndreDTO(andre);
        return andreDTO;
    }

    /**
     *  Delete the  andre by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Andre : {}", id);
        andreRepository.delete(id);
    }
}
