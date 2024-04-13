/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A read YamlMapping in flow format, where elements are
 * between curly brackets, separated by a comma.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 8.0.0
 * @todo #615:60min Implement the comment() method properly (at the moment
 *  it always assumes there is no comment).
 */
final class ReadFlowMapping extends BaseYamlMapping {

    /**
     * The entries of this flow mapping as String.
     */
    private final StringEntries entries;

    /**
     * This flow mapping folded on a single line.
     */
    private final YamlLine folded;

    /**
     * Ctor.
     * @param lines All lines of the YAML document.
     */
    ReadFlowMapping(final AllYamlLines lines) {
        this(new YamlLine.NullYamlLine(), lines);
    }

    /**
     * Ctor.
     * @param previous Line just before the start of this flow mapping.
     * @param lines All lines of the YAML document.
     * @checkstyle AvoidInlineConditionals (30 lines)
     */
    ReadFlowMapping(final YamlLine previous, final AllYamlLines lines) {
        this(
            new CollapsedFlowLines(
                new Skip(
                    lines,
                    line -> line.number() <= previous.number(),
                    line -> line.trimmed().startsWith("#"),
                    line -> line.trimmed().startsWith("---"),
                    line -> line.trimmed().startsWith("..."),
                    line -> line.trimmed().startsWith("%"),
                    line -> line.trimmed().startsWith("!!")
                ),
                '{',
                '}'
            ).line(previous.number() < 0 ? 0 : previous.number() + 1)
        );
    }

    /**
     * Constructor.
     * @param folded All the YAML lines of this flow mapping,
     *  folded into a single one.
     */
    ReadFlowMapping(final YamlLine folded) {
        this.entries = new StringEntries(folded);
        this.folded = folded;
    }

    @Override
    public Set<YamlNode> keys() {
        final Set<YamlNode> keys = new LinkedHashSet<>();
        for(final Map.Entry<String, String> entry : this.entries) {
            keys.add(this.stringToYamlNodeNode(entry.getKey()));
        }
        return keys;
    }

    //@checkstyle ReturnCount (50 lines)
    @Override
    public YamlNode value(final YamlNode key) {
        for(final Map.Entry<String, String> entry : this.entries) {
            final YamlNode entryKey = this.stringToYamlNodeNode(
                entry.getKey()
            );
            if(entryKey.equals(key)) {
                return this.stringToYamlNodeNode(entry.getValue());
            }
        }
        return null;
    }

    @Override
    public Comment comment() {
        return new BuiltComment(this, "");
    }

    /**
     * Turn the string input into a YamlNode.
     * @param node String Yaml node.
     * @return YamlNode, never null.
     */
    private YamlNode stringToYamlNodeNode(final String node) {
        final YamlNode yaml;
        if (node.startsWith("[")) {
            yaml = new ReadFlowSequence(
                new RtYamlLine(node, this.folded.number())
            );
        } else if (node.startsWith("{")) {
            yaml = new ReadFlowMapping(
                new RtYamlLine(node, this.folded.number())
            );
        } else {
            yaml = new PlainStringScalar(node.trim());
        }
        return yaml;
    }

    /**
     * This mappings's entries in String format.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 8.0.0
     * @checkstyle LineLength (100 lines)
     * @checkstyle ModifiedControlVariable (100 lines)
     * @checkstyle ParameterNumber (300 lines)
     * @checkstyle CyclomaticComplexity (300 lines)
     * @checkstyle ExecutableStatementCount (300 lines)
     */
    private static final class StringEntries implements Iterable<Map.Entry<String, String>> {

        /**
         * Line containing all the nodes.
         */
        private final YamlLine line;

        /**
         * Ctor.
         * @param line Line containing the nodes.
         */
        StringEntries(final YamlLine line) {
            this.line = line;
        }

        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            final List<String> keys = new ArrayList<>();
            final List<String> values = new ArrayList<>();
            final String trimmed = line.trimmed();
            final String nodes = trimmed.substring(
                trimmed.indexOf('{') + 1,
                trimmed.lastIndexOf('}')
            );
            StringBuilder nodeBuilder = new StringBuilder();
            boolean startedQuoteEscape = false;
            boolean startedApEscape = false;
            for (int i = 0; i < nodes.length(); i++) {
                final char currentChar = nodes.charAt(i);
                if(currentChar == ':' && !startedApEscape && !startedQuoteEscape) {
                    keys.add(nodeBuilder.toString().trim());
                    nodeBuilder.setLength(0);
                    continue;
                }
                if (currentChar == ',' && !startedApEscape && !startedQuoteEscape) {
                    values.add(nodeBuilder.toString().trim());
                    nodeBuilder.setLength(0);
                    continue;
                }
                if (isEscapeChar(i, '\"', nodes) && !startedApEscape) {
                    startedQuoteEscape = !startedQuoteEscape;
                    nodeBuilder.append(currentChar);
                } else if (isEscapeChar(i, '\'', nodes) && !startedQuoteEscape) {
                    startedApEscape = !startedApEscape;
                    nodeBuilder.append(currentChar);
                } else if ((currentChar == '[' || currentChar == '{') && !(startedApEscape || startedQuoteEscape)) {
                    String nestedNode = this.readNode(i, nodes, currentChar);
                    nodeBuilder.append(nestedNode);
                    i += nestedNode.length() - 1;
                } else {
                    nodeBuilder.append(currentChar);
                }
            }
            if (!nodeBuilder.toString().trim().isEmpty()) {
                values.add(nodeBuilder.toString().trim());
            }
            if(keys.size() != values.size()) {
                throw new IllegalStateException("Invalid flow YamlMapping!");
            }
            final Map<String, String> mapping = new LinkedHashMap<>();
            for(int i=0; i< keys.size(); i++) {
                mapping.put(keys.get(i), values.get(i));
            }
            return mapping.entrySet().iterator();
        }

        /**
         * Read/go over a nested node.
         * @param start Start index.
         * @param nodes String containing all the nodes.
         * @param opening Opening bracket ([ or {).
         * @return Integer index where the read node ends.
         */
        private String readNode(
            final int start,
            final String nodes,
            final char opening
        ) {
            char closing;
            if(opening == '{') {
                closing = '}';
            } else {
                closing = ']';
            }
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
            return node.toString();
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
            if(isEscapeChar(i, escapeChar, nodes)) {
                node.append(nodes.charAt(i));
                i++;
                while(!isEscapeChar(i, escapeChar, nodes)) {
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

        /**
         * Does the string contain an escape chartacter a the specified index?
         * @param start Index.
         * @param escapeChar Escape character to look for.
         * @param nodes String containing the characters.
         * @return True if the escape character is found and it is
         *  not preceded by a backslash.
         */
        private boolean isEscapeChar(
            final int start, final char escapeChar, final String nodes
        ) {
            final boolean result;
            if (nodes.charAt(start) == escapeChar) {
                result = start == 0 || nodes.charAt(start - 1) != '\\';
            } else {
                result = false;
            }
            return result;
        }
    }
}
