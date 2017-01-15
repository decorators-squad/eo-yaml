package com.amihaiemil.camel;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
     * @param entries Entries contained in this mapping.
     */
    Mapping(
        final AbstractNode parent,
        final Map<AbstractNode, AbstractNode> entries
    ) {
        super(parent);
        this.mappings.putAll(entries);
    }

    /**
     * Fetch a value from this mapping.
     * @param key Key of the entry.
     * @return Corresponding node or null if there is no entry
     *  for the specified key.
     */
    public AbstractNode get(final AbstractNode key) {
        return this.mappings.get(key);
    }

    @Override
    public Collection<AbstractNode> children() {
        return this.mappings.values();
    }

    /**
     * Compare this Mapping to another node.<br><br>
     * 
     * A Mapping is always considered greater than a Scalar or a Sequence.<br>
     * 
     * If o is a Mapping, their integer lengths are compared - the one with
     * the greater length is considered greater. If the lengths are equal,
     * then the 2 Mappings are equal if all elements are equal (K==K and V==V).
     * If the elements are not identical, the comparison of the first unequal
     * elements is returned.
     * 
     * @param other The other AbstractNode.
     * @checkstyle NestedIfDepth (100 lines)
     * @checkstyle ExecutableStatementCount (100 lines)
     * @return
     *  a value < 0 if this < o <br>
     *   0 if this == o or <br>
     *  a value > 0 if this > o
     */
    @Override
    public int compareTo(final AbstractNode other) {
        int result = 0;
        if (other == null || other instanceof Scalar) {
            result = 1;
        } else if (other instanceof Mapping) {
            result = -1;
        } else if (this != other) {
            Mapping map = (Mapping) other;
            if(this.mappings.size() > map.mappings.size()) {
                result = 1;
            } else if (this.mappings.size() < map.mappings.size()) {
                result = -1;
            } else {
                Iterator<Entry<AbstractNode, AbstractNode>> entries =
                    this.mappings.entrySet().iterator();
                Iterator<Entry<AbstractNode, AbstractNode>> others =
                    map.mappings.entrySet().iterator();
                int keys;
                int values;
                while(entries.hasNext()) {
                    Entry<AbstractNode, AbstractNode> entry = entries.next();
                    Entry<AbstractNode, AbstractNode> oEntry = others.next();
                    keys = entry.getKey().compareTo(oEntry.getKey());
                    values = entry.getValue().compareTo(oEntry.getValue());
                    if(keys != 0) {
                        result = keys;
                        break;
                    }
                    if(values != 0) {
                        result = values;
                        break;
                    }
                }
            }
        }
        return result;
    }
}
