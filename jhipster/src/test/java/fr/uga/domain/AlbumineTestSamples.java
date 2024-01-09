package fr.uga.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AlbumineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Albumine getAlbumineSample1() {
        return new Albumine().id(1L);
    }

    public static Albumine getAlbumineSample2() {
        return new Albumine().id(2L);
    }

    public static Albumine getAlbumineRandomSampleGenerator() {
        return new Albumine().id(longCount.incrementAndGet());
    }
}
