package com.teaching;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLoggerNaming {
    private static Logger pkgLogger = Logger.getLogger("com.teaching");
    private static Logger logger = Logger.getLogger("com.teaching.Main");

    public static void main(String[] args) {
        logger.entering("com.teaching", "Main");
        logger.log(Level.INFO, "We're logging!");
        logger.exiting("com.teaching", "Main");
    }
}