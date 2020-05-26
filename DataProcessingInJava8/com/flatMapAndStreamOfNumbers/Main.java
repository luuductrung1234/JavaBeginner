package com.flatMapAndStreamOfNumbers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        // simple demo
        streamOutput();

        // FlatMap demo
        try (Stream<String> streamOfLines = readToStreamOfLines(stream -> stream)) {
            logger.log(Level.INFO, "# Lines: {0}", streamOfLines.count());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
        try (Stream<String> streamOfLines = readToStreamOfLines(UnaryOperator.identity())) {
            logger.log(Level.INFO, "# Lines: {0}", streamOfLines.count());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
        try (Stream<String> streamOfWords = readToStreamOfWords()) {
            logger.log(Level.INFO, "# Distinct Words (4 characters length): {0}",
                    streamOfWords.map(String::toLowerCase).filter(word -> word.length() == 4).distinct().count());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }

    private static void streamOutput() {
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

    private static Stream<String> readToStreamOfLines(UnaryOperator<Stream<String>> mapper) throws IOException {
        Stream<String> stream1 = Files.lines(Paths.get("./assets/TomSawyer_01.txt"));
        Stream<String> stream2 = Files.lines(Paths.get("./assets/TomSawyer_02.txt"));
        Stream<String> stream3 = Files.lines(Paths.get("./assets/TomSawyer_03.txt"));
        Stream<String> stream4 = Files.lines(Paths.get("./assets/TomSawyer_04.txt"));
        Stream<Stream<String>> streamOfStreams = Stream.of(stream1, stream2, stream3, stream4);
        return streamOfStreams.flatMap(mapper);
    }

    private static Stream<String> readToStreamOfWords() throws IOException {
        Stream<String> streamOfLines = readToStreamOfLines(UnaryOperator.identity());
        Function<String, Stream<String>> lineSplitter = line -> Pattern.compile(" ").splitAsStream(line);
        return streamOfLines.flatMap(lineSplitter);
    }
}