package com.centreon.map.service;

import com.centreon.map.domain.View;
import com.centreon.map.repository.ViewRepository;
import com.centreon.map.service.dto.ViewDTO;
import com.centreon.map.service.mapper.ViewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link View}.
 */
@Service
@Transactional
public class ViewService {

    private final Logger log = LoggerFactory.getLogger(ViewService.class);

    private final ViewRepository viewRepository;

    private final ViewMapper viewMapper;

    public ViewService(ViewRepository viewRepository, ViewMapper viewMapper) {
        this.viewRepository = viewRepository;
        this.viewMapper = viewMapper;
    }

    /**
     * Save a view.
     *
     * @param viewDTO the entity to save.
     * @return the persisted entity.
     */
    public ViewDTO save(ViewDTO viewDTO) {
        log.debug("Request to save View : {}", viewDTO);
        View view = viewMapper.toEntity(viewDTO);
        view = viewRepository.save(view);
        return viewMapper.toDto(view);
    }

    /**
     * Get all the views.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ViewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Views");
        return viewRepository.findAll(pageable)
            .map(viewMapper::toDto);
    }


    /**
     * Get one view by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ViewDTO> findOne(Long id) {
        log.debug("Request to get View : {}", id);
        return viewRepository.findById(id)
            .map(viewMapper::toDto);
    }

    /**
     * Delete the view by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete View : {}", id);
        viewRepository.deleteById(id);
    }
}
