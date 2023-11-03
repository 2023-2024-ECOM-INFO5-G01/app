package fr.uga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlimentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aliment.class);
        Aliment aliment1 = new Aliment();
        aliment1.setId(1L);
        Aliment aliment2 = new Aliment();
        aliment2.setId(aliment1.getId());
        assertThat(aliment1).isEqualTo(aliment2);
        aliment2.setId(2L);
        assertThat(aliment1).isNotEqualTo(aliment2);
        aliment1.setId(null);
        assertThat(aliment1).isNotEqualTo(aliment2);
    }
}
