package com.elexyt.ugflweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationDTO {
    private String degree;
    private String institution;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduationDate;
}
