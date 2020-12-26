package newJavaIO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;

public class ZipHelper {
    private ZipHelper() {
    }

    public static FileSystem openZip(Path zipPath) throws IOException, URISyntaxException {
        /**
         * whenever we construct a file system, we can specify provider-specific
         * properties for that file system
         */

        var providerProps = new HashMap<String, String>();
        providerProps.put("create", "true");

        var zipUri = new URI("jar:file", zipPath.toUri().getPath(), null);
        return FileSystems.newFileSystem(zipUri, providerProps);
    }

    public static void copyToZip(FileSystem zipFs) throws IOException {
        // first way to get path of source file
        // Path sourceFile = Paths.get("./assets/file1.txt");

        // second way to get path of source file
        Path sourceFile = FileSystems.getDefault().getPath("./assets/file1.txt");

        Path destFile = zipFs.getPath("/file1Copied.txt");

        Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void writeToFileInZip1(FileSystem zipFs, String[] data) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(zipFs.getPath("/newFile1.txt"))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void writeToFileInZip2(FileSystem zipFs, String[] data) throws IOException {
        Files.write(zipFs.getPath("/newFile2.txt"), Arrays.asList(data), Charset.defaultCharset(),
                StandardOpenOption.CREATE);
    }
}
