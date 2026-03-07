package com.elexyt.ugflweb.controller;

import com.elexyt.ugflweb.repository.LoanApplicationRepository;
import com.elexyt.ugflweb.service.LoanApplicationService;
import com.elexyt.ugflweb.dto.LoanApplicationDTO;
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
 * REST controller for managing {@link com.elexyt.ugflweb.entity.LoanApplication}.
 */
@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationResource {

    private static final Logger LOG = LoggerFactory.getLogger(LoanApplicationResource.class);

    private static final String ENTITY_NAME = "loanApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanApplicationService loanApplicationService;

    private final LoanApplicationRepository loanApplicationRepository;

    public LoanApplicationResource(LoanApplicationService loanApplicationService, LoanApplicationRepository loanApplicationRepository) {
        this.loanApplicationService = loanApplicationService;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    /**
     * {@code POST  /loan-applications} : Create a new loanApplication.
     *
     * @param loanApplicationDTO the loanApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanApplicationDTO, or with status {@code 400 (Bad Request)} if the loanApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LoanApplicationDTO> createLoanApplication(@Valid @RequestBody LoanApplicationDTO loanApplicationDTO, Authentication auth)
        throws URISyntaxException {
        LOG.debug("REST request to save LoanApplication : {}", loanApplicationDTO);
        if (loanApplicationDTO.getLoanApplicationId() != null) {
            throw new BadRequestAlertException("A new loanApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        loanApplicationDTO = loanApplicationService.save(loanApplicationDTO, auth.getName());
        return ResponseEntity.created(new URI("/api/loan-applications/" + loanApplicationDTO.getLoanApplicationId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    loanApplicationDTO.getLoanApplicationId().toString()
                )
            )
            .body(loanApplicationDTO);
    }

    /**
     * {@code PUT  /loan-applications/:loanApplicationId} : Updates an existing loanApplication.
     *
     * @param loanApplicationId the id of the loanApplicationDTO to save.
     * @param loanApplicationDTO the loanApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the loanApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{loanApplicationId}")
    public ResponseEntity<LoanApplicationDTO> updateLoanApplication(
        @PathVariable(value = "loanApplicationId", required = false) final String loanApplicationId,
        @Valid @RequestBody LoanApplicationDTO loanApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LoanApplication : {}, {}", loanApplicationId, loanApplicationDTO);
        if (loanApplicationDTO.getLoanApplicationId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(loanApplicationId, loanApplicationDTO.getLoanApplicationId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanApplicationRepository.existsById(loanApplicationId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        loanApplicationDTO = loanApplicationService.update(loanApplicationDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanApplicationDTO.getLoanApplicationId().toString())
            )
            .body(loanApplicationDTO);
    }

    /**
     * {@code PATCH  /loan-applications/:loanApplicationId} : Partial updates given fields of an existing loanApplication, field will ignore if it is null
     *
     * @param loanApplicationId the id of the loanApplicationDTO to save.
     * @param loanApplicationDTO the loanApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the loanApplicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanApplicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{loanApplicationId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanApplicationDTO> partialUpdateLoanApplication(
        @PathVariable(value = "loanApplicationId", required = false) final String loanApplicationId,
        @NotNull @RequestBody LoanApplicationDTO loanApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LoanApplication partially : {}, {}", loanApplicationId, loanApplicationDTO);
        if (loanApplicationDTO.getLoanApplicationId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(loanApplicationId, loanApplicationDTO.getLoanApplicationId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanApplicationRepository.existsById(loanApplicationId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanApplicationDTO> result = loanApplicationService.partialUpdate(loanApplicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanApplicationDTO.getLoanApplicationId().toString())
        );
    }

    /**
     * {@code GET  /loan-applications} : get all the loanApplications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanApplications in body.
     */
    @GetMapping("")
    public List<LoanApplicationDTO> getAllLoanApplications() {
        LOG.debug("REST request to get all LoanApplications");
        return loanApplicationService.findAll();
    }

    /**
     * {@code GET  /loan-applications/:id} : get the "id" loanApplication.
     *
     * @param id the id of the loanApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationDTO> getLoanApplication(@PathVariable("id") String id) {
        LOG.debug("REST request to get LoanApplication : {}", id);
        Optional<LoanApplicationDTO> loanApplicationDTO = loanApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanApplicationDTO);
    }

    /**
     * {@code DELETE  /loan-applications/:id} : delete the "id" loanApplication.
     *
     * @param id the id of the loanApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanApplication(@PathVariable("id") String id) {
        LOG.debug("REST request to delete LoanApplication : {}", id);
        loanApplicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
