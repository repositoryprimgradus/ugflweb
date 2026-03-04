package com.elexyt.ugflweb.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.elexyt.ugflweb.entity.LoanApplication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class LoanApplicationDTO implements Serializable {


    private String loanApplicationId;

    @Size(max = 255)
    private String name;

    @NotNull
    private Long phone;

    @NotNull
    private Integer goldPurity;

    @NotNull
    private Double weight;

    @Size(max = 255)
    private String address;

    private LocalDateTime createdDate;


}
