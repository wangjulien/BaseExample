package com.centreon.map.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ViewMapperTest {

    private ViewMapper viewMapper;

    @BeforeEach
    public void setUp() {
        viewMapper = new ViewMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(viewMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(viewMapper.fromId(null)).isNull();
    }
}
