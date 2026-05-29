package com.elexyt.ugflweb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link com.elexyt.ugflweb.entity.JobApplication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class JobApplicationDTO implements Serializable {


    private String jobApplicationId;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String email;

    @NotNull
    private Long phone;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String position;

    @NotNull
    private Double salary;

    @Size(max = 255)
    private String education;

    @Size(max = 255)
    private String experience;

    @Size(max = 255)
    private String coverLetter;

    @Size(max = 255)
    private String resumePath;

    private String experiences;
    private String higherStudies;

    private MultipartFile file;
    private LocalDateTime createdDate;

    @JsonIgnore
    private List<ExperienceDTO> experienceList;

    @JsonIgnore
    private List<EducationDTO> higherStudiesList;

}
