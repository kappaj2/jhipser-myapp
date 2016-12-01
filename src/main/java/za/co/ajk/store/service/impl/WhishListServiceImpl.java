package za.co.ajk.store.service.impl;

import za.co.ajk.store.service.WhishListService;
import za.co.ajk.store.domain.WhishList;
import za.co.ajk.store.repository.WhishListRepository;
import za.co.ajk.store.service.dto.WhishListDTO;
import za.co.ajk.store.service.mapper.WhishListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing WhishList.
 */
@Service
@Transactional
public class WhishListServiceImpl implements WhishListService{

    private final Logger log = LoggerFactory.getLogger(WhishListServiceImpl.class);
    
    @Inject
    private WhishListRepository whishListRepository;

    @Inject
    private WhishListMapper whishListMapper;

    /**
     * Save a whishList.
     *
     * @param whishListDTO the entity to save
     * @return the persisted entity
     */
    public WhishListDTO save(WhishListDTO whishListDTO) {
        log.debug("Request to save WhishList : {}", whishListDTO);
        WhishList whishList = whishListMapper.whishListDTOToWhishList(whishListDTO);
        whishList = whishListRepository.save(whishList);
        WhishListDTO result = whishListMapper.whishListToWhishListDTO(whishList);
        return result;
    }

    /**
     *  Get all the whishLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<WhishListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WhishLists");
        Page<WhishList> result = whishListRepository.findAll(pageable);
        return result.map(whishList -> whishListMapper.whishListToWhishListDTO(whishList));
    }

    /**
     *  Get one whishList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WhishListDTO findOne(Long id) {
        log.debug("Request to get WhishList : {}", id);
        WhishList whishList = whishListRepository.findOne(id);
        WhishListDTO whishListDTO = whishListMapper.whishListToWhishListDTO(whishList);
        return whishListDTO;
    }

    /**
     *  Delete the  whishList by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WhishList : {}", id);
        whishListRepository.delete(id);
    }
}
