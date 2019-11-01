package net.klakegg.importer.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author erlend
 */
public interface ExtraFiles {

    static Stream<Path> listFiles(Path folder) throws IOException {
        return Files.walk(folder)
                .filter(Files::isRegularFile)
                .map(p -> p.subpath(folder.getNameCount(), p.getNameCount()));
    }

    static void copy(Path source, Path target) throws IOException {
        if (Files.exists(target))
            return;

        if (target.getParent() != null)
            Files.createDirectories(target.getParent());

        Files.copy(source, target);
    }
}
