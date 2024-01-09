package fr.uga.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlimentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Aliment getAlimentSample1() {
        return new Aliment().id(1L).nom("nom1");
    }

    public static Aliment getAlimentSample2() {
        return new Aliment().id(2L).nom("nom2");
    }

    public static Aliment getAlimentRandomSampleGenerator() {
        return new Aliment().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
