package newJavaIO;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

import util.logging.CustomLogger;

public class FindSymbolicLinkVisitor extends SimpleFileVisitor<Path> {
    private static Logger logger = CustomLogger.getLogger(FindSymbolicLinkVisitor.class.getSimpleName());

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.toString().endsWith(".txt")) {
            logger.info("Found Text File: [" + file.getFileName() + "]");
        }

        if (Files.isSymbolicLink(file)) {
            logger.info("Skip Symbolic Link: [" + file.getFileName() + "]");
            return FileVisitResult.SKIP_SUBTREE;
        }

        return FileVisitResult.CONTINUE;
    }
}
