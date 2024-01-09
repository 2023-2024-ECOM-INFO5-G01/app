package fr.uga.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class IMCTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IMC getIMCSample1() {
        return new IMC().id(1L);
    }

    public static IMC getIMCSample2() {
        return new IMC().id(2L);
    }

    public static IMC getIMCRandomSampleGenerator() {
        return new IMC().id(longCount.incrementAndGet());
    }
}
