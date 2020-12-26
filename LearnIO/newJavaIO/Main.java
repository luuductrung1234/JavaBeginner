package newJavaIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;

import util.logging.CustomLogger;
import util.exception.ExceptionExtensions;

public class Main {
    public static volatile boolean shutdown = false;

    private static Logger logger = CustomLogger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        int choice = 0;
        try {
            choice = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Please choose a specific demo to run!");
        }

        /**
         * implement a simple shutdown mechanism for Java main class
         */
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    logger.warning("[Application] Shutting Down!");
                    shutdown = true;
                    mainThread.join();
                    logger.warning("[Application] Shuted Down!");
                } catch (InterruptedException e) {
                    ExceptionExtensions.logMe(e, logger);
                }
            }
        });

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
                walkingThroughDirectoryTree();
                break;
            case 6:
                createAndDeleteFile();
                break;
            case 7:
                copyAndMovingFile();
                break;
            case 8:
                fileAttributes();
                break;
            case 9:
                readAndWrite();
                break;
            case 10:
                // this demo required shutdown mechanism
                watchingFileChangeWithNotification();
                break;
            case 11:
                fileChannel();
                break;
            case 12:
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

    public static void createAndDeleteFile() {
        try {
            Path tempDir = Paths.get("/Users/LDTrung/Temp");

            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-rw-rw-");
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

            Path file = Files.createFile(tempDir.resolve("my_stuff.txt"), attr);

            if (Files.exists(file))
                logger.info("Create Successfully File: [" + file.getFileName() + "]");

            Files.delete(file);

            if (!Files.exists(file))
                logger.info("Delete Successfully File: [" + file.getFileName() + "]");
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void copyAndMovingFile() {
        try {
            Path srcDir = Paths.get("/Users/LDTrung/Temp");
            Path destDir = Paths.get("/Users/LDTrung/CopiedTemp");

            Files.copy(srcDir.resolve("dump_text.txt"), destDir.resolve("copied_dump_text.txt"),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.move(srcDir.resolve("copied_dump_text.txt"), destDir.resolve("moved/dump_text.txt"),
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void fileAttributes() {
        try {
            Path tempDir = Paths.get("/Users/LDTrung/Temp");
            Path file = tempDir.resolve("dump_text.txt");

            PosixFileAttributes posixAttr = Files.readAttributes(file, PosixFileAttributes.class);
            BasicFileAttributes basicAttr = Files.readAttributes(file, BasicFileAttributes.class);

            logger.log(Level.INFO, "File size: [{0}]", basicAttr.size());
            logger.log(Level.INFO, "Is regular file: [{0}]", basicAttr.isRegularFile());
            logger.log(Level.INFO, "Is symbolic link: [{0}]", basicAttr.isSymbolicLink());
            posixAttr.permissions().stream().forEach((perm) -> {
                logger.log(Level.INFO, "permission: [{0}]", perm.name());
            });

            Set<PosixFilePermission> perms = posixAttr.permissions();
            perms.clear();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.OTHERS_READ);
            Files.setPosixFilePermissions(file, perms);
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void readAndWrite() {
        Path logFile = Paths.get("/Users/LDTrung/Temp/app.log");
        // Read file using old Java.IO
        try (BufferedReader reader = Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }

        // Write file using old Java.IO
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8,
                StandardOpenOption.WRITE)) {
            writer.write("Hello World!");
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }

        // Read file using new Java.nIO
        try {
            List<String> lines = Files.readAllLines(logFile, StandardCharsets.UTF_8);
            logger.log(Level.INFO, "Read [{0}] lines", lines.size());

            byte[] bytes = Files.readAllBytes(logFile);
            logger.log(Level.INFO, "Read [{0}] bytes", bytes.length);
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }

        // Write file using new Java.nIO
        try {
            List<String> lines = Arrays.asList("With Java NIO", "We can read and write to file", "very easy!");
            byte[] bytes = ArrayUtils.toPrimitive(lines.toArray(new Byte[lines.size()]));

            Files.write(logFile, lines, StandardOpenOption.WRITE);
            logger.log(Level.INFO, "Write [{0}] lines", lines.size());

            Files.write(logFile, bytes, StandardOpenOption.WRITE);
            logger.log(Level.INFO, "Write [{0}] bytes", bytes.length);
        } catch (IOException e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    /**
     * using java.nio.file.WatchService
     * 
     * This class uses client threads to keep an eye on registered files or
     * directories for changes, and will return an event when a change is detected
     * 
     * The event notification can be useful for security monitoring, refreshing data
     * from properties files,...
     * 
     * It's ideal for replacing the (comparatively poorly performing) polling
     * mechanism.
     */
    public static void watchingFileChangeWithNotification() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path tempDir = FileSystems.getDefault().getPath("/Users/LDTrung/Temp");

            WatchKey key = tempDir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            logger.info("Thread start");

            while (!shutdown) {
                logger.log(Level.INFO, "{0} - listening...", shutdown);

                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        logger.log(Level.INFO, "temp directory was modified - [{0}]", event);
                    }
                }
                key.reset();
            }

            logger.info("Thread end");
        } catch (IOException | InterruptedException e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void fileChannel() {
        Path textFile = Paths.get("assets/TomSawyer.txt");

        if (Files.exists(textFile)) {
            logger.log(Level.INFO, "Read File {0}", textFile.toAbsolutePath());
        } else {
            logger.log(Level.INFO, "File {0} does not existed!", textFile.toAbsolutePath());
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try (FileChannel channel = FileChannel.open(textFile, StandardOpenOption.READ)) {
            channel.read(buffer, channel.size() - 1000);
            logger.log(Level.INFO, "File Content: \n===================\n{0}\n===================\n",
                    new String(buffer.array(), StandardCharsets.UTF_8));
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
