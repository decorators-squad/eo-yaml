package com.amihaiemil.camel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * YamlLines default implementation.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @todo #63:30min/DEV After ticket #64 is resolved, make sure method
 *  toYamlNode from this class is completed. (ReadYamlSequence instead of null)
 */
final class RtYamlLines implements YamlLines {

    /**
     * Yaml lines.
     */
    private Collection<YamlLine> lines;

    /**
     * Ctor.
     * @param lines Yaml lines collection.
     */
    RtYamlLines(final Collection<YamlLine> lines) {
        this.lines = lines;
    }

    /**
     * Returns an iterator over these Yaml lines.
     * It <b>only</b> iterates over the lines which are at the same
     * level of indentation with the first!
     * @return Iterator over these yaml lines.
     */
    @Override
    public Iterator<YamlLine> iterator() {
        Iterator<YamlLine> iterator = this.lines.iterator();
        if (iterator.hasNext()) {
            final List<YamlLine> sameIndentation = new ArrayList<>();
            final YamlLine first = iterator.next();
            sameIndentation.add(first);
            while (iterator.hasNext()) {
                YamlLine current = iterator.next();
                if(current.indentation() == first.indentation()) {
                    sameIndentation.add(current);
                }
            }
            iterator = sameIndentation.iterator();
        }
        return iterator;
    }

    @Override
    public YamlLines nested(final int after) {
        final List<YamlLine> nestedLines = new ArrayList<YamlLine>();
        YamlLine start = null;
        for(final YamlLine line: this.lines) {
            if(line.number() == after) {
                start = line;
            }
            if(line.number() > after) {
                if(line.indentation() > start.indentation()) {
                    nestedLines.add(line);   
                } else {
                    break;
                }
            }
        }
        return new RtYamlLines(nestedLines);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final YamlLine line : this.lines) {
            builder.append(line.toString()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public YamlNode toYamlNode() {
        final YamlNode node;
        boolean sequence = false;
        for (final YamlLine line : this) {
            if (line.trimmed().startsWith("-")) {
                sequence = true;
            } else {
                if(sequence) {
                    throw new IllegalStateException(
                        "Line " + line.number() + " should start with '-', "
                        + "as it is part of a sequence"
                    );
                }
                break;
            }
        }
        if(sequence) {
            node = null;
        } else {
            node = new ReadYamlMapping(this);
        }
        return node;
    }

}
