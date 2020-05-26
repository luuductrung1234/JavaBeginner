package com.streamOfNumbers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        try {
            Set<String> shakespeareWords = Files.lines(Paths.get("./assets/words.shakespeare.txt"))
                    .map(word -> word.toLowerCase()).collect(Collectors.toSet());
            Set<String> scrabbleWords = Files.lines(Paths.get("./assets/ospd.txt")).map(word -> word.toLowerCase())
                    .collect(Collectors.toSet());
            logger.log(Level.INFO, "# Words of Shakespeare: {0}", shakespeareWords.size());
            logger.log(Level.INFO, "# Words of Scrabble: {0}", scrabbleWords.size());

            final int[] scrabbleENScore = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4,
                    10 };
            String text = "hello";
            // boxed number value
            Function<String, Integer> score = word -> word.chars().map(letter -> scrabbleENScore[letter - 'a']).sum();
            logger.log(Level.INFO, "Score of {0}: {1}", new Object[] { text, score.apply(text) });
            // unboxed number value
            ToIntFunction<String> intScore = word -> word.chars().map(letter -> scrabbleENScore[letter - 'a']).sum();
            logger.log(Level.INFO, "Score of {0}: {1}", new Object[] { text, intScore.applyAsInt(text) });

            Optional<String> bestWord = shakespeareWords.stream().filter(word -> scrabbleWords.contains(word))
                    .max(Comparator.comparing(score));
            if (bestWord.isPresent())
                logger.log(Level.INFO, "Best word: {0}", bestWord);

            IntSummaryStatistics summaryStatistics = shakespeareWords.stream().filter(scrabbleWords::contains)
                    .mapToInt(intScore).summaryStatistics();
            logger.log(Level.INFO, "Summary stats: {0}", summaryStatistics);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }
}