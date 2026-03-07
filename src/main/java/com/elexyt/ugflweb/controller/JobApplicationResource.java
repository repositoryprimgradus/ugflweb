package com.elexyt.ugflweb.controller;

import com.elexyt.ugflweb.dto.ExperienceDTO;
import com.elexyt.ugflweb.dto.EducationDTO;
import com.elexyt.ugflweb.repository.JobApplicationRepository;
import com.elexyt.ugflweb.service.JobApplicationService;
import com.elexyt.ugflweb.dto.JobApplicationDTO;
import com.elexyt.ugflweb.error.BadRequestAlertException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.elexyt.ugflweb.entity.JobApplication}.
 */
@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationResource {

    @Value("${app.upload.path}")
    public String uploadPath;

    private static final Logger LOG = LoggerFactory.getLogger(JobApplicationResource.class);

    private static final String ENTITY_NAME = "jobApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobApplicationService jobApplicationService;

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationResource(JobApplicationService jobApplicationService, JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationService = jobApplicationService;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    /**
     * {@code POST  /job-applications} : Create a new jobApplication.
     *
     * @param jobApplicationDTO the jobApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobApplicationDTO, or with status {@code 400 (Bad Request)} if the jobApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JobApplicationDTO> createJobApplication(@Valid @ModelAttribute JobApplicationDTO jobApplicationDTO, Authentication auth)
            throws URISyntaxException, IOException {
        LOG.debug("REST request to save JobApplication : {}", jobApplicationDTO);
        if (jobApplicationDTO.getJobApplicationId() != null) {
            throw new BadRequestAlertException("A new jobApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        jobApplicationDTO = jobApplicationService.saveJobMultipart(jobApplicationDTO, auth.getName());
        return ResponseEntity.created(new URI("/api/job-applications/" + jobApplicationDTO.getJobApplicationId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, jobApplicationDTO.getJobApplicationId())
            )
            .body(jobApplicationDTO);
    }

    /**
     * {@code PUT  /job-applications/:jobApplicationId} : Updates an existing jobApplication.
     *
     * @param jobApplicationId the id of the jobApplicationDTO to save.
     * @param jobApplicationDTO the jobApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the jobApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{jobApplicationId}")
    public ResponseEntity<JobApplicationDTO> updateJobApplication(
        @PathVariable(value = "jobApplicationId", required = false) final String jobApplicationId,
        @Valid @RequestBody JobApplicationDTO jobApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update JobApplication : {}, {}", jobApplicationId, jobApplicationDTO);
        if (jobApplicationDTO.getJobApplicationId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(jobApplicationId, jobApplicationDTO.getJobApplicationId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobApplicationRepository.existsById(jobApplicationId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        jobApplicationDTO = jobApplicationService.update(jobApplicationDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobApplicationDTO.getJobApplicationId().toString())
            )
            .body(jobApplicationDTO);
    }

    /**
     * {@code PATCH  /job-applications/:jobApplicationId} : Partial updates given fields of an existing jobApplication, field will ignore if it is null
     *
     * @param jobApplicationId the id of the jobApplicationDTO to save.
     * @param jobApplicationDTO the jobApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the jobApplicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobApplicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{jobApplicationId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobApplicationDTO> partialUpdateJobApplication(
        @PathVariable(value = "jobApplicationId", required = false) final String jobApplicationId,
        @NotNull @RequestBody JobApplicationDTO jobApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JobApplication partially : {}, {}", jobApplicationId, jobApplicationDTO);
        if (jobApplicationDTO.getJobApplicationId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(jobApplicationId, jobApplicationDTO.getJobApplicationId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobApplicationRepository.existsById(jobApplicationId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobApplicationDTO> result = jobApplicationService.partialUpdate(jobApplicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobApplicationDTO.getJobApplicationId().toString())
        );
    }

    /**
     * {@code GET  /job-applications} : get all the jobApplications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobApplications in body.
     */
    @GetMapping("")
    public List<JobApplicationDTO> getAllJobApplications() {
        LOG.debug("REST request to get all JobApplications");
        return jobApplicationService.findAll();
    }

    /**
     * {@code GET  /job-applications/:id} : get the "id" jobApplication.
     *
     * @param id the id of the jobApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationDTO> getJobApplication(@PathVariable("id") String id) {
        LOG.debug("REST request to get JobApplication : {}", id);
        Optional<JobApplicationDTO> jobApplicationDTO = jobApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobApplicationDTO);
    }

    /**
     * {@code DELETE  /job-applications/:id} : delete the "id" jobApplication.
     *
     * @param id the id of the jobApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable("id") String id) {
        LOG.debug("REST request to delete JobApplication : {}", id);
        jobApplicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/download-resume/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {

        Path filePath = Paths.get(uploadPath, "jobApplication", "resumes").resolve(fileName).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


}
