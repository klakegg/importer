package net.klakegg.importer;

import com.google.common.collect.Lists;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;
import net.klakegg.importer.io.ExtraFiles;
import net.klakegg.importer.loader.DependencyLoader;
import net.klakegg.importer.loader.ImportsLoader;
import net.klakegg.importer.model.*;
import net.klakegg.importer.module.ArgumentModule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author erlend
 */
@Slf4j
public class Main {

    @Inject
    private DependencyLoader dependencyLoader;

    @Inject
    private ImportsLoader importsLoader;

    @Inject
    private Arguments arguments;

    private static String modulePath = ".module";

    private static String importsPath = "imports.yaml";

    public static void main(String... args) {
        // Load modules
        List<Module> modules = Lists.newArrayList(ServiceLoader.load(Module.class));
        modules.add(new ArgumentModule(args));

        // Load context
        try {
            Guice.createInjector(modules).getInstance(Main.class).run();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void run() throws Exception {
        // Fix target folder
        log.info("Preparing target folder.");
        if (Files.exists(arguments.getTarget()))
            MoreFiles.deleteDirectoryContents(arguments.getTarget(), RecursiveDeleteOption.ALLOW_INSECURE);
        else
            Files.createDirectories(arguments.getTarget());

        // Find configuration files
        Map<String, List<Path>> filesMap = ExtraFiles.listFiles(arguments.getSource())
                .filter(p -> Arrays.asList(modulePath, importsPath).contains(p.getFileName().toString()))
                .collect(Collectors.groupingBy(p -> p.getFileName().toString()));

        // Parse imports files
        Map<Path, Imports> importsMap = new HashMap<>();
        if (filesMap.containsKey(importsPath))
            for (Path path : filesMap.get(importsPath))
                importsMap.put(path, importsLoader.read(arguments.getSource().resolve(path)));

        // Prepare modules based on .module files.
        Map<String, Path> moduleMap = new HashMap<>();
        if (filesMap.containsKey(modulePath)) {
            for (Path path : filesMap.get(modulePath)) {
                log.info("Loading module '{}'.", path.getParent().getFileName());
                moduleMap.put(path.getParent().getFileName().toString(), arguments.getTarget().resolve(path.getParent()));
            }
        }

        // Prepare modules based on dependency declarations.
        for (Map.Entry<Path, Imports> importsEntry : importsMap.entrySet()) {
            if (importsEntry.getValue().getDependencies() != null) {
                for (Map.Entry<String, Dependency> dependencyEntry : importsEntry.getValue().getDependencies().entrySet()) {
                    log.info("Loading module '{}'.", dependencyEntry.getKey());
                    moduleMap.put(dependencyEntry.getKey(), dependencyLoader.get(dependencyEntry.getValue(), arguments.getTarget().resolve(importsEntry.getKey()).getParent()));
                }
            }
        }

        // Copy source content
        log.info("Copy source content.");
        for (Path path : ExtraFiles.listFiles(arguments.getSource())
                .filter(p -> !Arrays.asList(modulePath, importsPath).contains(p.getFileName().toString()))
                .collect(Collectors.toList()))
            ExtraFiles.copy(arguments.getSource().resolve(path), arguments.getTarget().resolve(path));

        // Copy imports
        for (Map.Entry<Path, Imports> importsEntry : importsMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .sorted(Comparator.comparingInt(e -> e.getValue().getWeight()))
                .collect(Collectors.toList())) {
            if (importsEntry.getValue().getFrom() != null) {
                log.info("Handling '{}'.", importsEntry.getKey());

                for (From from : importsEntry.getValue().getFrom()) {
                    for (Resource resource : from.getResources()) {
                        Path source = moduleMap.get(from.getIdentifier()).resolve(resource.getPath());
                        Path target = arguments.getTarget().resolve(importsEntry.getKey()).getParent().resolve(resource.getAs());

                        if (Files.isDirectory(source)) {
                            for (Path path : ExtraFiles.listFiles(source).collect(Collectors.toList()))
                                ExtraFiles.copy(source.resolve(path), target.resolve(path));
                        } else {
                            ExtraFiles.copy(source, target);
                        }
                    }
                }
            }
        }
    }
}
