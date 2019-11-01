package net.klakegg.importer.parser;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.klakegg.importer.api.DependencyParser;
import net.klakegg.importer.model.Dependency;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author erlend
 */
public class GithubDependencyParser implements DependencyParser {

    @Inject
    @Named("repository")
    private Path repository;

    @Inject
    @Named("raw_external")
    private Path target;

    @Override
    public Path load(Dependency dependency, Path contextPath) {
        String[] parts = dependency.getIdentifier().split(":");

        String url;
        String path;
        String target;

        switch (parts.length) {
            case 1:
                url = String.format("https://github.com/%s/archive/master.zip", parts[0]);
                path = String.format("github/%s/master.zip", parts[0]);
                target = String.format("github/%s/master", parts[0].replace("/", "--"));
                break;
            case 2:
                url = String.format("https://github.com/%s/archive/%s.zip", parts[0], parts[1]);
                path = String.format("github/%s/%s.zip", parts[0], parts[1]);
                target = String.format("github/%s/%s", parts[0].replace("/", "--"), parts[1]);
                break;
            case 3:
                url = String.format("https://github.com/%s/releases/download/%s/%s", parts[0], parts[1], parts[2]);
                path = String.format("github/%s/%s/%s", parts[0], parts[1], parts[2]);
                target = String.format("github/%s/%s/%s", parts[0].replace("/", "--"), parts[1], parts[2].replace(".zip", ""));
                break;
            default:
                throw new IllegalStateException(String.format("Unknown pattern: %s", dependency.getIdentifier()));
        }

        if (Files.notExists(repository.resolve(path))) {
            try (InputStream inputStream = new URL(url).openStream()) {
                Path to = repository.resolve(path);

                if (Files.notExists(to.getParent()))
                    Files.createDirectories(to.getParent());

                Files.copy(inputStream, to);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        if (Files.notExists(this.target.resolve(target))) {
            try (InputStream inputStream = Files.newInputStream(repository.resolve(path));
                 ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.isDirectory())
                        continue;

                    Path to = this.target.resolve(target).resolve(zipEntry.getName());

                    if (Files.notExists(to.getParent()))
                        Files.createDirectories(to.getParent());

                    Files.copy(zipInputStream, to);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        return this.target.resolve(target).resolve(dependency.getPath());
    }
}
