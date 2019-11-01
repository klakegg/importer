package net.klakegg.importer.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author erlend
 */
@Getter
@Setter
public class Dependency {

    private String type = "local";

    private String identifier;

    private String path = "";

}
