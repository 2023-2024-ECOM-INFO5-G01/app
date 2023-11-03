package fr.uga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EPATest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EPA.class);
        EPA ePA1 = new EPA();
        ePA1.setId(1L);
        EPA ePA2 = new EPA();
        ePA2.setId(ePA1.getId());
        assertThat(ePA1).isEqualTo(ePA2);
        ePA2.setId(2L);
        assertThat(ePA1).isNotEqualTo(ePA2);
        ePA1.setId(null);
        assertThat(ePA1).isNotEqualTo(ePA2);
    }
}
