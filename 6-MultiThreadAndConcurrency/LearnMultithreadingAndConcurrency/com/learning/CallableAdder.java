package com.learning;

import java.util.concurrent.Callable;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CallableAdder implements Callable<Integer> {
    private String inFile;

    public CallableAdder(String inFile) {
        super();
        this.inFile = inFile;
    }

    /*
     * Old code
     */
    public int doAdd() throws IOException {
        int total = 0;
        String line = null;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inFile))) {
            while ((line = reader.readLine()) != null) {
                total += Integer.parseInt(line);
            }
        }
        return total;
    }

    /*
     * Multi-threading code
     */
    @Override
    public Integer call() throws IOException {
        return doAdd();
    }
}