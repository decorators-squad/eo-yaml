package com.amihaiemil.eoyaml;

import java.util.Collection;
import java.util.Collections;

/**
 * Representation of an empty YAML Sequence (or "[]").  A decorator around
 * {@link YamlSequence} with no values.
 * @author Andrew Newman
 * @version $Id$
 * @since 5.1.17
 */
public class EmptyYamlSequence extends BaseYamlSequence {

    /**
     * Decorated object - used for getting comments.
     */
    private final YamlSequence sequence;

    /**
     * Wrap an existing sequence - expects comments() to be implemented.
     * @param sequence The sequence to wrap.
     */
    public EmptyYamlSequence(final YamlSequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public final Collection<YamlNode> values() {
        return Collections.emptyList();
    }

    @Override
    public final Comment comment() {
        return sequence.comment();
    }
}
