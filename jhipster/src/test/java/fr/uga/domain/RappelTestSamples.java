package fr.uga.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RappelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Rappel getRappelSample1() {
        return new Rappel().id(1L).action("action1");
    }

    public static Rappel getRappelSample2() {
        return new Rappel().id(2L).action("action2");
    }

    public static Rappel getRappelRandomSampleGenerator() {
        return new Rappel().id(longCount.incrementAndGet()).action(UUID.randomUUID().toString());
    }
}
