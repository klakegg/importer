package net.klakegg.importer.parser;

import net.klakegg.importer.api.DependencyParser;
import net.klakegg.importer.model.Dependency;

import java.nio.file.Path;

/**
 * @author erlend
 */
public class LocalDependencyParser implements DependencyParser {

    @Override
    public Path load(Dependency dependency, Path contextPath) {
        return contextPath.resolve(dependency.getPath());
    }
}
