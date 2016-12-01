package za.co.ajk.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import za.co.ajk.store.service.WhishListService;
import za.co.ajk.store.web.rest.util.HeaderUtil;
import za.co.ajk.store.web.rest.util.PaginationUtil;
import za.co.ajk.store.service.dto.WhishListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing WhishList.
 */
@RestController
@RequestMapping("/api")
public class WhishListResource {

    private final Logger log = LoggerFactory.getLogger(WhishListResource.class);
        
    @Inject
    private WhishListService whishListService;

    /**
     * POST  /whish-lists : Create a new whishList.
     *
     * @param whishListDTO the whishListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new whishListDTO, or with status 400 (Bad Request) if the whishList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/whish-lists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WhishListDTO> createWhishList(@RequestBody WhishListDTO whishListDTO) throws URISyntaxException {
        log.debug("REST request to save WhishList : {}", whishListDTO);
        if (whishListDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("whishList", "idexists", "A new whishList cannot already have an ID")).body(null);
        }
        WhishListDTO result = whishListService.save(whishListDTO);
        return ResponseEntity.created(new URI("/api/whish-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("whishList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /whish-lists : Updates an existing whishList.
     *
     * @param whishListDTO the whishListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated whishListDTO,
     * or with status 400 (Bad Request) if the whishListDTO is not valid,
     * or with status 500 (Internal Server Error) if the whishListDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/whish-lists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WhishListDTO> updateWhishList(@RequestBody WhishListDTO whishListDTO) throws URISyntaxException {
        log.debug("REST request to update WhishList : {}", whishListDTO);
        if (whishListDTO.getId() == null) {
            return createWhishList(whishListDTO);
        }
        WhishListDTO result = whishListService.save(whishListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("whishList", whishListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /whish-lists : get all the whishLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of whishLists in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/whish-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WhishListDTO>> getAllWhishLists(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WhishLists");
        Page<WhishListDTO> page = whishListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/whish-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /whish-lists/:id : get the "id" whishList.
     *
     * @param id the id of the whishListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the whishListDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/whish-lists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WhishListDTO> getWhishList(@PathVariable Long id) {
        log.debug("REST request to get WhishList : {}", id);
        WhishListDTO whishListDTO = whishListService.findOne(id);
        return Optional.ofNullable(whishListDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /whish-lists/:id : delete the "id" whishList.
     *
     * @param id the id of the whishListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/whish-lists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWhishList(@PathVariable Long id) {
        log.debug("REST request to delete WhishList : {}", id);
        whishListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("whishList", id.toString())).build();
    }

}
