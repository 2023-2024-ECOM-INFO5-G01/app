package fr.uga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EhpadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ehpad.class);
        Ehpad ehpad1 = new Ehpad();
        ehpad1.setId(1L);
        Ehpad ehpad2 = new Ehpad();
        ehpad2.setId(ehpad1.getId());
        assertThat(ehpad1).isEqualTo(ehpad2);
        ehpad2.setId(2L);
        assertThat(ehpad1).isNotEqualTo(ehpad2);
        ehpad1.setId(null);
        assertThat(ehpad1).isNotEqualTo(ehpad2);
    }
}
