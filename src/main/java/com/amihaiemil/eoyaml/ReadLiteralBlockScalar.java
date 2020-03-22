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
 * Read Yaml literal block Scalar. This is a Scalar spanning multiple lines.
 * This Scalar's lines will be treated as separate lines and won't be folded
 * into a single line. Example of Literal Block Scalar:
 * <pre>
 *   literal block scalar: |
 *     a multiline text
 *     line two of the scalar
 *     line three of the scalar
 * </pre>
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.2
 *
 */
final class ReadLiteralBlockScalar extends ComparableScalar {

    /**
     * Lines to be represented as a wrapped scalar.
     */
    private YamlLines lines;

    /**
     * Ctor.
     * @param lines Given lines to represent.
     */
    ReadLiteralBlockScalar(final YamlLines lines) {
        this.lines = lines;
    }

    @Override
    public String indent(final int indentation) {
        StringBuilder printed = new StringBuilder();
        for(final YamlLine line: this.lines) {
            int spaces = indentation;
            while (spaces > 0) {
                printed.append(" ");
                spaces--;
            }
            printed.append(line.trimmed());
            printed.append(System.lineSeparator());
        }
        printed.delete(printed.length()-1, printed.length());
        return printed.toString();
    }

    /**
     * Value of this scalar.
     * @return String
     */
    public String value() {
        StringBuilder builder = new StringBuilder();
        for(final YamlLine line: this.lines) {
            builder.append(line.trimmed());
            builder.append(System.lineSeparator());
        }
        builder.delete(builder.length()-1, builder.length());
        return builder.toString();
    }

}
