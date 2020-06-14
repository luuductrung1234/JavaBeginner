package com.collectDataUsingCollectors;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collector is a reduction in a container.
 * 
 * A Collector is a special typo of reduction, which is precisely not an
 * aggregation. It is a terminal operation that triggers the computation of the
 * Stream.
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
                antiPatternToReduceDemo();
                break;
            case 2:
                rightPatternToReduceDemo();
                break;
            case 3:
                usingCollectorsDemo();
                break;
            case 4:
                summaryDemo();
                break;
            default:
                summaryDemo();
                break;
        }
    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    /**
     * Add element to external list.
     *
     * This approach is not thread-save when stream process in parallel (ArrayList
     * is not a concurrent-aware data structure).
     */
    private static void antiPatternToReduceDemo() {
        List<String> peopleNames = getListOfPeopleNames(1000);

        List<String> result = new ArrayList<>();
        peopleNames.stream().parallel().filter(s -> !s.isEmpty()).filter(s -> s.endsWith("2")).forEach(result::add);
        logger.log(Level.INFO, "Anti-Pattern to reduce: {0}", result);
    }

    /**
     * Collector Pattern will deal with parallel process. This step is called
     * mutable collection because it collects the data in a mutable container.
     * 
     * This approach is thread-save when stream process in parallel.
     */
    private static void rightPatternToReduceDemo() {
        List<String> peopleNames = getListOfPeopleNames(1000);

        List<String> result = peopleNames.stream().parallel().filter(s -> !s.isEmpty()).filter(s -> s.endsWith("2"))
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Right-Pattern to reduce: {0}", result);
    }

    /**
     * The JDK provides a factory class: Collectors
     * 
     * Collecting data is about gathering data in a mutable container:
     * 
     * - A String (concatenation)
     * 
     * - A Collection (adding)
     * 
     * - A HashMap (grouping by a criteria)
     * 
     * Collectors is the class factory to build collectors
     */
    private static void usingCollectorsDemo() {
        List<Person> people = getListOfPeople(100);

        Optional<Person> oldest = people.stream().collect(Collectors.maxBy(Comparator.comparing(p -> p.getAge())));
        logger.log(Level.INFO, "Oldest person: {0}", oldest);
        double average = people.stream().collect(Collectors.averagingDouble(p -> p.getAge()));
        logger.log(Level.INFO, "Average of people ages: {0}", average);

        String names = people.stream().map(Person::getName).collect(Collectors.joining(", "));
        logger.log(Level.INFO, "Joined names: {0}", names);

        Set<String> setOfNames = people.stream().map(Person::getName).collect(Collectors.toSet());
        logger.log(Level.INFO, "Set of names: {0}", setOfNames);

        TreeSet<String> treeSetOfNames = people.stream().map(Person::getName)
                .collect(Collectors.toCollection(TreeSet::new));
        logger.log(Level.INFO, "TreeSet of names: {0}", treeSetOfNames);

        Map<Boolean, List<Person>> peopleSeparatedByAge = people.stream()
                .collect(Collectors.partitioningBy(p -> p.getAge() > 21));
        logger.log(Level.INFO, "People separated by age: {0}", peopleSeparatedByAge);

        Map<Integer, List<Person>> peopleGroupByAge = people.stream().collect(Collectors.groupingBy(p -> p.getAge()));
        logger.log(Level.INFO, "People grouped by age: {0}", peopleGroupByAge);

        Map<Integer, Long> numberOfPeopleGroupedByAge = people.stream()
                .collect(Collectors.groupingBy(p -> p.getAge(), Collectors.counting()));
        logger.log(Level.INFO, "Number of people grouped by age: {0}", numberOfPeopleGroupedByAge);

        Map<Integer, List<String>> namesGroupedByAge = people.stream().collect(
                Collectors.groupingBy(p -> p.getAge(), Collectors.mapping(p -> p.getName(), Collectors.toList())));
        logger.log(Level.INFO, "People names grouped by age: {0}", namesGroupedByAge);

        Map<Integer, TreeSet<String>> treeSetOfNamesGroupedByAge = people.stream().collect(Collectors.groupingBy(
                p -> p.getAge(), Collectors.mapping(p -> p.getName(), Collectors.toCollection(TreeSet::new))));
        logger.log(Level.INFO, "TreeSet of people names grouped by age: {0}", treeSetOfNamesGroupedByAge);

        Map<Integer, List<Person>> immutableMapOfPeopleGroupedByAge = (Map<Integer, List<Person>>) people.stream()
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(p -> p.getAge()),
                        Collections::unmodifiableMap));
        logger.log(Level.INFO, "Immutable-Map of people grouped by age: {0}", immutableMapOfPeopleGroupedByAge);
    }

    /**
     * Summary Demo (contains all concepts above)
     */
    private static void summaryDemo() {
        try (Stream<String> ospd = Files.lines(Paths.get("./assets/ospd.txt"));
                Stream<String> shakespeare = Files.lines(Paths.get("./assets/words.shakespeare.txt"));) {
            Set<String> scrabbleWords = ospd.collect(Collectors.toSet());
            Set<String> shakespeareWords = shakespeare.collect(Collectors.toSet());

            logger.log(Level.INFO, "Scrabble : {0}", scrabbleWords.size());
            logger.log(Level.INFO, "Shakespeare : {0}", shakespeareWords.size());

            /**
             * Calculate a score of individual word in Shakespeare paragraph
             */

            final int[] letterScore = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4,
                    10 };

            ToIntFunction<String> evaluateWordScore = word -> word.toLowerCase().chars()
                    .map(letter -> letterScore[letter - 'a']).sum();

            Map<Integer, List<String>> histogramWordsByScore = shakespeareWords.stream().filter(scrabbleWords::contains)
                    .collect(Collectors.groupingBy(evaluateWordScore::applyAsInt));

            logger.log(Level.INFO, "# Histogram of Shakespeare words by score : {0}", histogramWordsByScore.size());

            histogramWordsByScore.entrySet().stream().sorted(Comparator.comparing(entry -> -entry.getKey())).limit(3)
                    .forEach(entry -> logger.log(Level.INFO, "POINT={0} - WORD={1}",
                            new Object[] { entry.getKey(), entry.getValue() }));

            /**
             * Calculate a score of individual word in Shakespeare paragraph. Each letter
             * has a limited frequency in single word
             */

            int[] numberOfAllowedLetters = { 9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2,
                    1 };

            Function<String, Map<Integer, Long>> histogramWord = word -> word.chars().boxed()
                    .collect(Collectors.groupingBy(letter -> letter, Collectors.counting()));

            ToLongFunction<String> numberOfBlanks = word -> histogramWord.apply(word).entrySet().stream().mapToLong(
                    entry -> Long.max(entry.getValue() - (long) numberOfAllowedLetters[entry.getKey() - 'a'], 0L))
                    .sum();

            ToIntFunction<String> evaluateWordScore2 = word -> histogramWord.apply(word).entrySet().stream()
                    .mapToInt(entry -> letterScore[entry.getKey() - 'a']
                            * Integer.min(entry.getValue().intValue(), numberOfAllowedLetters[entry.getKey() - 'a']))
                    .sum();

            Map<Integer, List<String>> histogramWordsByScore2 = shakespeareWords.stream()
                    .filter(scrabbleWords::contains).filter(word -> numberOfBlanks.applyAsLong(word) <= 2L)
                    .collect(Collectors.groupingBy(evaluateWordScore2::applyAsInt));

            logger.log(Level.INFO, "# Histogram of Shakespeare words by score : {0}", histogramWordsByScore2.size());

            histogramWordsByScore2.entrySet().stream().sorted(Comparator.comparing(entry -> -entry.getKey())).limit(3)
                    .forEach(entry -> logger.log(Level.INFO, "POINT={0} - WORD={1}",
                            new Object[] { entry.getKey(), entry.getValue() }));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    /**
     * Helper method to get a sample list of people
     * 
     * @return
     */
    private static List<Person> getListOfPeople(int count) {
        int MAX_AGE = 100;
        int MIN_AGE = 10;
        List<String> cities = Arrays.asList("Ho Chi Minh", "Vinh", "Ben Tre", "Quang Binh", "Nghe An", "Da Nang");
        List<Person> people = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append("User ").append(i);
            int randomAge = ThreadLocalRandom.current().nextInt(MIN_AGE, MAX_AGE + 1);
            int randomCity = ThreadLocalRandom.current().nextInt(0, cities.size());
            people.add(new Person(nameBuilder.toString(), randomAge, cities.get(randomCity)));
        }
        logger.log(Level.INFO, "Generated sample people: {0}", people);
        return people;
    }

    private static List<String> getListOfPeopleNames(int count) {
        List<Person> people = getListOfPeople(count);
        List<String> peopleNames = people.stream().map(Person::getName).collect(Collectors.toList());
        logger.log(Level.INFO, "Generated sample people names: {0}", peopleNames);
        return peopleNames;
    }
}