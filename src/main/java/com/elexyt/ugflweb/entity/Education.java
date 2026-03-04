package com.elexyt.ugflweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "education")
public class Education {

    @Id
    @Column(name = "education_id", length = 36)
    private String educationId;

    @Column(name = "job_application_id", length = 36)
    private String jobApplicationId;

    @Column(name = "degree", length = 500)
    private String degree;

    @Column(name = "institution", length = 500)
    private String institution;

    @Column(name = "graduationDate")
    private LocalDate graduationDate;

    @Column(name = "is_active")
    private Integer isActive = 1;

    @PrePersist
    public void prePersist() {
        this.educationId = UUID.randomUUID().toString();
    }
}
