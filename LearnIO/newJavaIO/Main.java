package newJavaIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.logging.CustomLogger;
import util.exception.ExceptionExtensions;

public class Main {
    private static Logger logger = CustomLogger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        int choice = 0;
        try {
            choice = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Please choose a specific demo to run!");
        }

        switch (choice) {
            case 1:
                usingPath();
                break;
            case 2:
                convertingPath();
                break;
            case 3:
                nIOPathWithExistedFileClass();
                break;
            case 4:
                findFilesInDirectory();
                break;
            case 5:
                zipFileSystem();
                break;
            case 6:
                walkingThroughDirectoryTree();
                break;
            default:
                zipFileSystem();
                break;
        }
    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    public static void usingPath() {
        var listing = Paths.get("/usr/bin/zip");
        logger.info("File Name [" + listing.getFileName() + "]");
        logger.info("Number of Name Elements in the Path [" + listing.getNameCount() + "]");
        logger.info("Parent Path [" + listing.getParent() + "]");
        logger.info("Root of Path [" + listing.getRoot() + "]");
        logger.info("Subpath from Root, 1 elements deep [" + listing.subpath(0, 1) + "]");
        logger.info("Subpath from Root, 2 elements deep [" + listing.subpath(0, 2) + "]");
        logger.info("Subpath from Root, 3 elements deep [" + listing.subpath(0, 3) + "]");
    }

    public static void convertingPath() {
        Path prefix = Paths.get("/Users");
        Path completePath = prefix.resolve("LDTrung/learnLogging_0.log");
        logger.log(Level.INFO, "Join to path: [{0}]", completePath);

        Path log0 = Paths.get("/Users/LDTrung/learnLogging_0.log");
        Path log1 = Paths.get("/Users/LDTrung/learnLogging_1.log");
        Path pathToLog = log0.relativize(log1);
        logger.log(Level.INFO, "Retrieve path between two path: [{0}]", pathToLog);

        logger.log(Level.INFO, "Is path start with: [{0}]", completePath.startsWith(prefix));
        logger.log(Level.INFO, "Is path end with: [{0}]", completePath.endsWith(".log"));
        logger.log(Level.INFO, "Is two path equal: [{0}]", completePath.equals(pathToLog));
    }

    public static void nIOPathWithExistedFileClass() {
        File file = new File("./ZipHelper.java");

        Path path = file.toPath();

        logger.log(Level.INFO, "Absolute path to code file: [{0}]", path.toAbsolutePath());

        file = path.toFile();
    }

    public static void findFilesInDirectory() {
        Path homeDir = Paths.get("/Users/LDTrung");

        // using "glob pattern match" to filter files end with ".log"
        // (Perl-like regular expression)
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(homeDir, "*.log")) {
            for (Path entry : stream) {
                Objects.requireNonNull(entry);
                Objects.requireNonNull(entry.getFileName());
                logger.info(entry.getFileName().toString());
            }
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void walkingThroughDirectoryTree() {
        try {
            // walk through directory-tree

            // display file's name, which is java code.
            logger.info("find java source code:");
            Path sourceCodeDir = Paths.get("/Users/LDTrung/SourceCode/Research/Backend/Java/JavaBeginner/LearnIO");
            Files.walkFileTree(sourceCodeDir, new FindJavaVisitor());

            // display file's name, which is text and skip symbolic link.
            logger.info("find text and skip symbolic link:");
            Path tempDir = Paths.get("/Users/LDTrung/Temp");
            Files.walkFileTree(tempDir, new FindSymbolicLinkVisitor());
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void zipFileSystem() {
        String[] data = { "Line 1", "Line 2 2", "Line 3 3 3", "Line 4 4 4 4", "Line 5 5 5 5 5" };

        try (FileSystem zipFs = ZipHelper.openZip(Paths.get("./assets/myData.zip"))) {
            ZipHelper.copyToZip(zipFs);
            ZipHelper.writeToFileInZip1(zipFs, data);
            ZipHelper.writeToFileInZip2(zipFs, data);
        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }
}
