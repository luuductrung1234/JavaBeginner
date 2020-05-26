package com.spliteratorPattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        Path path = Paths.get("./assets/person.txt");
        try (Stream<String> lines = Files.lines(path)) { // read all lines in file as a Stream
            Spliterator<String> lineSpliterator = lines.spliterator();
            Spliterator<Person> peopleSpliterator = new PersonSpliterator(lineSpliterator);
            Stream<Person> people = StreamSupport.stream(peopleSpliterator, false);
            people.forEach(person -> logger.log(Level.INFO, person.toString()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }
}
