package com.elexyt.ugflweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "experience")
public class Experience {

    @Id
    @Column(name = "experience_id", length = 36)
    private String experienceId;

    @Column(name = "job_application_id", length = 36)
    private String jobApplicationId;

    @Column(name = "company")
    private String company;

    @Column(name = "designation")
    private String designation;

    @Column(name = "fromDate")
    private LocalDate fromDate;

    @Column(name = "toDate")
    private LocalDate toDate;

    @Column(name = "is_active")
    private Integer isActive = 1;

    @PrePersist
    public void prePersist() {
        this.experienceId = UUID.randomUUID().toString();
    }
}

