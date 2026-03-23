package com.elexyt.ugflweb.controller;

import com.elexyt.ugflweb.repository.FaqQueryRepository;
import com.elexyt.ugflweb.service.FaqQueryService;
import com.elexyt.ugflweb.dto.FaqQueryDTO;
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
 * REST controller for managing {@link com.elexyt.ugflweb.entity.FaqQuery}.
 */
@RestController
@RequestMapping("/api/faq-queries")
public class FaqQueryResource {

    private static final Logger LOG = LoggerFactory.getLogger(FaqQueryResource.class);

    private static final String ENTITY_NAME = "faqQuery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FaqQueryService faqQueryService;

    private final FaqQueryRepository faqQueryRepository;

    public FaqQueryResource(FaqQueryService faqQueryService, FaqQueryRepository faqQueryRepository) {
        this.faqQueryService = faqQueryService;
        this.faqQueryRepository = faqQueryRepository;
    }

    /**
     * {@code POST  /faq-queries} : Create a new faqQuery.
     *
     * @param faqQueryDTO the faqQueryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new faqQueryDTO, or with status {@code 400 (Bad Request)} if the faqQuery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FaqQueryDTO> createFaqQuery(@Valid @RequestBody FaqQueryDTO faqQueryDTO) throws URISyntaxException {
        LOG.debug("REST request to save FaqQuery : {}", faqQueryDTO);
        if (faqQueryDTO.getFaqQueryId() != null) {
            throw new BadRequestAlertException("A new faqQuery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        faqQueryDTO = faqQueryService.save(faqQueryDTO);
        return ResponseEntity.created(new URI("/api/faq-queries/" + faqQueryDTO.getFaqQueryId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, faqQueryDTO.getFaqQueryId().toString()))
            .body(faqQueryDTO);
    }

    /**
     * {@code PUT  /faq-queries/:faqQueryId} : Updates an existing faqQuery.
     *
     * @param faqQueryId the id of the faqQueryDTO to save.
     * @param faqQueryDTO the faqQueryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated faqQueryDTO,
     * or with status {@code 400 (Bad Request)} if the faqQueryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the faqQueryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{faqQueryId}")
    public ResponseEntity<FaqQueryDTO> updateFaqQuery(
        @PathVariable(value = "faqQueryId", required = false) final String faqQueryId,
        @Valid @RequestBody FaqQueryDTO faqQueryDTO, Authentication auth
    ) throws URISyntaxException {
        LOG.debug("REST request to update FaqQuery : {}, {}", faqQueryId, faqQueryDTO);
        if (faqQueryDTO.getFaqQueryId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(faqQueryId, faqQueryDTO.getFaqQueryId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!faqQueryRepository.existsById(faqQueryId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        faqQueryDTO = faqQueryService.update(faqQueryDTO, auth.getName());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, faqQueryDTO.getFaqQueryId().toString()))
            .body(faqQueryDTO);
    }

    /**
     * {@code PATCH  /faq-queries/:faqQueryId} : Partial updates given fields of an existing faqQuery, field will ignore if it is null
     *
     * @param faqQueryId the id of the faqQueryDTO to save.
     * @param faqQueryDTO the faqQueryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated faqQueryDTO,
     * or with status {@code 400 (Bad Request)} if the faqQueryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the faqQueryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the faqQueryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{faqQueryId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FaqQueryDTO> partialUpdateFaqQuery(
        @PathVariable(value = "faqQueryId", required = false) final String faqQueryId,
        @NotNull @RequestBody FaqQueryDTO faqQueryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FaqQuery partially : {}, {}", faqQueryId, faqQueryDTO);
        if (faqQueryDTO.getFaqQueryId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(faqQueryId, faqQueryDTO.getFaqQueryId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!faqQueryRepository.existsById(faqQueryId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FaqQueryDTO> result = faqQueryService.partialUpdate(faqQueryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, faqQueryDTO.getFaqQueryId().toString())
        );
    }

    /**
     * {@code GET  /faq-queries} : get all the faqQueries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of faqQueries in body.
     */
    @GetMapping("")
    public List<FaqQueryDTO> getAllFaqQueries() {
        LOG.debug("REST request to get all FaqQueries");
        return faqQueryService.findAll();
    }

    /**
     * {@code GET  /faq-queries/:id} : get the "id" faqQuery.
     *
     * @param id the id of the faqQueryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the faqQueryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FaqQueryDTO> getFaqQuery(@PathVariable("id") String id) {
        LOG.debug("REST request to get FaqQuery : {}", id);
        Optional<FaqQueryDTO> faqQueryDTO = faqQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(faqQueryDTO);
    }

    /**
     * {@code DELETE  /faq-queries/:id} : delete the "id" faqQuery.
     *
     * @param id the id of the faqQueryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaqQuery(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FaqQuery : {}", id);
        faqQueryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
