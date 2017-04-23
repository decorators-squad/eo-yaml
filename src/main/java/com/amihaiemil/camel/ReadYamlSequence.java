package com.amihaiemil.camel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * YamlSequence read from somewhere.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
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
    public Collection<AbstractYamlNode> children() {
        final List<AbstractYamlNode> kids = new LinkedList<>();
        final AbstractYamlLines ordered = new OrderedYamlLines(this.lines);
        for(final YamlLine line : ordered) {
            if("-".equals(line.trimmed())) {
                kids.add(this.lines.nested(line.number()).toYamlNode());
            } else {
                final String trimmed = line.trimmed();
                kids.add(
                    new Scalar(trimmed.substring(trimmed.indexOf('-')+1).trim())
                );
            }
        }
        return kids;
    }

    @Override public String toString() {
        return this.indent(0);
    }

    @Override
    public String indent(final int indentation) {
        return new OrderedYamlLines(this.lines).indent(indentation);
    }

    @Override
    public AbstractYamlMapping yamlMapping(final int index) {
        AbstractYamlMapping mapping = null;
        int count = 0;
        for (final AbstractYamlNode node : this.children()) {
            if (count == index && node instanceof AbstractYamlMapping) {
                mapping = (AbstractYamlMapping) node;
            }
            count = count + 1;
        }
        return mapping;
    }

    @Override
    public AbstractYamlSequence yamlSequence(final int index) {
        AbstractYamlSequence sequence = null;
        int count = 0;
        for (final AbstractYamlNode node : this.children()) {
            if (count == index && node instanceof AbstractYamlSequence) {
                sequence = (AbstractYamlSequence) node;
            }
            count = count + 1;
        }
        return sequence;
    }

    @Override
    public String string(final int index) {
        String scalar = null;
        int count = 0;
        for (final AbstractYamlNode node : this.children()) {
            if (count == index && node instanceof Scalar) {
                scalar = ((Scalar) node).value();
            }
            count = count + 1;
        }
        return scalar;
    }

    @Override
    @SuppressWarnings("unused")
    public int size() {
        int size = 0;
        for(final YamlLine line : this.lines) {
            size = size + 1;
        }
        return size;
    }

}
