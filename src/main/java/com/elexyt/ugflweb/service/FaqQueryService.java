package com.elexyt.ugflweb.service;


import com.elexyt.ugflweb.dto.FaqQueryDTO;
import com.elexyt.ugflweb.entity.FaqQuery;
import com.elexyt.ugflweb.mapper.FaqQueryMapper;
import com.elexyt.ugflweb.repository.FaqQueryRepository;
import com.elexyt.ugflweb.utility.AuditUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link com.elexyt.ugflweb.entity.FaqQuery}.
 */
@Service
@Transactional
public class FaqQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(FaqQueryService.class);

    private final FaqQueryRepository faqQueryRepository;

    private final FaqQueryMapper faqQueryMapper;

    public FaqQueryService(FaqQueryRepository faqQueryRepository, FaqQueryMapper faqQueryMapper) {
        this.faqQueryRepository = faqQueryRepository;
        this.faqQueryMapper = faqQueryMapper;
    }

    /**
     * Save a faqQuery.
     *
     * @param faqQueryDTO the entity to save.
     * @return the persisted entity.
     */
    public FaqQueryDTO save(FaqQueryDTO faqQueryDTO) {
        LOG.debug("Request to save FaqQuery : {}", faqQueryDTO);
        FaqQuery faqQuery = faqQueryMapper.toEntity(faqQueryDTO);
        AuditUtil.setCreated(null, faqQuery);
        faqQuery.setIsActive(1);
        faqQuery = faqQueryRepository.save(faqQuery);
        return faqQueryMapper.toDto(faqQuery);
    }

    /**
     * Update a faqQuery.
     *
     * @param faqQueryDTO the entity to save.
     * @param username
     * @return the persisted entity.
     */
    public FaqQueryDTO update(FaqQueryDTO faqQueryDTO, String username) {
        LOG.debug("Request to update FaqQuery : {}", faqQueryDTO);
        FaqQuery faqQuery = faqQueryMapper.toEntity(faqQueryDTO);
        AuditUtil.setModified(username, faqQuery);
        faqQuery = faqQueryRepository.save(faqQuery);
        return faqQueryMapper.toDto(faqQuery);
    }

    /**
     * Partially update a faqQuery.
     *
     * @param faqQueryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FaqQueryDTO> partialUpdate(FaqQueryDTO faqQueryDTO) {
        LOG.debug("Request to partially update FaqQuery : {}", faqQueryDTO);

        return faqQueryRepository
            .findById(faqQueryDTO.getFaqQueryId())
            .map(existingFaqQuery -> {
                faqQueryMapper.partialUpdate(existingFaqQuery, faqQueryDTO);

                return existingFaqQuery;
            })
            .map(faqQueryRepository::save)
            .map(faqQueryMapper::toDto);
    }

    /**
     * Get all the faqQueries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FaqQueryDTO> findAll() {
        LOG.debug("Request to get all FaqQueries");
        return faqQueryRepository.findAll().stream().map(faqQueryMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one faqQuery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FaqQueryDTO> findOne(String id) {
        LOG.debug("Request to get FaqQuery : {}", id);
        return faqQueryRepository.findById(id).map(faqQueryMapper::toDto);
    }

    /**
     * Delete the faqQuery by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete FaqQuery : {}", id);
        faqQueryRepository.deleteById(id);
    }
}
