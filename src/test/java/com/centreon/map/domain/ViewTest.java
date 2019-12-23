package com.centreon.map.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.centreon.map.web.rest.TestUtil;

public class ViewTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(View.class);
        View view1 = new View();
        view1.setId(1L);
        View view2 = new View();
        view2.setId(view1.getId());
        assertThat(view1).isEqualTo(view2);
        view2.setId(2L);
        assertThat(view1).isNotEqualTo(view2);
        view1.setId(null);
        assertThat(view1).isNotEqualTo(view2);
    }
}
