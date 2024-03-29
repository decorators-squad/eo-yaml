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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * YamlSequence read from somewhere.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class ReadYamlSequence extends BaseYamlSequence {

    /**
     * Yaml line just previous to the one where this sequence starts. E.g.
     * <pre>
     * 0  sequence:
     * 1    - elem1
     * 2    - elem2
     * </pre>
     * In the above example the sequence consists of elem1 and elem2, while
     * "previous" is line 0. If the sequence starts at the root, then line
     * "previous" is {@link com.amihaiemil.eoyaml.YamlLine.NullYamlLine}; E.g.
     * <pre>
     * 0  - elem1
     * 1  - elem2
     * </pre>
     */
    private final YamlLine previous;

    /**
     * All lines of the YAML document.
     */
    private final AllYamlLines all;

    /**
     * Only the significant lines of this sequence.
     */
    private final YamlLines significant;

    /**
     * Ctor.
     * @param lines Given lines.
     */
    ReadYamlSequence(final AllYamlLines lines) {
        this(new YamlLine.NullYamlLine(), lines);
    }

    /**
     * Ctor.
     * @param previous Line just before the start of this sequence.
     * @param lines Given lines.
     */
    ReadYamlSequence(final YamlLine previous, final AllYamlLines lines) {
        this.previous = previous;
        this.all = lines;
        this.significant = new SameIndentationLevel(
            new WellIndented(
                new Skip(
                    lines,
                    line -> line.number() <= previous.number(),
                    line -> line.trimmed().startsWith("#"),
                    line -> line.trimmed().startsWith("---"),
                    line -> line.trimmed().startsWith("..."),
                    line -> line.trimmed().startsWith("%"),
                    line -> line.trimmed().startsWith("!!")
                )
            ),
            false
        );
    }

    /**
     * Retrieve the values of this sequence.
     * @checkstyle CyclomaticComplexity (200 lines)
     * @checkstyle ExecutableStatementCount (300 lines)
     */
    @Override
    public Collection<YamlNode> values() {
        final List<YamlNode> kids = new LinkedList<>();
        final boolean foldedSequence = this.previous.trimmed().matches(
            "^.*\\|.*\\-$"
        );
        boolean innerValueStarted = false;
        for(final YamlLine line : this.significant) {
            final String trimmed = line.trimmed();
            if(foldedSequence || trimmed.startsWith("-")) {
                if ("-".equals(trimmed)
                    || trimmed.endsWith("|")
                    || trimmed.endsWith(">")
                ) {
                    innerValueStarted = true;
                    kids.add(this.significant.nextYamlNode(line));
                } else {
                    innerValueStarted = false;
                    if(this.blockMappingStartsAtDash(line)) {
                        kids.add(
                            new ReadYamlMapping(
                                line.number() + 1,
                                this.getPreviousLine(line),
                                this.all
                            )
                        );
                    } else if(this.flowSequenceStartsAtDash(line)) {
                        innerValueStarted = true;
                        kids.add(
                            new ReadFlowSequence(
                                this.getPreviousLine(line),
                                this.all
                            )
                        );
                    } else if(this.flowMappingStartsAtDash(line)) {
                        innerValueStarted = true;
                        kids.add(
                            new ReadFlowMapping(
                                this.getPreviousLine(line),
                                this.all
                            )
                        );
                    } else {
                        kids.add(new ReadPlainScalar(this.all, line));
                    }
                }
            } else {
                if(!innerValueStarted) {
                    break;
                }
            }
        }
        return kids;
    }

    @Override
    public Comment comment() {
        boolean documentComment = this.previous.number() < 0;
        //@checkstyle LineLength (50 lines)
        return new ReadComment(
            new Backwards(
                new FirstCommentFound(
                    new Backwards(
                        new Skip(
                            this.all,
                            line -> {
                                final boolean skip;
                                if(documentComment) {
                                    if(this.significant.iterator().hasNext()) {
                                        skip = line.number() >= this.significant
                                                .iterator().next().number();
                                    } else {
                                        skip = false;
                                    }
                                } else {
                                    skip = line.number() >= this.previous.number();
                                }
                                return skip;
                            },
                            line -> line.trimmed().startsWith("..."),
                            line -> line.trimmed().startsWith("%"),
                            line -> line.trimmed().startsWith("!!")
                        )
                    ),
                    documentComment
                )
            ),
            this
        );
    }

    /**
     * Returns true if there's a YamlMapping starting right after the
     * dash, on the same line.
     * @param dashLine Line.
     * @return True of false.
     */
    private boolean blockMappingStartsAtDash(final YamlLine dashLine) {
        final String trimmed = dashLine.trimmed();
        final boolean escapedScalar = trimmed.matches("^\\s*-\\s*\".*\"$")
            || trimmed.matches("^\\s*-\\s*'.*'$");
        return trimmed.matches("^.*-.+:(|\\s.*)$") && !escapedScalar;
    }

    /**
     * Returns true if there's a flow-style YamlSequence starting right after
     * the dash, on the same line.
     * @param dashLine Line.
     * @return True of false.
     */
    private boolean flowSequenceStartsAtDash(final YamlLine dashLine) {
        final String trimmed = dashLine.trimmed();
        return trimmed.matches("^\\s*-\\s*\\[.*$");
    }

    /**
     * Returns true if there's a flow-style YamlMapping starting right after
     * the dash, on the same line.
     * @param dashLine Line.
     * @return True of false.
     */
    private boolean flowMappingStartsAtDash(final YamlLine dashLine) {
        final String trimmed = dashLine.trimmed();
        return trimmed.matches("^\\s*-\\s*\\{.*$");
    }

    /**
     * Get the line previous to the given one or NullYamlLine if the
     * given line is the first one.
     * @param line Given YamlLine.
     * @return YamlLine previous to it.
     */
    private YamlLine getPreviousLine(final YamlLine line) {
        YamlLine prev;
        if (line.number() == 0) {
            prev = new YamlLine.NullYamlLine();
        } else {
            prev = this.all.line(line.number() - 1);
        }
        return prev;
    }
}
