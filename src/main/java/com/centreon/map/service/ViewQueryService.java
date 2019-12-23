package com.centreon.map.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.centreon.map.domain.View;
import com.centreon.map.domain.*; // for static metamodels
import com.centreon.map.repository.ViewRepository;
import com.centreon.map.service.dto.ViewCriteria;
import com.centreon.map.service.dto.ViewDTO;
import com.centreon.map.service.mapper.ViewMapper;

/**
 * Service for executing complex queries for {@link View} entities in the database.
 * The main input is a {@link ViewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ViewDTO} or a {@link Page} of {@link ViewDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ViewQueryService extends QueryService<View> {

    private final Logger log = LoggerFactory.getLogger(ViewQueryService.class);

    private final ViewRepository viewRepository;

    private final ViewMapper viewMapper;

    public ViewQueryService(ViewRepository viewRepository, ViewMapper viewMapper) {
        this.viewRepository = viewRepository;
        this.viewMapper = viewMapper;
    }

    /**
     * Return a {@link List} of {@link ViewDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ViewDTO> findByCriteria(ViewCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<View> specification = createSpecification(criteria);
        return viewMapper.toDto(viewRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ViewDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ViewDTO> findByCriteria(ViewCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<View> specification = createSpecification(criteria);
        return viewRepository.findAll(specification, page)
            .map(viewMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ViewCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<View> specification = createSpecification(criteria);
        return viewRepository.count(specification);
    }

    /**
     * Function to convert {@link ViewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<View> createSpecification(ViewCriteria criteria) {
        Specification<View> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), View_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), View_.title));
            }
            if (criteria.getIsLocked() != null) {
                specification = specification.and(buildSpecification(criteria.getIsLocked(), View_.isLocked));
            }
        }
        return specification;
    }
}
