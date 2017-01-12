package com.amihaiemil.camel;

import java.util.Collection;

/**
 * YAML mapping.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#mapping//
 */
final class Mapping extends AbstractNode {

    /**
     * Ctor.
     * @param parent The parent node of this mapping.
     */
    Mapping(AbstractNode parent) {
        super(parent);
    }

    @Override
    public Collection<AbstractNode> children() {
        return null;
    }

}
