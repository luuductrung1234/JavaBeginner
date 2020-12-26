package newJavaIO;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

import util.logging.CustomLogger;

public class FindJavaVisitor extends SimpleFileVisitor<Path> {
    private static Logger logger = CustomLogger.getLogger(FindJavaVisitor.class.getSimpleName());

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.toString().endsWith(".java")) {
            logger.info("Found Java Source Code: [" + file.getFileName() + "]");
        }

        return FileVisitResult.CONTINUE;
    }
}
