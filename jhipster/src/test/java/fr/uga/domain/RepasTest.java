package fr.uga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repas.class);
        Repas repas1 = new Repas();
        repas1.setId(1L);
        Repas repas2 = new Repas();
        repas2.setId(repas1.getId());
        assertThat(repas1).isEqualTo(repas2);
        repas2.setId(2L);
        assertThat(repas1).isNotEqualTo(repas2);
        repas1.setId(null);
        assertThat(repas1).isNotEqualTo(repas2);
    }
}
