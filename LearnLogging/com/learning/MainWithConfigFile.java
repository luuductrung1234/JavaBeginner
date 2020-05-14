package com.learning;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWithConfigFile {
    static Logger logger = Logger.getLogger("com.learning");

    public static void main(String[] args) {
        logger.log(Level.INFO, "we're logging!");
        logger.log(Level.INFO, "there are {0} loggers", logger.getHandlers().length);
        logger.log(Level.INFO, "user home: {0}", System.getProperty("user.home"));
    }
}