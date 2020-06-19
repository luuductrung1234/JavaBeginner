package com.customCollectors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
            case 2:
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

    private static void simpleCustomCollector() {
        Supplier<List<Person>> supplier = ArrayList::new;

        BiConsumer<List<Person>, Person> accumulator = (list, p) -> {
            if (p.getAge() > 50)
                list.add(p);
        };

        BinaryOperator<List<Person>> combiner = (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };

        Collector<Person, ?, List<Person>> collector = Collector.of(supplier, accumulator, combiner,
                Characteristics.IDENTITY_FINISH);

        List<Person> people = getListOfPeople(100);
        List<Person> peopleOlderThan50 = (List<Person>) people.stream().collect(collector);
        logger.log(Level.INFO, "People that older than 50: {0}", peopleOlderThan50);
    }

    private static void summaryDemo() {
        Set<Movie> movies = getListOfMovies(Paths.get("./assets/movies-mpaa.txt"));

        // counting # of actors in list of Movies
        long numberOfActors = movies.stream().flatMap(movie -> movie.getActors().stream()).distinct().count();
        logger.log(Level.INFO, "Number of Actors : {0}", numberOfActors);

        /**
         * 
         * actor that played in the greatest # of movies
         * 
         */
        Map.Entry<Actor, Long> actorInMostMovies = movies.stream().flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                .max(Map.Entry.comparingByValue()).get();
        logger.log(Level.INFO, "Actor {0} play in {1} movies",
                new Object[] { actorInMostMovies.getKey(), actorInMostMovies.getValue() });

        /**
         * 
         * actor that played in the greatest # of movies during a year
         * 
         */
        Supplier<HashMap<Actor, AtomicLong>> supplier = HashMap::new;

        BiConsumer<HashMap<Actor, AtomicLong>, Movie> accumulator = (map, movie) -> {
            movie.getActors().forEach(actor -> map.computeIfAbsent(actor, a -> new AtomicLong()).incrementAndGet());
        };

        BinaryOperator<HashMap<Actor, AtomicLong>> combiner = (dest, src) -> {
            src.entrySet().forEach(entry -> dest.merge(entry.getKey(), entry.getValue(), (al1, al2) -> {
                al1.addAndGet(al2.get());
                return al1;
            }));
            return dest;
        };

        Map.Entry<Integer, Map.Entry<Actor, AtomicLong>> actorWithGreatestMoviesInYear = movies.stream()
                .collect(Collectors.groupingBy(movie -> movie.getReleaseYear(),
                        Collector.of(supplier, accumulator, combiner, Characteristics.IDENTITY_FINISH)))
                // Map<Integer, HashMap<Actor, AtomicLong>>
                .entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(),
                        entry -> entry.getValue().entrySet().stream()
                                .max(Map.Entry.comparingByValue(Comparator.comparing(l -> l.get()))).get()))
                // Map<Integer, Map.Entry<Actor, AtomicLong>>
                .entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.comparing(entry -> entry.getValue().get()))).get();

        logger.log(Level.INFO, "{0} is a greatest actor, who plays in {1} movies in year {2}",
                new Object[] { actorWithGreatestMoviesInYear.getValue().getKey(),
                        actorWithGreatestMoviesInYear.getValue().getValue(), actorWithGreatestMoviesInYear.getKey() });
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    /**
     * Helper method to read and analyze data in text file to produce a list of
     * movies
     * 
     * @param path
     * @return
     */
    private static Set<Movie> getListOfMovies(Path path) {
        Set<Movie> movies = new HashSet<>();

        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach((String line) -> {
                String[] elements = line.split("/");
                String title = elements[0].substring(0, elements[0].lastIndexOf("(")).trim();
                String releaseYear = elements[0].substring(elements[0].lastIndexOf("(") + 1,
                        elements[0].lastIndexOf(")"));

                if (releaseYear.contains(",")) {
                    // skip movies with a comma in their title
                    return;
                }

                Movie movie = new Movie(title, Integer.valueOf(releaseYear));

                for (int i = 1; i < elements.length; i++) {
                    String[] name = elements[i].split(", ");
                    String lastName = name[0].trim();
                    String firstName = "";
                    if (name.length > 1)
                        firstName = name[1].trim();

                    Actor actor = new Actor(lastName, firstName);
                    movie.addActor(actor);
                }

                movies.add(movie);
            });
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
        return movies;
    }

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
}