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

/**
 * Read Yaml folded block Scalar. This is a Scalar spanning multiple lines.
 * This Scalar's newlines will be ignored ("folded"), the scalar's value
 * is a single line of text split in multiple lines for readability.<br><br>
 * Example of Folded Block Scalar:
 * <pre>
 *   folded block scalar: >
 *     a long line split into
 *     several short
 *     lines for readability
 * </pre>
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.2
 */
final class ReadFoldedBlockScalar extends BaseScalar {

    /**
     * Lines to be represented as a wrapped scalar.
     */
    private YamlLines lines;

    /**
     * Ctor.
     * @param lines Given lines to represent.
     */
    ReadFoldedBlockScalar(final YamlLines lines) {
        this.lines = new Skip(
            lines,
            line -> line.trimmed().endsWith(">"),
            line -> line.trimmed().endsWith("|"),
            line -> "---".equals(line.trimmed()),
            line -> "...".equals(line.trimmed()),
            line -> line.trimmed().startsWith("%"),
            line -> line.trimmed().startsWith("!!")
        );
    }

    @Override
    String indent(final int indentation) {
        StringBuilder alignment = new StringBuilder();
        int spaces = indentation;
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        StringBuilder printed = new StringBuilder();
        for(final YamlLine line: this.lines) {
            printed.append(alignment);
            printed.append(line.trimmed());
            printed.append(System.lineSeparator());
        }
        printed.delete(printed.length()-1, printed.length());
        return printed.toString();
    }

    /**
     * Checks whether StringBuilder do not end with newline or not.
     * @param builder StringBuilder
     * @return Boolean Whether builder do not end with newline char or not
     */
    private boolean doNotEndWithNewLine(final StringBuilder builder) {
        return builder.length() > 0
                && !builder.toString().endsWith(System.lineSeparator());
    }
    /**
     * Value of this scalar.
     * @return String
     */
    public String value() {
        StringBuilder builder = new StringBuilder();
        final String newLine = System.lineSeparator();
        for(final YamlLine line: this.lines) {
            if(line.trimmed().length() == 0 || line.indentation() > 0) {
                if(this.doNotEndWithNewLine(builder)) {
                    builder.append(newLine);
                }
                int indentation = line.indentation();
                for(int i = 0; i < indentation; i++) {
                    builder.append(' ');
                }
                builder.append(line.trimmed());
                builder.append(newLine);
            } else {
                if(this.doNotEndWithNewLine(builder)) {
                    builder.append(' ');
                }
                builder.append(line.trimmed());
            }
        }
        return builder.toString();
    }

    /**
     * When printing a folded scalar, we have to wrap it
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
        string.append("---").append(System.lineSeparator())
            .append(">").append(System.lineSeparator())
            .append(this.indent(2))
            .append(System.lineSeparator())
            .append("...");
        return string.toString();
    }
}
