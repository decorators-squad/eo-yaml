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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation for {@link YamlScalarBuilder}. "Rt" stands for "Runtime.
 * This class is immutable and thread-safe.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
final class RtYamlScalarBuilder implements YamlScalarBuilder {

    /**
     * Added lines.
     */
    private final List<String> lines;

    /**
     * Default ctor.
     */
    RtYamlScalarBuilder() {
        this(new LinkedList<>());
    }

    /**
     * Constructor.
     * @param lines String lines of the Scalar.
     */
    RtYamlScalarBuilder(final List<String> lines) {
        this.lines = lines;
    }

    @Override
    public YamlScalarBuilder addLine(final String value) {
        final List<String> all = new LinkedList<>();
        all.addAll(this.lines);
        all.add(value);
        return new RtYamlScalarBuilder(all);
    }

    @Override
    public Scalar buildPlainScalar() {
        final String plain = this.lines.stream().map(
            line -> line.replaceAll(System.lineSeparator(), " ")
        ).collect(Collectors.joining(" "));
        return new PlainStringScalar(plain);
    }

    @Override
    public Scalar buildFoldedBlockScalar() {
        return new BuiltFoldedBlockScalar(this.lines);
    }

    @Override
    public Scalar buildLiteralBlockScalar() {
        return new BuiltLiteralBlockScalar(this.lines);
    }

    /**
     * A built folded block Scalar.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 4.0.0
     */
    static class BuiltFoldedBlockScalar extends BaseScalar {

        /**
         * Lines of this scalar.
         */
        private final List<String> lines;

        /**
         * Ctor.
         * @param lines Given string lines.
         */
        BuiltFoldedBlockScalar(final List<String> lines) {
            this.lines = lines;
        }

        /**
         * Return the value of this folded block scalar.
         * @todo #244:30min This method is not correct. When returning
         *  the value we should indeed fold the new lines, with the exception
         *  that we should preserve the blank lines and new lines of more inner
         *  indented lines. See how ReadFoldedBlockScalar.value()
         *  is implemented.
         * @return String value.
         */
        @Override
        public String value() {
            return this.lines.stream().map(
                line -> line.replaceAll(System.lineSeparator(), " ")
            ).collect(Collectors.joining(" "));
        }

        /**
         * Indent this block scalar. When indenting/printing, we're going to
         * separate the lines.
         * @param indentation Number of preceding spaces of each line.
         * @return Indented Scalar.
         */
        @Override
        String indent(final int indentation) {
            int spaces = indentation;
            StringBuilder print = new StringBuilder();
            StringBuilder alignment = new StringBuilder();
            while (spaces > 0) {
                alignment.append(" ");
                spaces--;
            }
            for(int idx = 0; idx < this.lines.size(); idx++) {
                final String line = this.lines.get(idx);
                if(line.contains(System.lineSeparator())) {
                    final String[] hardcodedNewLines = line.split(
                        System.lineSeparator()
                    );
                    for(final String subline : hardcodedNewLines) {
                        print
                            .append(alignment)
                            .append(subline)
                            .append(System.lineSeparator());
                    }
                } else {
                    print
                        .append(alignment)
                        .append(line);
                    if (idx < this.lines.size() - 1) {
                        print.append(System.lineSeparator());
                    }
                }
            }
            return print.toString();
        }

        /**
         * When printing a block scalar, we have to wrap it
         * inside proper YAML elements, otherwise it won't make
         * sense as a YAML document. It will look like this:
         * <pre>
         * ---
         * >
         *   some folded
         *   scalar on more lines
         * ...
         * </pre>
         * @return This scalar as a YAML document.
         */
        @Override
        public String toString() {
            final StringBuilder string = new StringBuilder();
            string
                .append("---")
                .append(System.lineSeparator())
                .append(">")
                .append(System.lineSeparator())
                .append(this.indent(2))
                .append(System.lineSeparator())
                .append("...");
            return string.toString();
        }
    }

    /**
     * A built literal block Scalar.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 4.0.0
     */
    static class BuiltLiteralBlockScalar extends BaseScalar {

        /**
         * Lines of this scalar.
         */
        private final List<String> lines;

        /**
         * Ctor.
         * @param lines Given string lines.
         */
        BuiltLiteralBlockScalar(final List<String> lines) {
            this.lines = lines;
        }

        /**
         * Return the value of this literal scalar.
         * @return String value.
         */
        @Override
        public String value() {
            return this.lines.stream().collect(
                Collectors.joining(System.lineSeparator())
            );
        }

        /**
         * Indent this block scalar. When indenting/printing, we're going to
         * separate the lines.
         * @param indentation Number of preceding spaces of each line.
         * @return Indented Scalar.
         */
        @Override
        String indent(final int indentation) {
            int spaces = indentation;
            StringBuilder print = new StringBuilder();
            StringBuilder alignment = new StringBuilder();
            while (spaces > 0) {
                alignment.append(" ");
                spaces--;
            }
            for(int idx = 0; idx < this.lines.size(); idx++) {
                final String line = this.lines.get(idx);
                if(line.contains(System.lineSeparator())) {
                    final String[] hardcodedNewLines = line.split(
                        System.lineSeparator()
                    );
                    for(final String subline : hardcodedNewLines) {
                        print
                            .append(alignment)
                            .append(subline)
                            .append(System.lineSeparator());
                    }
                } else {
                    print
                        .append(alignment)
                        .append(line);
                    if (idx < this.lines.size() - 1) {
                        print.append(System.lineSeparator());
                    }
                }
            }
            return print.toString();
        }

        /**
         * When printing a block scalar, we have to wrap it
         * inside proper YAML elements, otherwise it won't make
         * sense as a YAML document. It will look like this:
         * <pre>
         * ---
         * |
         *   line 1
         *   line 2
         *   line 3
         * ...
         * </pre>
         * @return This scalar as a YAML document.
         */
        @Override
        public String toString() {
            final StringBuilder string = new StringBuilder();
            string
                .append("---")
                .append(System.lineSeparator())
                .append("|")
                .append(System.lineSeparator())
                .append(this.indent(2))
                .append(System.lineSeparator())
                .append("...");
            return string.toString();
        }
    }
}
