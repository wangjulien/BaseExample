package com.centreon.map.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.centreon.map.web.rest.TestUtil;

public class ViewDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewDTO.class);
        ViewDTO viewDTO1 = new ViewDTO();
        viewDTO1.setId(1L);
        ViewDTO viewDTO2 = new ViewDTO();
        assertThat(viewDTO1).isNotEqualTo(viewDTO2);
        viewDTO2.setId(viewDTO1.getId());
        assertThat(viewDTO1).isEqualTo(viewDTO2);
        viewDTO2.setId(2L);
        assertThat(viewDTO1).isNotEqualTo(viewDTO2);
        viewDTO1.setId(null);
        assertThat(viewDTO1).isNotEqualTo(viewDTO2);
    }
}
