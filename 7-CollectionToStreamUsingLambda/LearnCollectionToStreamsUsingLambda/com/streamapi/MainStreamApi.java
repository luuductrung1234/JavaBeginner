package com.streamapi;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainStreamApi {
    private static Logger logger = Logger.getLogger(MainStreamApi.class.getSimpleName());

    public static void main(String[] args) {
        List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4);
        Stream<Integer> stream1 = ints.stream();
        Stream<Integer> stream2 = Stream.of(0, 1, 2, 3, 4);

        stream1.forEach(integer -> logger.log(Level.INFO, integer.toString()));
        stream2.forEach(integer -> logger.log(Level.INFO, integer.toString()));

        Stream<String> streamOfStrings = Stream.generate(() -> "One");
        streamOfStrings.limit(5).forEach(value -> logger.log(Level.INFO, value));

        Stream<String> streamOfStrings2 = Stream.iterate("+", s -> s + "+");
        streamOfStrings2.limit(5).forEach(value -> logger.log(Level.INFO, value));

        IntStream streamOfInts = ThreadLocalRandom.current().ints();
        streamOfInts.limit(5).forEach(value -> logger.log(Level.INFO, String.valueOf(value)));
    }
}