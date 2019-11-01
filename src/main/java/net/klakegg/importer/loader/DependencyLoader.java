package net.klakegg.importer.loader;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.klakegg.importer.api.DependencyParser;
import net.klakegg.importer.model.Dependency;

import java.nio.file.Path;

/**
 * @author erlend
 */
@Singleton
public class DependencyLoader {

    @Inject
    private Injector injector;

    public Path get(Dependency dependency, Path contextPath) {
        return injector.getInstance(Key.get(DependencyParser.class, Names.named(dependency.getType())))
                .load(dependency, contextPath);
    }
}
