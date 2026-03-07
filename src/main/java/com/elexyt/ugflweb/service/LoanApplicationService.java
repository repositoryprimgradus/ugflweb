package com.elexyt.ugflweb.service;

import com.elexyt.ugflweb.entity.LoanApplication;
import com.elexyt.ugflweb.repository.LoanApplicationRepository;
import com.elexyt.ugflweb.dto.LoanApplicationDTO;
import com.elexyt.ugflweb.mapper.LoanApplicationMapper;
import com.elexyt.ugflweb.utility.AuditUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link com.elexyt.ugflweb.entity.LoanApplication}.
 */
@Service
@Transactional
public class LoanApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(LoanApplicationService.class);

    private final LoanApplicationRepository loanApplicationRepository;

    private final LoanApplicationMapper loanApplicationMapper;

    public LoanApplicationService(LoanApplicationRepository loanApplicationRepository, LoanApplicationMapper loanApplicationMapper) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.loanApplicationMapper = loanApplicationMapper;
    }

    /**
     * Save a loanApplication.
     *
     * @param loanApplicationDTO the entity to save.
     * @param name
     * @return the persisted entity.
     */
    public LoanApplicationDTO save(LoanApplicationDTO loanApplicationDTO, String name) {
        LOG.debug("Request to save LoanApplication : {}", loanApplicationDTO);
        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationDTO);
        AuditUtil.setCreated(name, loanApplication);
        loanApplication.setIsActive(1);
        loanApplication = loanApplicationRepository.save(loanApplication);
        return loanApplicationMapper.toDto(loanApplication);
    }

    /**
     * Update a loanApplication.
     *
     * @param loanApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public LoanApplicationDTO update(LoanApplicationDTO loanApplicationDTO) {
        LOG.debug("Request to update LoanApplication : {}", loanApplicationDTO);
        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationDTO);
        loanApplication = loanApplicationRepository.save(loanApplication);
        return loanApplicationMapper.toDto(loanApplication);
    }

    /**
     * Partially update a loanApplication.
     *
     * @param loanApplicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LoanApplicationDTO> partialUpdate(LoanApplicationDTO loanApplicationDTO) {
        LOG.debug("Request to partially update LoanApplication : {}", loanApplicationDTO);

        return loanApplicationRepository
            .findById(loanApplicationDTO.getLoanApplicationId())
            .map(existingLoanApplication -> {
                loanApplicationMapper.partialUpdate(existingLoanApplication, loanApplicationDTO);

                return existingLoanApplication;
            })
            .map(loanApplicationRepository::save)
            .map(loanApplicationMapper::toDto);
    }

    /**
     * Get all the loanApplications.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LoanApplicationDTO> findAll() {
        LOG.debug("Request to get all LoanApplications");
        return loanApplicationRepository
            .findAll()
            .stream()
            .map(loanApplicationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one loanApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LoanApplicationDTO> findOne(String id) {
        LOG.debug("Request to get LoanApplication : {}", id);
        return loanApplicationRepository.findById(id).map(loanApplicationMapper::toDto);
    }

    /**
     * Delete the loanApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete LoanApplication : {}", id);
        loanApplicationRepository.deleteById(id);
    }
}
