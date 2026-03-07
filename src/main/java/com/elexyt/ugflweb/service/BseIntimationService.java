package com.elexyt.ugflweb.service;

import com.elexyt.ugflweb.dto.BseIntimationDTO;
import com.elexyt.ugflweb.entity.BseIntimation;
import com.elexyt.ugflweb.entity.JobApplication;
import com.elexyt.ugflweb.mapper.BseIntimationMapper;
import com.elexyt.ugflweb.repository.BseIntimationRepository;
import com.elexyt.ugflweb.utility.AuditUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BseIntimationService {

    @Value("${app.upload.path}")
    public String uploadPath;


    private static final Logger LOG = LoggerFactory.getLogger(BseIntimationService.class);

    private final BseIntimationRepository bseIntimationRepository;
    private final BseIntimationMapper bseIntimationMapper;

    public BseIntimationService(BseIntimationRepository bseIntimationRepository, BseIntimationMapper bseIntimationMapper) {
        this.bseIntimationRepository = bseIntimationRepository;
        this.bseIntimationMapper = bseIntimationMapper;
    }

    public BseIntimationDTO saveBseIntimationMultipart(BseIntimationDTO dto, String name) throws IOException {
        LOG.debug("Request to save BseIntimation with file: {}", dto);
        MultipartFile file = dto.getFile();
        BseIntimation bseIntimation = bseIntimationMapper.toEntity(dto);
        AuditUtil.setCreated(name, bseIntimation);
        bseIntimation.setIsActive(1);
        bseIntimation = bseIntimationRepository.save(bseIntimation);

        fileUpload(file, bseIntimation);

        return bseIntimationMapper.toDto(bseIntimation);
    }

    private void fileUpload(MultipartFile file, BseIntimation bseIntimation) throws IOException {
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();
            if (contentType == null || !(contentType.equals("application/pdf") ||
                    contentType.equals("application/msword") || // .doc
                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) { // .docx
                throw new IOException("Only PDF and Word document formats are allowed.");
            }

            String uploadDir = "bseIntimation";
            Path fullPath = Paths.get(uploadPath, uploadDir);
            Files.createDirectories(fullPath);

            String fileName = bseIntimation.getBseIntimationId() + "_" + file.getOriginalFilename();
            Path filePath = fullPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            bseIntimation.setFileName(fileName);
            bseIntimationRepository.save(bseIntimation);
        }
    }

    public List<BseIntimationDTO> findAll() {
        LOG.debug("Request to get all BseIntimations");
        return bseIntimationRepository
                .findAll()
                .stream()
                .map(bseIntimationMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void deleteBseIntimation(String id) throws IOException {
        LOG.debug("Request to delete BseIntimation : {}", id);

        BseIntimation bseIntimation = bseIntimationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BSE Intimation record not found"));

        // delete file if exists
        if (bseIntimation.getFileName() != null) {
            Path filePath = Paths.get(uploadPath, "bseIntimation", bseIntimation.getFileName());
            LOG.debug("Deleting file: {}", filePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }

        bseIntimationRepository.deleteById(id);
    }
}
