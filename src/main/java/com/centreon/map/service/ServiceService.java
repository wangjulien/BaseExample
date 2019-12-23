package com.centreon.map.service;

import com.centreon.map.repository.ServiceRepository;
import com.centreon.map.service.dto.ServiceDTO;
import com.centreon.map.service.mapper.ServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Service}.
 */
@Service
@Transactional
public class ServiceService {

    private final Logger log = LoggerFactory.getLogger(ServiceService.class);

    private final ServiceRepository serviceRepository;

    private final ServiceMapper serviceMapper;

    public ServiceService(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    /**
     * Save a service.
     *
     * @param serviceDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceDTO save(ServiceDTO serviceDTO) {
        log.debug("Request to save Service : {}", serviceDTO);
        com.centreon.map.domain.Service service = serviceMapper.toEntity(serviceDTO);
        service = serviceRepository.save(service);
        return serviceMapper.toDto(service);
    }

    /**
     * Get all the services.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Services");
        return serviceRepository.findAll(pageable)
            .map(serviceMapper::toDto);
    }


    /**
     * Get one service by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceDTO> findOne(Long id) {
        log.debug("Request to get Service : {}", id);
        return serviceRepository.findById(id)
            .map(serviceMapper::toDto);
    }

    /**
     * Delete the service by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Service : {}", id);
        serviceRepository.deleteById(id);
    }
}
