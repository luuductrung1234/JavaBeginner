package com.learning;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        String[] inFiles = { "./assets/file1.txt", "./assets/file2.txt", "./assets/file3.txt", "./assets/file4.txt",
                "./assets/file5.txt", "./assets/file6.txt", };
        String[] outFiles = { "./assets/file1.out.txt", "./assets/file2.out.txt", "./assets/file3.out.txt",
                "./assets/file4.out.txt", "./assets/file5.out.txt", "./assets/file6.out.txt", };

        runAdderInSingleThread(inFiles, outFiles);
        runAdderInMultiThread(inFiles, outFiles);
    }

    private static void runAdderInSingleThread(String[] inFiles, String[] outFiles) {
        try {
            for (int i = 0; i < inFiles.length; i++) {
                Adder adder = new Adder(inFiles[i], outFiles[i]);
                adder.doAdd();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }

    private static void runAdderInMultiThread(String[] inFiles, String[] outFiles) {
        Thread[] threads = new Thread[inFiles.length];
        for (int i = 0; i < inFiles.length; i++) {
            Adder adder = new Adder(inFiles[i], outFiles[i]);
            threads[i] = new Thread(adder);
            threads[i].start();
        }
        try {
            for (Thread thread : threads) {
                thread.join(); // Blocks main thread, waiting for threads completion.
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }
}