package fr.uga.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EPATestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EPA getEPASample1() {
        return new EPA().id(1L);
    }

    public static EPA getEPASample2() {
        return new EPA().id(2L);
    }

    public static EPA getEPARandomSampleGenerator() {
        return new EPA().id(longCount.incrementAndGet());
    }
}
