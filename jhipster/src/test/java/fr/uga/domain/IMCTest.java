package fr.uga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IMCTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IMC.class);
        IMC iMC1 = new IMC();
        iMC1.setId(1L);
        IMC iMC2 = new IMC();
        iMC2.setId(iMC1.getId());
        assertThat(iMC1).isEqualTo(iMC2);
        iMC2.setId(2L);
        assertThat(iMC1).isNotEqualTo(iMC2);
        iMC1.setId(null);
        assertThat(iMC1).isNotEqualTo(iMC2);
    }
}
