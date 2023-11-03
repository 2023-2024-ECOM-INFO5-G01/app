package fr.uga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlbumineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Albumine.class);
        Albumine albumine1 = new Albumine();
        albumine1.setId(1L);
        Albumine albumine2 = new Albumine();
        albumine2.setId(albumine1.getId());
        assertThat(albumine1).isEqualTo(albumine2);
        albumine2.setId(2L);
        assertThat(albumine1).isNotEqualTo(albumine2);
        albumine1.setId(null);
        assertThat(albumine1).isNotEqualTo(albumine2);
    }
}
