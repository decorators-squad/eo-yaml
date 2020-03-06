package com.amihaiemil.eoyaml;

import java.util.Collection;
import java.util.Iterator;
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
    public Collection<YamlNode> children() {
        final List<YamlNode> kids = new LinkedList<>();
        for(final YamlLine line : this.lines) {
            if("-".equals(line.trimmed())) {
                kids.add(this.lines.nested(line.number()).toYamlNode(line));
            } else {
                final String trimmed = line.trimmed();
                kids.add(
                    new Scalar(trimmed.substring(trimmed.indexOf('-')+1).trim())
                );
            }
        }
        return kids;
    }

    @Override
    public String toString() {
        return this.indent(0);
    }

    @Override
    public String indent(final int indentation) {
        return this.lines.indent(indentation);
    }

    @Override
    public YamlMapping yamlMapping(final int index) {
        YamlMapping mapping = null;
        int count = 0;
        for (final YamlNode node : this.children()) {
            if (count == index && node instanceof YamlMapping) {
                mapping = (YamlMapping) node;
            }
            count = count + 1;
        }
        return mapping;
    }

    @Override
    public YamlSequence yamlSequence(final int index) {
        YamlSequence sequence = null;
        int count = 0;
        for (final YamlNode node : this.children()) {
            if (count == index && node instanceof YamlSequence) {
                sequence = (YamlSequence) node;
            }
            count = count + 1;
        }
        return sequence;
    }

    @Override
    public String string(final int index) {
        String scalar = null;
        int count = 0;
        for (final YamlNode node : this.children()) {
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

    @Override
    public Iterator<YamlNode> iterator() {
        return this.children().iterator();
    }

}
