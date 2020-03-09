/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.amihaiemil.eoyaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Iterable yaml lines.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
interface YamlLines extends Iterable<YamlLine> {

    /**
     * All the YAML lines, as a collection.
     * @return These YamlLines as a Collection.
     */
    Collection<YamlLine> lines();
    
    /**
     * Number of lines.
     * @return Integer.
     */
    int count();

    /**
     * Default iterator which doesn't skip any line,
     * iterates over all of them.
     * @return Iterator of YamlLine.
     */
    default Iterator<YamlLine> iterator() {
        return this.lines().iterator();
    }
    
    /**
     * Indent these lines.
     * @param indentation Spaces to precede each line.
     * @return String with the pretty-printed, indented lines.
     */
    default String indent(final int indentation) {
        final StringBuilder indented = new StringBuilder();
        final Iterator<YamlLine> linesIt = this.iterator();
        if(linesIt.hasNext()) {
            final YamlLine first = linesIt.next();
            if (first.indentation() == indentation) {
                indented.append(first.toString()).append("\n");
                while (linesIt.hasNext()) {
                    indented.append(linesIt.next().toString()).append("\n");
                }
            } else {
                final int offset = indentation - first.indentation();
                for (final YamlLine line : this.lines()) {
                    int correct = line.indentation() + offset;
                    while (correct > 0) {
                        indented.append(" ");
                        correct--;
                    }
                    indented.append(line.trimmed()).append("\n");
                }
            }
        }
        return indented.toString();
    }
    
    /**
     * Lines which are nested after the given YamlLine (lines which are
     * <br> indented by 2 or more spaces beneath it).
     * @param after Number of a YamlLine
     * @return YamlLines
     */
    default AllYamlLines nested(final int after) {
        final List<YamlLine> nestedLines = new ArrayList<YamlLine>();
        YamlLine start = null;
        for(final YamlLine line : this.lines()) {
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
        return new AllYamlLines(nestedLines);
    }
    
    
    /**
     * Turn these lines into a YamlNode.
     * @param prev Previous YamlLine
     * @return YamlNode
     * @todo #107:30min/DEV Add more tests to cover all the nested node
     *  possibilities.
     */
    default YamlNode toYamlNode(final YamlLine prev) {
        final String trimmed = prev.trimmed();
        final String last = trimmed.substring(trimmed.length()-1);
        final YamlNode node;
        switch (last) {
            case Nested.YAML:
                final boolean sequence = this.iterator()
                    .next().trimmed().startsWith("-");
                if(sequence) {
                    node = new ReadYamlSequence(new SameIndentationLevel((AllYamlLines) this));
                } else {
                    node = new ReadYamlMapping(new SameIndentationLevel((AllYamlLines) this));
                }
                break;
            case Nested.KEY_YAML:
                final boolean sequenceKey = this.iterator()
                    .next().trimmed().startsWith("-");
                if(sequenceKey) {
                    node = new ReadYamlSequence(new SameIndentationLevel((AllYamlLines) this));
                } else {
                    node = new ReadYamlMapping(new SameIndentationLevel((AllYamlLines) this));
                }
                break;
            case Nested.SEQUENCE:
                if(trimmed.length() == 1) {
                    final boolean elementSequence = this.iterator()
                        .next().trimmed().startsWith("-");
                    if(elementSequence) {
                        node = new ReadYamlSequence(new SameIndentationLevel((AllYamlLines) this));
                    } else {
                        node = new ReadYamlMapping(new SameIndentationLevel((AllYamlLines) this));
                    }
                } else {
                    node = new ReadYamlSequence(new SameIndentationLevel((AllYamlLines) this));
                }
                break;
            case Nested.PIPED_SCALAR:
                node = new ReadPipeScalar(this);
                break;
            case Nested.POINTED_SCALAR:
                node = new ReadPointedScalar(this);
                break;
            default:
                throw new IllegalStateException(
                    "No nested Yaml node after line " + prev.number()
                    + " which has [" + last + "] character at the end"
                );
        }
        return node;
    }

}
