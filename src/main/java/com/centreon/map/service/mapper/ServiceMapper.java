package com.centreon.map.service.mapper;

import com.centreon.map.domain.*;
import com.centreon.map.service.dto.ServiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Service} and its DTO {@link ServiceDTO}.
 */
@Mapper(componentModel = "spring", uses = {HostMapper.class})
public interface ServiceMapper extends EntityMapper<ServiceDTO, Service> {

    @Mapping(source = "host.id", target = "hostId")
    ServiceDTO toDto(Service service);

    @Mapping(source = "hostId", target = "host")
    Service toEntity(ServiceDTO serviceDTO);

    default Service fromId(Long id) {
        if (id == null) {
            return null;
        }
        Service service = new Service();
        service.setId(id);
        return service;
    }
}
