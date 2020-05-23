package com.predicate;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainPredicate {
    private static Logger logger = Logger.getLogger(MainPredicate.class.getSimpleName());

    public static void main(String[] args) {
        Predicate<String> notLargeTextPredicate = s -> {
            logger.log(Level.INFO, "running notLargeTextPredicate");
            return s.length() < 20;
        };
        Predicate<String> notShortTextPredicate = s -> {
            logger.log(Level.INFO, "running notShortTextPredicate");
            return s.length() > 5;
        };

        var text = "Hello";
        if (notLargeTextPredicate.test(text))
            logger.log(Level.INFO, "{0} is shorter than 20 chars", text);

        Predicate<String> andPredicate = notLargeTextPredicate.and(notShortTextPredicate);
        if (andPredicate.test(text))
            logger.log(Level.INFO, "{0} is larger than 5 chars and shorter than 20 chars", text);

        Predicate<String> orPredicate = notLargeTextPredicate.or(notShortTextPredicate);
        if (orPredicate.test(text))
            logger.log(Level.INFO, "{0} is larger than 5 chars or shorter than 20 chars", text);

        var checkText = "Hi";
        Predicate<String> textEqualPredicate = Predicate.isEqualsTo(checkText);
        if (textEqualPredicate.test(text))
            logger.log(Level.INFO, "{0} is equal to {1}", new Object[] { text, checkText });
    }
}
