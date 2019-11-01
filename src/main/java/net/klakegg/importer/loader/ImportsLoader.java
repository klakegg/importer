package net.klakegg.importer.loader;

import com.google.inject.Singleton;
import net.klakegg.importer.model.Imports;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@Singleton
public class ImportsLoader {

    private final Yaml YAML = new Yaml(new Constructor(Imports.class));

    public Imports read(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            return YAML.loadAs(reader, Imports.class);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
