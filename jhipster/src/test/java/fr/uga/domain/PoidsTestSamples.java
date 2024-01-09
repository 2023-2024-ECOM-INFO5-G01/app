package fr.uga.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PoidsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Poids getPoidsSample1() {
        return new Poids().id(1L);
    }

    public static Poids getPoidsSample2() {
        return new Poids().id(2L);
    }

    public static Poids getPoidsRandomSampleGenerator() {
        return new Poids().id(longCount.incrementAndGet());
    }
}
