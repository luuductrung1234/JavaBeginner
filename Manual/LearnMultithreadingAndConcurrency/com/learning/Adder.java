package com.learning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Adder implements Runnable {
    private String inFile;
    private String outFile;
    private static Logger logger = Logger.getLogger(Adder.class.getName());

    public Adder(String inFile, String outFile) {
        super();
        this.inFile = inFile;
        this.outFile = outFile;
    }

    /*
     * Old code
     */
    public void doAdd() throws IOException {
        int total = 0;
        String line = null;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inFile))) {
            while ((line = reader.readLine()) != null) {
                total += Integer.parseInt(line);
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outFile), StandardOpenOption.CREATE)) {
            writer.write("Total: " + total);
        }
    }

    /*
     * Multi-threading code
     */
    @Override
    public void run() {
        try {
            doAdd();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }
}