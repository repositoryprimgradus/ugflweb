package com.elexyt.ugflweb.controller;

import com.elexyt.ugflweb.repository.GoldRateDaliyRepository;
import com.elexyt.ugflweb.service.GoldRateDaliyService;
import com.elexyt.ugflweb.dto.GoldRateDaliyDTO;
import com.elexyt.ugflweb.error.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.elexyt.ugflweb.entity.GoldRateDaliy}.
 */
@RestController
@RequestMapping("/api/gold-rate-daliys")
public class GoldRateDaliyResource {

    private static final Logger LOG = LoggerFactory.getLogger(GoldRateDaliyResource.class);

    private static final String ENTITY_NAME = "goldRateDaliy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoldRateDaliyService goldRateDaliyService;

    private final GoldRateDaliyRepository goldRateDaliyRepository;

    public GoldRateDaliyResource(GoldRateDaliyService goldRateDaliyService, GoldRateDaliyRepository goldRateDaliyRepository) {
        this.goldRateDaliyService = goldRateDaliyService;
        this.goldRateDaliyRepository = goldRateDaliyRepository;
    }

    /**
     * {@code POST  /gold-rate-daliys} : Create a new goldRateDaliy.
     *
     * @param goldRateDaliyDTO the goldRateDaliyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goldRateDaliyDTO, or with status {@code 400 (Bad Request)} if the goldRateDaliy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GoldRateDaliyDTO> createGoldRateDaliy(@Valid @RequestBody GoldRateDaliyDTO goldRateDaliyDTO, Authentication auth)
        throws URISyntaxException {
        LOG.debug("REST request to save GoldRateDaliy : {}", goldRateDaliyDTO);
        if (goldRateDaliyDTO.getGoldRateDaliyId() != null) {
            throw new BadRequestAlertException("A new goldRateDaliy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        goldRateDaliyDTO = goldRateDaliyService.save(goldRateDaliyDTO,auth.getName());
        return ResponseEntity.created(new URI("/api/gold-rate-daliys/" + goldRateDaliyDTO.getGoldRateDaliyId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, goldRateDaliyDTO.getGoldRateDaliyId().toString())
            )
            .body(goldRateDaliyDTO);
    }

    /**
     * {@code PUT  /gold-rate-daliys/:goldRateDaliyId} : Updates an existing goldRateDaliy.
     *
     * @param goldRateDaliyId the id of the goldRateDaliyDTO to save.
     * @param goldRateDaliyDTO the goldRateDaliyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goldRateDaliyDTO,
     * or with status {@code 400 (Bad Request)} if the goldRateDaliyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goldRateDaliyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{goldRateDaliyId}")
    public ResponseEntity<GoldRateDaliyDTO> updateGoldRateDaliy(
        @PathVariable(value = "goldRateDaliyId", required = false) final String goldRateDaliyId,
        @Valid @RequestBody GoldRateDaliyDTO goldRateDaliyDTO, Authentication auth
    ) throws URISyntaxException {
        LOG.debug("REST request to update GoldRateDaliy : {}, {}", goldRateDaliyId, goldRateDaliyDTO);
        if (goldRateDaliyDTO.getGoldRateDaliyId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(goldRateDaliyId, goldRateDaliyDTO.getGoldRateDaliyId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goldRateDaliyRepository.existsById(goldRateDaliyId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        goldRateDaliyDTO = goldRateDaliyService.update(goldRateDaliyDTO,auth.getName());
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goldRateDaliyDTO.getGoldRateDaliyId().toString())
            )
            .body(goldRateDaliyDTO);
    }

    /**
     * {@code PATCH  /gold-rate-daliys/:goldRateDaliyId} : Partial updates given fields of an existing goldRateDaliy, field will ignore if it is null
     *
     * @param goldRateDaliyId the id of the goldRateDaliyDTO to save.
     * @param goldRateDaliyDTO the goldRateDaliyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goldRateDaliyDTO,
     * or with status {@code 400 (Bad Request)} if the goldRateDaliyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the goldRateDaliyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the goldRateDaliyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{goldRateDaliyId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GoldRateDaliyDTO> partialUpdateGoldRateDaliy(
        @PathVariable(value = "goldRateDaliyId", required = false) final String goldRateDaliyId,
        @NotNull @RequestBody GoldRateDaliyDTO goldRateDaliyDTO, Authentication auth
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GoldRateDaliy partially : {}, {}", goldRateDaliyId, goldRateDaliyDTO);
        if (goldRateDaliyDTO.getGoldRateDaliyId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(goldRateDaliyId, goldRateDaliyDTO.getGoldRateDaliyId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goldRateDaliyRepository.existsById(goldRateDaliyId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GoldRateDaliyDTO> result = goldRateDaliyService.partialUpdate(goldRateDaliyDTO,auth.getName());

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goldRateDaliyDTO.getGoldRateDaliyId().toString())
        );
    }

    /**
     * {@code GET  /gold-rate-daliys} : get all the goldRateDaliys.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goldRateDaliys in body.
     */
    @GetMapping("")
    public List<GoldRateDaliyDTO> getAllGoldRateDaliys() {
        LOG.debug("REST request to get all GoldRateDaliys");
        return goldRateDaliyService.findAll();
    }

    /**
     * {@code GET  /gold-rate-daliys/:id} : get the "id" goldRateDaliy.
     *
     * @param id the id of the goldRateDaliyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goldRateDaliyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoldRateDaliyDTO> getGoldRateDaliy(@PathVariable("id") String id) {
        LOG.debug("REST request to get GoldRateDaliy : {}", id);
        Optional<GoldRateDaliyDTO> goldRateDaliyDTO = goldRateDaliyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goldRateDaliyDTO);
    }

    /**
     * {@code DELETE  /gold-rate-daliys/:id} : delete the "id" goldRateDaliy.
     *
     * @param id the id of the goldRateDaliyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoldRateDaliy(@PathVariable("id") String id) {
        LOG.debug("REST request to delete GoldRateDaliy : {}", id);
        goldRateDaliyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


    @GetMapping("/today-rate")
    public ResponseEntity<GoldRateDaliyDTO> getGoldRateDaliyTodayRate() {
        LOG.debug("REST request to get GoldRateDaliy");
        Optional<GoldRateDaliyDTO> goldRateDaliyDTO = goldRateDaliyService.todayRate();
        return ResponseUtil.wrapOrNotFound(goldRateDaliyDTO);
    }
}
