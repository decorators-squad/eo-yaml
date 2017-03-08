package com.amihaiemil.camel;

import java.util.Collection;

/**
 * YamlSequence read from somewhere.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @todo #64:30min/DEV Continue implementing and unit testing this class, 
 *  one method at a time.
 */
final class ReadYamlSequence extends AbstractYamlSequence {

    /**
     * Lines read.
     */
    private AbstractYamlLines lines;

    /**
     * Ctor.
     * @param lines Given lines.
     */
    ReadYamlSequence(final AbstractYamlLines lines) {
        this.lines = lines;
    }

    @Override
    public Collection<YamlNode> children() {
        return null;
    }

    @Override public String toString() {
        return this.indent(0);
    }

    @Override
    public String indent(final int indentation) {
        return this.lines.indent(indentation);
    }

    @Override
    public YamlMapping yamlMapping(final int index) {
        YamlMapping mapping = null;
        int current = 0;
        final AbstractYamlLines ordered = new OrderedYamlLines(this.lines);
        for (final YamlLine line : ordered) {
            if(current == index) {
                if (line.hasNestedNode()) {
                    mapping = new ReadYamlMapping(
                        this.lines.nested(line.number())
                    );
                } else {
                    throw new IllegalStateException(
                        "YamlSequence: a mapping should be nested "
                        + "(+2 indent) after line " + line.number()
                    );
                }
            }
        }
        if(mapping == null) {
            throw new IllegalStateException(
                "YamlSequence: no mapping found at index:" + index
            );
        }
        return mapping;
    }

    @Override
    public YamlSequence yamlSequence(final int index) {
        return null;
    }

    @Override
    public String string(final int index) {
        return null;
    }

}
