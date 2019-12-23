package com.centreon.map.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.centreon.map.web.rest.TestUtil;

public class HostDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HostDTO.class);
        HostDTO hostDTO1 = new HostDTO();
        hostDTO1.setId(1L);
        HostDTO hostDTO2 = new HostDTO();
        assertThat(hostDTO1).isNotEqualTo(hostDTO2);
        hostDTO2.setId(hostDTO1.getId());
        assertThat(hostDTO1).isEqualTo(hostDTO2);
        hostDTO2.setId(2L);
        assertThat(hostDTO1).isNotEqualTo(hostDTO2);
        hostDTO1.setId(null);
        assertThat(hostDTO1).isNotEqualTo(hostDTO2);
    }
}
