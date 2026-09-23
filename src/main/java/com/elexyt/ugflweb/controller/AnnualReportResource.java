package com.elexyt.ugflweb.controller;

import com.elexyt.ugflweb.dto.AnnualReportDTO;
import com.elexyt.ugflweb.error.BadRequestAlertException;
import com.elexyt.ugflweb.service.AnnualReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/annual-reports")
public class AnnualReportResource {

    @Value("${app.upload.path}")
    public String uploadPath;

    private static final Logger LOG = LoggerFactory.getLogger(AnnualReportResource.class);

    private static final String ENTITY_NAME = "annualReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnualReportService annualReportService;

    public AnnualReportResource(AnnualReportService annualReportService) {
        this.annualReportService = annualReportService;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnnualReportDTO> insertAnnualReport(
            @ModelAttribute AnnualReportDTO annualReportDTO,
            Authentication auth
    ) throws URISyntaxException, IOException {

        LOG.debug("REST request to save Annual Report : {}", annualReportDTO);

        if (annualReportDTO.getAnnualReportId() != null) {
            throw new BadRequestAlertException(
                    "A new annual report cannot already have an ID",
                    ENTITY_NAME,
                    "idexists"
            );
        }

        annualReportDTO = annualReportService.saveAnnualReportMultipart(
                annualReportDTO,
                auth.getName()
        );

        return ResponseEntity.created(new URI("/api/annual-report/" + annualReportDTO.getAnnualReportId()))
                .headers(
                        HeaderUtil.createEntityCreationAlert(
                                applicationName,
                                true,
                                ENTITY_NAME,
                                annualReportDTO.getAnnualReportId()
                        )
                )
                .body(annualReportDTO);
    }

    @GetMapping("")
    public List<AnnualReportDTO> getAllAnnualReports() {
        LOG.debug("REST request to get all annual reports");
        return annualReportService.findAll();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {

        String uploadDir = "annualReport";
        Path filePath = Paths.get(uploadPath, uploadDir).resolve(fileName).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnualReport(@PathVariable String id) throws IOException {

        LOG.debug("REST request to delete annual report : {}", id);

        annualReportService.deleteAnnualReport(id);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id))
                .build();
    }

}
