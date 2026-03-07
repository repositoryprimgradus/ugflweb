package com.elexyt.ugflweb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "bse_intimation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BseIntimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bse_intimation_id")
    private String bseIntimationId;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_language")
    private String documentLanguage;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "is_active")
    private Integer isActive;
}