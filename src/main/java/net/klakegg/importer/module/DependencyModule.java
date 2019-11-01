package net.klakegg.importer.module;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.klakegg.importer.api.DependencyParser;
import net.klakegg.importer.parser.LocalDependencyParser;
import net.klakegg.importer.parser.GithubDependencyParser;
import org.kohsuke.MetaInfServices;

/**
 * @author erlend
 */
@MetaInfServices(Module.class)
public class DependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        // Github
        bind(Key.get(DependencyParser.class, Names.named("github")))
                .to(GithubDependencyParser.class)
                .in(Singleton.class);
        // Local
        bind(Key.get(DependencyParser.class, Names.named("local")))
                .to(LocalDependencyParser.class)
                .in(Singleton.class);
    }
}
