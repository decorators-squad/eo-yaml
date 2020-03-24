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
        return new BuiltBlockScalar(this.lines, Boolean.TRUE);
    }

    @Override
    public Scalar buildLiteralBlockScalar() {
        return new BuiltBlockScalar(this.lines, Boolean.FALSE);
    }

    /**
     * A built block Scalar. It can be folded or literal.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 4.0.0
     */
    static class BuiltBlockScalar extends ComparableScalar {

        /**
         * Lines of this scalar.
         */
        private final List<String> lines;

        /**
         * Folded or not (literal)?
         */
        private final boolean folded;

        /**
         * Ctor.
         * @param lines Given string lines.
         * @param folded Folded or not (literal).
         */
        BuiltBlockScalar(final List<String> lines, final boolean folded) {
            this.lines = lines;
            this.folded = folded;
        }

        @Override
        public String value() {
            final String value;
            if(this.folded) {
                value = this.lines.stream().map(
                    line -> line.replaceAll(System.lineSeparator(), " ")
                ).collect(Collectors.joining(" "));
            } else {
                value = this.lines.stream().collect(
                    Collectors.joining(System.lineSeparator())
                );
            }
            return value;
        }

        /**
         * Indent this block scalar. When indenting/printing, we're going to
         * separate the lines.
         * @param indentation Number of preceding spaces of each line.
         * @return Indented Scalar.
         */
        public String indent(final int indentation) {
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
    }
}
