package com.mapFilterReduce;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMapFilterReduce {
    private static Logger logger = Logger.getLogger(MainMapFilterReduce.class.getSimpleName());

    public static void main(String[] args) {
        List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> ints1 = Arrays.asList(0, 1, 2, 3, 4);
        List<Integer> ints2 = Arrays.asList(5, 6, 7, 8, 9);

        run("Sum - Demo", ints, ints1, ints2, (i1, i2) -> i1 + i2);
        run("Max - Demo", ints, ints1, ints2, Integer::max);
        /*
         * Reduction is one of three step Map / Filter / Reduce pattern. It is a tricky
         * one, when design a reduce, notice Associative and Identity Element
         * 
         * The 0 is not always an identity element for BinaryOperator. There are an
         * Optional class which is an better alternative for 0.
         */
        run("Not Associative - Demo", ints, ints1, ints2, (i1, i2) -> (i1 + i2) * (i1 + i2));

        ints = Arrays.asList(0, 1, 2, 3, 4, -1, -2, -3, -4);
        ints1 = Arrays.asList(0, 1, 2, 3, 4);
        ints2 = Arrays.asList(-1, -2, -3, -4);

        run("0 is not an identity element of the BinaryOperator - Demo", ints, ints1, ints2, Integer::max);
    }

    public static void run(String message, List<Integer> ints, List<Integer> ints1, List<Integer> ints2,
            BinaryOperator<Integer> op) {
        logger.log(Level.INFO, message);
        logger.log(Level.INFO, "--------------");
        int reduction1 = reduce(ints1, 0, op);
        logger.log(Level.INFO, "List Integer 1: {0}", ints1);
        logger.log(Level.INFO, "Reduction 1: {0}", reduction1);
        int reduction2 = reduce(ints2, 0, op);
        logger.log(Level.INFO, "List Integer 2: {0}", ints2);
        logger.log(Level.INFO, "Reduction 2: {0}", reduction2);
        int reduction = reduce(Arrays.asList(reduction1, reduction2), 0, op);
        logger.log(Level.INFO, "Reduce Reduction 1 & 2: {0}", reduction);
        int actualReduction = reduce(ints, 0, op);
        logger.log(Level.INFO, "List Integer: {0}", ints);
        logger.log(Level.INFO, "Actual Reduction: {0}", actualReduction);
    }

    /**
     * Sample implementation of Collection's reduce function
     * 
     * @param values
     * @param valueIfEmpty
     * @param reduction
     * @return
     */
    public static int reduce(List<Integer> values, int valueIfEmpty, BinaryOperator<Integer> reduction) {
        int result = valueIfEmpty;
        for (Integer value : values) {
            result = reduction.apply(result, value);
        }
        return result;
    }
}