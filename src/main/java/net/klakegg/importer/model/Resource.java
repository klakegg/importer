package net.klakegg.importer.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author erlend
 */
@Getter
@Setter
public class Resource {

    private String path;

    private String as;

    public String getAs() {
        return as == null ? path : as;
    }
}
