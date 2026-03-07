package com.elexyt.ugflweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDTO {
    private String company;
    private String designation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    private String duration;
    private String noticePeriod;
}
