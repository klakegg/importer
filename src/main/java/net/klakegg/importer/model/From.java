package net.klakegg.importer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author erlend
 */
@Getter
@Setter
public class From {

    private String identifier;

    private List<Resource> resources;

}
