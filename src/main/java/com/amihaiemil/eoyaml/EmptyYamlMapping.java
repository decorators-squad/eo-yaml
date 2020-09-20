package com.amihaiemil.eoyaml;

import java.util.Collections;
import java.util.Set;

/**
 * Representation of an empty YAML Mapping (or "{}").  A decorator around
 * {@link YamlMapping} with no keys.
 * @author Andrew Newman
 * @version $Id$
 * @since 5.1.17
 */
public class EmptyYamlMapping extends BaseYamlMapping {

    /**
     * Decorated object - used for getting comments.
     */
    private final YamlMapping mapping;

    /**
     * Wrap an existing mapping - expects comments() to be implemented.
     * @param mapping The mapping to wrap.
     */
    public EmptyYamlMapping(final YamlMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public final Set<YamlNode> keys() {
        return Collections.emptySet();
    }

    @Override
    public final YamlNode value(final YamlNode key) {
        return null;
    }

    @Override
    public final Comment comment() {
        return this.mapping.comment();
    }
}
