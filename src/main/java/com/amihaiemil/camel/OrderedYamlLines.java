/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
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
package com.amihaiemil.camel;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Ordered YamlLines. Use this decorator only punctually, when it the
 * AbstractYamlLines you're working with need to be ordered.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class OrderedYamlLines extends AbstractYamlLines {

    /**
     * Lines to order.
     */
    private AbstractYamlLines unordered;

    /**
     * Ctor.
     * @param unordered Decorated lines.
     */
    OrderedYamlLines(final AbstractYamlLines unordered) {
        this.unordered = unordered;
    }

    /**
     * Iterates over the lines with the same indentation. The lines
     * are ordered. <br><br> This method is a little more complex - we're not
     * simply sorting the lines. We have to also cover the case when these
     * lines are a sequence; then a line might be just a simple dash,
     * so we have to order these "dash" lines by the node that is nested under
     * them.
     * @return Iterator over the ordered lines.
     */
    @Override
    public Iterator<YamlLine> iterator() {
        final Iterator<YamlLine> lines = this.unordered.iterator();
        final List<YamlLine> ordered = new LinkedList<>();
        final List<YamlLine> dashes = new LinkedList<>();
        final Map<YamlNode, Integer> nodesInSequence = new TreeMap<>();
        int index = 0;
        while (lines.hasNext()) {
            final YamlLine line = lines.next();
            if("-".equals(line.trimmed())) {
                nodesInSequence.put(
                    this.nested(line.number()).toYamlNode(line),
                    index
                );
                dashes.add(line);
                index = index + 1;
            } else {
                ordered.add(line);
            }
        }
        Collections.sort(ordered);
        for (final Integer idx : nodesInSequence.values()) {
            ordered.add(dashes.get(idx));
        }
        return ordered.iterator();
    }

    /**
     * Returns the lines which are nested after the given line. 
     * The lines are not necessarily ordered. If the resulting lines
     * should be ordered (be iterated in order), then they have
     * to be wrapped inside a new OrderedYamlLines.
     * @return AbstractYamlLines
     * @param after The number of the parent line
     */
    @Override
    AbstractYamlLines nested(final int after) {
        return this.unordered.nested(after);
    }

    @Override
    int count() {
        return this.unordered.count();
    }

    @Override
    String indent(final int indentation) {
        final StringBuilder indented = new StringBuilder();
        final Iterator<YamlLine> linesIt = this.iterator();
        final YamlLine first = linesIt.next();
        final int offset = indentation - first.indentation();
        indented.append(this.indentLine(first, offset));
        while(linesIt.hasNext()) {
            indented.append(
                this.indentLine(linesIt.next(), offset)
            );
        }
        return indented.toString();
    }

    /**
     * Indent the given line (and its possible nested nodes) with the given
     * offset.
     * @param line YamlLine to indent.
     * @param offset Offset added to its already existing indentation.
     * @return String indented result.
     */
    private String indentLine(final YamlLine line, final int offset){
        final StringBuilder indented = new StringBuilder();
        int indentation = line.indentation() + offset;
        while (indentation > 0) {
            indented.append(" ");
            indentation--;
        }
        indented.append(line.toString()).append("\n");
        if (line.hasNestedNode()) {
            final OrderedYamlLines nested = new OrderedYamlLines(
                this.unordered.nested(line.number())
            );
            indented.append(nested.indent(indentation + offset));
        }
        return indented.toString();
    }
}
