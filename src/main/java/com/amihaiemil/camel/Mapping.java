package com.amihaiemil.camel;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * YAML mapping.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#mapping//
 */
final class Mapping extends AbstractNode {

    /**
     * Key:value tree map (ordered keys).
     */
    private final Map<AbstractNode, AbstractNode> mappings =
        new TreeMap<AbstractNode, AbstractNode>();
    
    /**
     * Ctor.
     * @param parent The parent node of this mapping.
     */
    Mapping(final AbstractNode parent) {
        super(parent);
    }

    @Override
    public Collection<AbstractNode> children() {
        return this.mappings.values();
    }

    /**
     * Compare this Mapping to another node.
     * @return
     *  -1 if this < o <br>
     *   0 if this == o or <br>
     *   1 if this > o
     */
    @Override
    public int compareTo(AbstractNode o) {
        int result = 1;
        if (o instanceof Mapping) {
            Mapping map = (Mapping) o;
        }
        return result;
    }
}
