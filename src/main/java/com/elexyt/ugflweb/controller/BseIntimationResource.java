package com.elexyt.ugflweb.controller;

import com.elexyt.ugflweb.dto.BseIntimationDTO;
import com.elexyt.ugflweb.dto.JobApplicationDTO;
import com.elexyt.ugflweb.error.BadRequestAlertException;
import com.elexyt.ugflweb.service.BseIntimationService;
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
@RequestMapping("/api/bse-intimation")
public class BseIntimationResource {

    @Value("${app.upload.path}")
    public String uploadPath;

    private static final Logger LOG = LoggerFactory.getLogger(BseIntimationResource.class);

    private static final String ENTITY_NAME = "bseIntimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BseIntimationService bseIntimationService;

    public BseIntimationResource(BseIntimationService bseIntimationService) {
        this.bseIntimationService = bseIntimationService;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BseIntimationDTO> createBseIntimation(
            @ModelAttribute BseIntimationDTO bseIntimationDTO,
            Authentication auth
    ) throws URISyntaxException, IOException {

        LOG.debug("REST request to save BseIntimation : {}", bseIntimationDTO);

        if (bseIntimationDTO.getBseIntimationId() != null) {
            throw new BadRequestAlertException(
                    "A new bseIntimation cannot already have an ID",
                    ENTITY_NAME,
                    "idexists"
            );
        }

        bseIntimationDTO = bseIntimationService.saveBseIntimationMultipart(
                bseIntimationDTO,
                auth.getName()
        );

        return ResponseEntity.created(new URI("/api/bse-intimations/" + bseIntimationDTO.getBseIntimationId()))
                .headers(
                        HeaderUtil.createEntityCreationAlert(
                                applicationName,
                                true,
                                ENTITY_NAME,
                                bseIntimationDTO.getBseIntimationId()
                        )
                )
                .body(bseIntimationDTO);
    }

    @GetMapping("")
    public List<BseIntimationDTO> getAllBseIntimations() {
        LOG.debug("REST request to get all BseIntimations");
        return bseIntimationService.findAll();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {

        String uploadDir = "bseIntimation";
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
    public ResponseEntity<Void> deleteBseIntimation(@PathVariable String id) throws IOException {

        LOG.debug("REST request to delete BseIntimation : {}", id);

        bseIntimationService.deleteBseIntimation(id);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id))
                .build();
    }

}
