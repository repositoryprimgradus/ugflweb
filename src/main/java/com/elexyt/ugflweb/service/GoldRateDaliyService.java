package com.elexyt.ugflweb.service;

import com.elexyt.ugflweb.entity.GoldRateDaliy;
import com.elexyt.ugflweb.repository.GoldRateDaliyRepository;
import com.elexyt.ugflweb.dto.GoldRateDaliyDTO;
import com.elexyt.ugflweb.mapper.GoldRateDaliyMapper;
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
 * Service Implementation for managing {@link com.elexyt.ugflweb.entity.GoldRateDaliy}.
 */
@Service
@Transactional
public class GoldRateDaliyService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldRateDaliyService.class);

    private final GoldRateDaliyRepository goldRateDaliyRepository;

    private final GoldRateDaliyMapper goldRateDaliyMapper;

    public GoldRateDaliyService(GoldRateDaliyRepository goldRateDaliyRepository, GoldRateDaliyMapper goldRateDaliyMapper) {
        this.goldRateDaliyRepository = goldRateDaliyRepository;
        this.goldRateDaliyMapper = goldRateDaliyMapper;
    }

    /**
     * Save a goldRateDaliy.
     *
     * @param goldRateDaliyDTO the entity to save.
     * @param username
     * @return the persisted entity.
     */
    public GoldRateDaliyDTO save(GoldRateDaliyDTO goldRateDaliyDTO, String username) {
        LOG.debug("Request to save GoldRateDaliy : {}", goldRateDaliyDTO);
        GoldRateDaliy goldRateDaliy = goldRateDaliyMapper.toEntity(goldRateDaliyDTO);
        AuditUtil.setCreated(username,goldRateDaliy);
        goldRateDaliy = goldRateDaliyRepository.save(goldRateDaliy);
        return goldRateDaliyMapper.toDto(goldRateDaliy);
    }

    /**
     * Update a goldRateDaliy.
     *
     * @param goldRateDaliyDTO the entity to save.
     * @return the persisted entity.
     */
    public GoldRateDaliyDTO update(GoldRateDaliyDTO goldRateDaliyDTO, String username) {
        LOG.debug("Request to update GoldRateDaliy : {}", goldRateDaliyDTO);
        GoldRateDaliy goldRateDaliy = goldRateDaliyMapper.toEntity(goldRateDaliyDTO);
        AuditUtil.setModified(username,goldRateDaliy);
        goldRateDaliy = goldRateDaliyRepository.save(goldRateDaliy);
        return goldRateDaliyMapper.toDto(goldRateDaliy);
    }

    /**
     * Partially update a goldRateDaliy.
     *
     * @param goldRateDaliyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GoldRateDaliyDTO> partialUpdate(GoldRateDaliyDTO goldRateDaliyDTO, String username) {
        LOG.debug("Request to partially update GoldRateDaliy : {}", goldRateDaliyDTO);

        return goldRateDaliyRepository
            .findById(goldRateDaliyDTO.getGoldRateDaliyId())
            .map(existingGoldRateDaliy -> {
                goldRateDaliyMapper.partialUpdate(existingGoldRateDaliy, goldRateDaliyDTO);
                AuditUtil.setModified(username,existingGoldRateDaliy);
                return existingGoldRateDaliy;
            })
            .map(goldRateDaliyRepository::save)
            .map(goldRateDaliyMapper::toDto);
    }

    /**
     * Get all the goldRateDaliys.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GoldRateDaliyDTO> findAll() {
        LOG.debug("Request to get all GoldRateDaliys");
        return goldRateDaliyRepository.findAll().stream().map(goldRateDaliyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one goldRateDaliy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoldRateDaliyDTO> findOne(String id) {
        LOG.debug("Request to get GoldRateDaliy : {}", id);
        return goldRateDaliyRepository.findById(id).map(goldRateDaliyMapper::toDto);
    }

    /**
     * Delete the goldRateDaliy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete GoldRateDaliy : {}", id);
        goldRateDaliyRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<GoldRateDaliyDTO> todayRate() {

        return goldRateDaliyRepository.findTodayOrLast().map(goldRateDaliyMapper::toDto);
    }




}
