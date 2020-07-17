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
 * A read YamlSequence in flow format (elements between square brackets).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 6.0.0
 * @todo #368:60min Implement and unit test class ReadFlowMapping, which
 *  will represent a mapping in flow format (between {} brackets).
 *  After that is done, use it here when reading the sequence's values.
 *  At the moment this flow sequence only reads sequence and scalar values.
 * @todo #368:60min Use this class to read flow YamlSequences which are
 *  children of other read nodes, and also when reading a flow/json-like
 *  YamlSequence directly from the YamlInput.
 */
final class ReadFlowSequence extends BaseYamlSequence {

    /**
     * This flow sequence folded on a single line.
     */
    private final YamlLine folded;

    /**
     * Ctor.
     * @param start Line where this flow sequence starts.
     * @param lines All lines of the YAML document.
     */
    ReadFlowSequence(final YamlLine start, final AllYamlLines lines) {
        this(
            new FoldedFlowLines(
                new Skip(
                    lines,
                    line -> line.number() < start.number(),
                    line -> line.trimmed().startsWith("#"),
                    line -> line.trimmed().startsWith("---"),
                    line -> line.trimmed().startsWith("..."),
                    line -> line.trimmed().startsWith("%"),
                    line -> line.trimmed().startsWith("!!")
                ),
                '[',
                ']'
            ).iterator().next()
        );
    }

    /**
     * Constructor.
     * @param folded All the YAML lines of this flow sequence,
     *  folded into a single one.
     */
    ReadFlowSequence(final YamlLine folded) {
        this.folded = folded;
    }

    @Override
    public Collection<YamlNode> values() {
        final List<YamlNode> kids = new ArrayList<>();
        for (final String node : new StringNodes(this.folded)) {
            if (node.startsWith("[")) {
                kids.add(
                    new ReadFlowSequence(
                        new RtYamlLine(node, this.folded.number())
                    )
                );
            } else {
                kids.add(
                    new PlainStringScalar(node.trim())
                );
            }
        }
        return kids;
    }

    @Override
    public Comment comment() {
        return new BuiltComment(this, "");
    }

    /**
     * This sequence's nodes in String format.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 6.0.0
     * @checkstyle LineLength (100 lines)
     * @checkstyle ModifiedControlVariable (100 lines)
     * @checkstyle ParameterNumber (300 lines)
     */
    private static final class StringNodes implements Iterable<String> {

        /**
         * Line containing all the nodes.
         */
        private final YamlLine line;

        /**
         * Ctor.
         * @param line Line containing the nodes.
         */
        StringNodes(final YamlLine line) {
            this.line = line;
        }

        @Override
        public Iterator<String> iterator() {
            final List<String> found;
            final String trimmed = line.trimmed();
            if("[]".equals(trimmed)) {
                found = new ArrayList<>();
            } else {
                found = new ArrayList<>();
                final String nodes = trimmed.substring(
                    trimmed.indexOf('[') + 1,
                    trimmed.lastIndexOf(']')
                );
                StringBuilder scalar = new StringBuilder();
                for (int i = 0; i < nodes.length(); i++) {
                    if (nodes.charAt(i) == '[') {
                        i = this.readNode(i, nodes, '[', ']', found);
                    } else if (nodes.charAt(i) == '{') {
                        i = this.readNode(i, nodes, '{', '}', found);
                    } else if (nodes.charAt(i) != ',' && nodes.charAt(i) != ' ') {
                        scalar.append(nodes.charAt(i));
                        if (i == nodes.length() - 1) {
                            found.add(scalar.toString().trim());
                        }
                    } else {
                        if (!scalar.toString().trim().isEmpty()) {
                            found.add(scalar.toString().trim());
                            scalar = new StringBuilder();
                        }
                    }
                }
            }
            return found.iterator();
        }

        /**
         * Read/go over a nested node.
         * @param start Start index.
         * @param nodes String containing all the nodes.
         * @param opening Opening bracket (usually [ or {).
         * @param closing Closing bracket (usually ] or }).
         * @param found List of nodes found so far, to add the
         *  read node to.
         * @return Integer index where the read node ends.
         */
        private int readNode(
            final int start,
            final String nodes,
            final char opening,
            final char closing,
            final List<String> found
        ) {
            final StringBuilder node = new StringBuilder();
            node.append(nodes.charAt(start));
            int nested = 1;
            int i = start;
            while(nested != 0) {
                i++;
                if(i == nodes.length()) {
                    throw new IllegalStateException(
                        "Could not find closing bracket " + closing
                      + " for node starting at " + start
                      + " on line " + (this.line.number() + 1)
                    );
                }
                i = goOverEscapedValue(node, i, nodes, '\"');
                i = goOverEscapedValue(node, i, nodes, '\'');
                node.append(nodes.charAt(i));
                if(nodes.charAt(i) == opening){
                    nested++;
                } else if(nodes.charAt(i) == closing) {
                    nested--;
                }
            }
            found.add(node.toString());
            return i;
        }

        /**
         * Go over escaped values (between quotes or apostrophes, usually).
         * We need to do this because a closing bracket ( ] or } ) should not
         * be taken into account if it is part of an escaped scalar.
         * @param node Node to append chars to.
         * @param start Start index.
         * @param nodes All the nodes.
         * @param escapeChar Escape char.
         * @return Integer index where the escaped value stops.
         */
        private int goOverEscapedValue(
            final StringBuilder node,
            final int start,
            final String nodes,
            final char escapeChar
        ) {
            int i = start;
            if(nodes.charAt(i) == escapeChar) {
                node.append(nodes.charAt(i));
                i++;
                while(nodes.charAt(i) != escapeChar) {
                    node.append(nodes.charAt(i));
                    i++;
                    if(i == nodes.length()) {
                        throw new IllegalStateException(
                            "Could not find closing pair (" + escapeChar
                          + ") for escaped value starting at " + start
                          + " on line " + (this.line.number() + 1)
                        );
                    }
                }
            }
            return i;
        }
    }
}
