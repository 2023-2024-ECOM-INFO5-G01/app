package fr.uga.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RepasTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Repas getRepasSample1() {
        return new Repas().id(1L);
    }

    public static Repas getRepasSample2() {
        return new Repas().id(2L);
    }

    public static Repas getRepasRandomSampleGenerator() {
        return new Repas().id(longCount.incrementAndGet());
    }
}
