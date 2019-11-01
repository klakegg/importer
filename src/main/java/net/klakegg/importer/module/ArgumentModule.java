package net.klakegg.importer.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.klakegg.importer.model.Arguments;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Paths;

/**
 * @author erlend
 */
public class ArgumentModule extends AbstractModule {

    private String[] args;

    public ArgumentModule(String[] args) {
        this.args = args;
    }

    @Provides
    @Singleton
    public Options getOptions() {
        return new Options()
                .addOption("s", "source", true, "Source folder")
                .addOption("t", "target", true, "Target folder");
    }

    @Provides
    @Singleton
    public CommandLine getCommandLine(Options options) throws ParseException {
        return new DefaultParser().parse(options, args);
    }

    @Provides
    @Singleton
    public Arguments getArguments(CommandLine commandLine) {
        return Arguments.builder()
                .source(Paths.get(commandLine.getOptionValue("source", "src")))
                .target(Paths.get(commandLine.getOptionValue("target", "target")))
                .build();
    }
}
