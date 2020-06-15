package com.customCollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Collector.Characteristics;

/**
 * A Collector made of three elements
 * 
 * - The first element is used to build the resulting container (e.g. ArrayList
 * or HashMap)
 * 
 * - The second element adds an object from the stream to the container (e.g. it
 * adds an object to an ArrayList)
 * 
 * - The third element is only used for parallelism. It is used to merge
 * together two partially filled containers.
 * 
 * (*) Parallelism is about distributing my computation among more than one
 * processor, of my CPU, so each of those cores will have to handle a certain
 * part of my computation and when each of these core is done with this partial
 * computation, I have to merge the results together. This is what the third
 * element is for.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        int choice = 0;
        try {
            choice = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Please choose a specific demo to run!");
        }

        switch (choice) {
            case 1:
                simpleCustomCollector();
                break;
            default:
                break;
        }
    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    private static void simpleCustomCollector() {
        Supplier<List<Person>> suppiler = () -> new ArrayList<>();

        BiConsumer<List<Person>, Person> accumulator = (list, p) -> list.add(p);

        BinaryOperator<List<Person>> combiner = (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };

        Collector collector = Collector.of(suppiler, accumulator, combiner, Characteristics.IDENTITY_FINISH);
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

}