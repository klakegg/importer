package net.klakegg.importer.api;

import net.klakegg.importer.model.Dependency;

import java.nio.file.Path;

/**
 * @author erlend
 */
public interface DependencyParser {

    Path load(Dependency dependency, Path contextPath);

}
