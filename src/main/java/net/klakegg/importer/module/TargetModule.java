package net.klakegg.importer.module;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.klakegg.importer.model.Arguments;
import org.kohsuke.MetaInfServices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author erlend
 */
@MetaInfServices(Module.class)
public class TargetModule extends AbstractModule {

    @Provides
    @Singleton
    @Named("repository")
    public Path getRepository() {
        return Paths.get(".repository");
    }

    @Provides
    @Singleton
    @Named("target")
    public Path getTarget(Arguments arguments) throws IOException {
        if (Files.exists(arguments.getTarget()))
            MoreFiles.deleteDirectoryContents(arguments.getTarget(), RecursiveDeleteOption.ALLOW_INSECURE);
        else
            Files.createDirectories(arguments.getTarget());

        return arguments.getTarget();
    }

    @Provides
    @Singleton
    @Named("raw_external")
    public Path getRawExternal(@Named("target") Path target) {
        return target.resolve("_external");
    }
}
