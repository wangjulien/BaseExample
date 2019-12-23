package com.centreon.map.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class HostMapperTest {

    private HostMapper hostMapper;

    @BeforeEach
    public void setUp() {
        hostMapper = new HostMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(hostMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(hostMapper.fromId(null)).isNull();
    }
}
