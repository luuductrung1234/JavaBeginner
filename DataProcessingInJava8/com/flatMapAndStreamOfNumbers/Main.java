package com.flatMapAndStreamOfNumbers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        simpleDemo();
        flatMapDemo(stream -> stream);
        flatMapDemo(UnaryOperator.identity());
    }

    private static void simpleDemo() {
        try (Stream<String> stream1 = Files.lines(Paths.get("./assets/TomSawyer_01.txt"));
                Stream<String> stream2 = Files.lines(Paths.get("./assets/TomSawyer_02.txt"));
                Stream<String> stream3 = Files.lines(Paths.get("./assets/TomSawyer_03.txt"));
                Stream<String> stream4 = Files.lines(Paths.get("./assets/TomSawyer_04.txt"));) {
            logger.log(Level.INFO, "Stream 1: {0}", stream1.count());
            logger.log(Level.INFO, "Stream 2: {0}", stream2.count());
            logger.log(Level.INFO, "Stream 3: {0}", stream3.count());
            logger.log(Level.INFO, "Stream 4: {0}", stream4.count());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }

    private static void flatMapDemo(UnaryOperator<Stream<String>> mapper) {
        try (Stream<String> stream1 = Files.lines(Paths.get("./assets/TomSawyer_01.txt"));
                Stream<String> stream2 = Files.lines(Paths.get("./assets/TomSawyer_02.txt"));
                Stream<String> stream3 = Files.lines(Paths.get("./assets/TomSawyer_03.txt"));
                Stream<String> stream4 = Files.lines(Paths.get("./assets/TomSawyer_04.txt"));) {
            Stream<Stream<String>> streamOfStreams = Stream.of(stream1, stream2, stream3, stream4);
            Stream<String> streamOfLines = streamOfStreams.flatMap(mapper);
            logger.log(Level.INFO, "# Lines: {0}", streamOfLines.count());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }
}