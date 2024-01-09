package fr.uga.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EhpadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ehpad getEhpadSample1() {
        return new Ehpad().id(1L).nom("nom1");
    }

    public static Ehpad getEhpadSample2() {
        return new Ehpad().id(2L).nom("nom2");
    }

    public static Ehpad getEhpadRandomSampleGenerator() {
        return new Ehpad().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
