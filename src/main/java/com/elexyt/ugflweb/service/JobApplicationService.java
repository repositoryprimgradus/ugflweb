package com.elexyt.ugflweb.service;

import com.elexyt.ugflweb.dto.EducationDTO;
import com.elexyt.ugflweb.dto.ExperienceDTO;
import com.elexyt.ugflweb.entity.Education;
import com.elexyt.ugflweb.entity.Experience;
import com.elexyt.ugflweb.entity.JobApplication;
import com.elexyt.ugflweb.repository.EducationRepository;
import com.elexyt.ugflweb.repository.ExperienceRepository;
import com.elexyt.ugflweb.repository.JobApplicationRepository;
import com.elexyt.ugflweb.dto.JobApplicationDTO;
import com.elexyt.ugflweb.mapper.JobApplicationMapper;
import com.elexyt.ugflweb.utility.AuditUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link com.elexyt.ugflweb.entity.JobApplication}.
 */
@Service
@Transactional
public class JobApplicationService {

    @Value("${app.upload.path}")
    public String uploadPath;

    private static final Logger LOG = LoggerFactory.getLogger(JobApplicationService.class);

    private final JobApplicationRepository jobApplicationRepository;

    private final JobApplicationMapper jobApplicationMapper;

    private final ExperienceRepository experienceRepository;

    private final EducationRepository educationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, JobApplicationMapper jobApplicationMapper, ExperienceRepository experienceRepository, EducationRepository educationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobApplicationMapper = jobApplicationMapper;
        this.experienceRepository = experienceRepository;
        this.educationRepository = educationRepository;
    }

    /**
     * Save a jobApplication.
     *
     * @param jobApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public JobApplicationDTO save(JobApplicationDTO jobApplicationDTO) {
        LOG.debug("Request to save JobApplication : {}", jobApplicationDTO);
        JobApplication jobApplication = jobApplicationMapper.toEntity(jobApplicationDTO);
        jobApplication.setIsActive(1);
        jobApplication = jobApplicationRepository.save(jobApplication);
        return jobApplicationMapper.toDto(jobApplication);
    }

    /**
     * Update a jobApplication.
     *
     * @param jobApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public JobApplicationDTO update(JobApplicationDTO jobApplicationDTO) {
        LOG.debug("Request to update JobApplication : {}", jobApplicationDTO);
        JobApplication jobApplication = jobApplicationMapper.toEntity(jobApplicationDTO);
        jobApplication = jobApplicationRepository.save(jobApplication);
        return jobApplicationMapper.toDto(jobApplication);
    }

    /**
     * Partially update a jobApplication.
     *
     * @param jobApplicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JobApplicationDTO> partialUpdate(JobApplicationDTO jobApplicationDTO) {
        LOG.debug("Request to partially update JobApplication : {}", jobApplicationDTO);

        return jobApplicationRepository
            .findById(jobApplicationDTO.getJobApplicationId())
            .map(existingJobApplication -> {
                jobApplicationMapper.partialUpdate(existingJobApplication, jobApplicationDTO);

                return existingJobApplication;
            })
            .map(jobApplicationRepository::save)
            .map(jobApplicationMapper::toDto);
    }

    /**
     * Get all the jobApplications.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobApplicationDTO> findAll() {
        LOG.debug("Request to get all JobApplications");
        return jobApplicationRepository
            .findAll()
            .stream()
            .map(jobApplicationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one jobApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobApplicationDTO> findOne(String id) {
        LOG.debug("Request to get JobApplication : {}", id);
        return jobApplicationRepository.findById(id).map(jobApplicationMapper::toDto);
    }

    /**
     * Delete the jobApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete JobApplication : {}", id);
        jobApplicationRepository.deleteById(id);
    }

    public JobApplicationDTO saveJobMultipart(JobApplicationDTO jobApplicationDTO, String name) throws IOException {
        LOG.debug("Request to save JobApplication with file: {}", jobApplicationDTO);
        MultipartFile file = jobApplicationDTO.getFile();
        JobApplication jobApplication = jobApplicationMapper.toEntity(jobApplicationDTO);
        AuditUtil.setCreated(name, jobApplication);
        jobApplication.setIsActive(1);
        jobApplication = jobApplicationRepository.save(jobApplication);

        fileUpload(file, jobApplication);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Convert experience JSON → DTO
        List<ExperienceDTO> experienceList =
                objectMapper.readValue(
                        jobApplicationDTO.getExperiences(),
                        new TypeReference<List<ExperienceDTO>>() {}
                );

        // Save experiences
        for (ExperienceDTO dto : experienceList) {
            Experience exp = new Experience();
            exp.setJobApplicationId(jobApplication.getJobApplicationId());
            exp.setCompany(dto.getCompany());
            exp.setDesignation(dto.getDesignation());
            exp.setFromDate(dto.getFromDate());
            exp.setToDate(dto.getToDate());
            experienceRepository.save(exp);
        }

        // Convert education JSON → DTO
        List<EducationDTO> educationList =
                objectMapper.readValue(
                        jobApplicationDTO.getHigherStudies(),
                        new TypeReference<List<EducationDTO>>() {}
                );

        // Save education
        for (EducationDTO dto : educationList) {
            Education edu = new Education();
            edu.setJobApplicationId(jobApplication.getJobApplicationId());
            edu.setDegree(dto.getDegree());
            edu.setInstitution(dto.getInstitution());
            edu.setGraduationDate(dto.getGraduationDate());
            educationRepository.save(edu);
        }

        return jobApplicationMapper.toDto(jobApplication);
    }

    private void fileUpload(MultipartFile file, JobApplication jobApplication) throws IOException {
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();
            if (contentType == null || !(contentType.equals("application/pdf") ||
                    contentType.equals("application/msword") || // .doc
                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) { // .docx
                throw new IOException("Only PDF and Word document formats are allowed.");
            }

            String uploadDir = "jobApplication/resumes";
            Path fullPath = Paths.get(uploadPath, uploadDir);
            Files.createDirectories(fullPath);

            String fileName = jobApplication.getJobApplicationId() + "_" + file.getOriginalFilename();
            Path filePath = fullPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            jobApplication.setResumePath(fileName);
            jobApplicationRepository.save(jobApplication);
        }
    }


}
