package com.learning;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    static Logger logger = Logger.getLogger("com.learning");

    public static void main(String[] args) {
        try {
            addConsoleHandler(logger);
            addFileHandler(logger);
            logger.setLevel(Level.INFO);
            logger.log(Level.INFO, "we're logging!");
            logger.log(Level.INFO, "there are {0} loggers", logger.getHandlers().length);
            logger.log(Level.INFO, "user home: {0}", System.getProperty("user.home"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    private static void addFileHandler(Logger logger) throws IOException {
        FileHandler fileHandler = new FileHandler("%h/learnLogging_%g.log", 1000, 4);
        Formatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
    }

    private static void addConsoleHandler(Logger logger) {
        Handler handler = new ConsoleHandler();
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.addHandler(handler);
    }
}
