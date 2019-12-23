package com.centreon.map.service;

import com.centreon.map.domain.Host;
import com.centreon.map.repository.HostRepository;
import com.centreon.map.service.dto.HostDTO;
import com.centreon.map.service.mapper.HostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Host}.
 */
@Service
@Transactional
public class HostService {

    private final Logger log = LoggerFactory.getLogger(HostService.class);

    private final HostRepository hostRepository;

    private final HostMapper hostMapper;

    public HostService(HostRepository hostRepository, HostMapper hostMapper) {
        this.hostRepository = hostRepository;
        this.hostMapper = hostMapper;
    }

    /**
     * Save a host.
     *
     * @param hostDTO the entity to save.
     * @return the persisted entity.
     */
    public HostDTO save(HostDTO hostDTO) {
        log.debug("Request to save Host : {}", hostDTO);
        Host host = hostMapper.toEntity(hostDTO);
        host = hostRepository.save(host);
        return hostMapper.toDto(host);
    }

    /**
     * Get all the hosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Hosts");
        return hostRepository.findAll(pageable)
            .map(hostMapper::toDto);
    }


    /**
     * Get one host by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HostDTO> findOne(Long id) {
        log.debug("Request to get Host : {}", id);
        return hostRepository.findById(id)
            .map(hostMapper::toDto);
    }

    /**
     * Delete the host by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Host : {}", id);
        hostRepository.deleteById(id);
    }
}
