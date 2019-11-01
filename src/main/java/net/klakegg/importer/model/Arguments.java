package net.klakegg.importer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author erlend
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Arguments {

    private Path source;

    private Path target;

}
