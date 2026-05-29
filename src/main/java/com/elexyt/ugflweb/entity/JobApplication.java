package com.elexyt.ugflweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A JobApplication.
 */
@Data
@Entity
@Table(name = "job_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "job_application_id", nullable = false)
    private String jobApplicationId;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private Long phone;

    @Size(max = 255)
    @Column(name = "city", length = 255)
    private String city;

    @Size(max = 255)
    @Column(name = "position", length = 255)
    private String position;

    @NotNull
    @Column(name = "salary", nullable = false)
    private Double salary;

    @Size(max = 255)
    @Column(name = "education", length = 255)
    private String education;

    @Size(max = 255)
    @Column(name = "experience", length = 255)
    private String experience;

    @Size(max = 255)
    @Column(name = "cover_letter", length = 255)
    private String coverLetter;

    @Size(max = 255)
    @Column(name = "resume_path", length = 255)
    private String resumePath;

    @NotNull
    @Column(name = "is_active", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer isActive;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Size(max = 100)
    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;


}
