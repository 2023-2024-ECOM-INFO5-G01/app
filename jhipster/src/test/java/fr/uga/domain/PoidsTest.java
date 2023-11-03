package fr.uga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoidsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poids.class);
        Poids poids1 = new Poids();
        poids1.setId(1L);
        Poids poids2 = new Poids();
        poids2.setId(poids1.getId());
        assertThat(poids1).isEqualTo(poids2);
        poids2.setId(2L);
        assertThat(poids1).isNotEqualTo(poids2);
        poids1.setId(null);
        assertThat(poids1).isNotEqualTo(poids2);
    }
}
