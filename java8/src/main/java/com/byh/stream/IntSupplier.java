package com.byh.stream;

import java.util.Random;
import java.util.function.Supplier;

public class IntSupplier implements Supplier<Integer> {

    private int index = 0;
    private Random random = new Random();

    @Override
    public Integer get() {
        return random.nextInt(100);
    }
}
