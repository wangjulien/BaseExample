package com.centreon.map.service.mapper;

import com.centreon.map.domain.*;
import com.centreon.map.service.dto.HostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Host} and its DTO {@link HostDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HostMapper extends EntityMapper<HostDTO, Host> {


    @Mapping(target = "services", ignore = true)
    @Mapping(target = "removeService", ignore = true)
    Host toEntity(HostDTO hostDTO);

    default Host fromId(Long id) {
        if (id == null) {
            return null;
        }
        Host host = new Host();
        host.setId(id);
        return host;
    }
}
