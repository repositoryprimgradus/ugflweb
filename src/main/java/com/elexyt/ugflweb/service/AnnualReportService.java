package com.elexyt.ugflweb.service;

import com.elexyt.ugflweb.dto.AnnualReportDTO;
import com.elexyt.ugflweb.entity.AnnualReport;
import com.elexyt.ugflweb.mapper.AnnualReportMapper;
import com.elexyt.ugflweb.repository.AnnualReportRepository;
import com.elexyt.ugflweb.utility.AuditUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnualReportService {

    @Value("${app.upload.path}")
    public String uploadPath;

    private static final Logger LOG = LoggerFactory.getLogger(AnnualReportService.class);

    private final AnnualReportMapper annualReportMapper;
    private final AnnualReportRepository annualReportRepository;

    public AnnualReportService(AnnualReportMapper annualReportMapper, AnnualReportRepository annualReportRepository) {
        this.annualReportMapper = annualReportMapper;
        this.annualReportRepository = annualReportRepository;
    }

    public AnnualReportDTO saveAnnualReportMultipart(AnnualReportDTO dto, String name) throws IOException {
        LOG.debug("Request to save annual report with file: {}", dto);
        MultipartFile file = dto.getFile();
        AnnualReport report = annualReportMapper.toEntity(dto);
        AuditUtil.setCreated(name, report);
        report.setIsActive(1);
        report = annualReportRepository.save(report);

        fileUpload(file, report);

        return annualReportMapper.toDto(report);
    }

    private void fileUpload(MultipartFile file, AnnualReport report) throws IOException {
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();
            if (contentType == null || !(contentType.equals("application/pdf") ||
                    contentType.equals("application/msword") || // .doc
                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) { // .docx
                throw new IOException("Only PDF and Word document formats are allowed.");
            }

            String uploadDir = "annualReport";
            Path fullPath = Paths.get(uploadPath, uploadDir);
            Files.createDirectories(fullPath);

            String fileName = report.getAnnualReportId() + "_" + file.getOriginalFilename();
            Path filePath = fullPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            report.setFileName(fileName);
            annualReportRepository.save(report);
        }
    }


    public List<AnnualReportDTO> findAll() {
        LOG.debug("Request to get all annual reports");
        return annualReportRepository
                .findAll()
                .stream()
                .map(annualReportMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void deleteAnnualReport(String id) throws IOException {
        LOG.debug("Request to delete annual report : {}", id);

        AnnualReport annualReport = annualReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annual report record not found"));

        // delete file if exists
        if (annualReport.getFileName() != null) {
            Path filePath = Paths.get(uploadPath, "annualReport", annualReport.getFileName());
            LOG.debug("Deleting file: {}", filePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }

        annualReportRepository.deleteById(id);
    }
}
