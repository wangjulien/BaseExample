package com.centreon.map.service.mapper;

import com.centreon.map.domain.*;
import com.centreon.map.service.dto.ViewDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link View} and its DTO {@link ViewDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ViewMapper extends EntityMapper<ViewDTO, View> {



    default View fromId(Long id) {
        if (id == null) {
            return null;
        }
        View view = new View();
        view.setId(id);
        return view;
    }
}
