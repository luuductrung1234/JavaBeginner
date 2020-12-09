package newJavaIO;

import java.nio.file.FileSystem;
import java.nio.file.Paths;
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
                zipFileSystem();
                break;
            default:
                zipFileSystem();
                break;
        }
    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

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
