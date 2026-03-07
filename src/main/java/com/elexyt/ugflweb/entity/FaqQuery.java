package com.elexyt.ugflweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A FaqQuery.
 */
@Data
@Entity
@Table(name = "faq_query")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FaqQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "faq_query_id", nullable = false)
    private String faqQueryId;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @NotNull
    @Column(name = "phone", nullable = false)
    private Long phone;


    @Size(max = 255)
    @Column(name = "subject", length = 255)
    private String subject;

    @Size(max = 255)
    @Column(name = "message", length = 255)
    private String message;

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
