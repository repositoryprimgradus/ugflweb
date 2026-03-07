package com.elexyt.ugflweb.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BseIntimationDTO {

    private String bseIntimationId;
    private String documentName;
    private String documentLanguage;
    private String fileName;
    private String createdBy;
    private String modifiedBy;
    private MultipartFile file;
}