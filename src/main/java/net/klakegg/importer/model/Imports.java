package net.klakegg.importer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author erlend
 */
@Getter
@Setter
public class Imports {

    private int weight = 0;

    private Map<String, Dependency> dependencies;

    private List<From> from;

}
