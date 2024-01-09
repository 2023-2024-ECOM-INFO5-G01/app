package fr.uga.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Note getNoteSample1() {
        return new Note().id(1L).note("note1");
    }

    public static Note getNoteSample2() {
        return new Note().id(2L).note("note2");
    }

    public static Note getNoteRandomSampleGenerator() {
        return new Note().id(longCount.incrementAndGet()).note(UUID.randomUUID().toString());
    }
}
