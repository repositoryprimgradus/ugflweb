package com.elexyt.ugflweb.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.elexyt.ugflweb.entity.FaqQuery} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class FaqQueryDTO implements Serializable {


    private String faqQueryId;

    @Size(max = 255)
    private String name;

    @NotNull
    private Long phone;


    @Size(max = 255)
    private String subject;

    @Size(max = 255)
    private String message;

    private LocalDateTime createdDate;

}
