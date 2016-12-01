package za.co.ajk.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import za.co.ajk.store.service.AndreService;
import za.co.ajk.store.web.rest.util.HeaderUtil;
import za.co.ajk.store.service.dto.AndreDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Andre.
 */
@RestController
@RequestMapping("/api")
public class AndreResource {

    private final Logger log = LoggerFactory.getLogger(AndreResource.class);
        
    @Inject
    private AndreService andreService;

    /**
     * POST  /andres : Create a new andre.
     *
     * @param andreDTO the andreDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new andreDTO, or with status 400 (Bad Request) if the andre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/andres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AndreDTO> createAndre(@Valid @RequestBody AndreDTO andreDTO) throws URISyntaxException {
        log.debug("REST request to save Andre : {}", andreDTO);
        if (andreDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("andre", "idexists", "A new andre cannot already have an ID")).body(null);
        }
        AndreDTO result = andreService.save(andreDTO);
        return ResponseEntity.created(new URI("/api/andres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("andre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /andres : Updates an existing andre.
     *
     * @param andreDTO the andreDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated andreDTO,
     * or with status 400 (Bad Request) if the andreDTO is not valid,
     * or with status 500 (Internal Server Error) if the andreDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/andres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AndreDTO> updateAndre(@Valid @RequestBody AndreDTO andreDTO) throws URISyntaxException {
        log.debug("REST request to update Andre : {}", andreDTO);
        if (andreDTO.getId() == null) {
            return createAndre(andreDTO);
        }
        AndreDTO result = andreService.save(andreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("andre", andreDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /andres : get all the andres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of andres in body
     */
    @RequestMapping(value = "/andres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AndreDTO> getAllAndres() {
        log.debug("REST request to get all Andres");
        return andreService.findAll();
    }

    /**
     * GET  /andres/:id : get the "id" andre.
     *
     * @param id the id of the andreDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the andreDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/andres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AndreDTO> getAndre(@PathVariable Long id) {
        log.debug("REST request to get Andre : {}", id);
        AndreDTO andreDTO = andreService.findOne(id);
        return Optional.ofNullable(andreDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /andres/:id : delete the "id" andre.
     *
     * @param id the id of the andreDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/andres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAndre(@PathVariable Long id) {
        log.debug("REST request to delete Andre : {}", id);
        andreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("andre", id.toString())).build();
    }

}
