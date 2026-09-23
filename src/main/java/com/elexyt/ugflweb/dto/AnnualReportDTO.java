package com.elexyt.ugflweb.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnualReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String annualReportId;
    private String documentName;
    private String description;
    private String fileName;
    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private Integer isActive;
    private MultipartFile file;

}
