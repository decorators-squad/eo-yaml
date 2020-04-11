/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
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

import java.io.IOException;
import java.io.Writer;

/**
 * Base implementation of YamlPrinter. "Rt" stands for "Runtime".
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.3.1
 */
final class RtYamlPrinter implements YamlPrinter {

    /**
     * Writer where the given YAML will be printed.
     */
    private final Writer writer;

    /**
     * Constructor.
     * @param writer Destination writer.
     */
    RtYamlPrinter(final Writer writer) {
        this.writer = writer;
    }

    @Override
    public void print(final YamlNode node) throws IOException  {
        if(node instanceof Scalar) {
            this.writer.append("---").append(System.lineSeparator());
            this.printScalar((Scalar) node, 0);
            this.writer.append("...");
        }
    }

    /**
     * Print a YAML Scalar.
     * @param scalar Given Scalar.
     * @param indentation Level of indentation of the printed Scalar.
     * @throws IOException If an I/O problem occurs.
     */
    private void printScalar(
        final Scalar scalar,
        final int indentation
    ) throws IOException {
        if (scalar instanceof PlainStringScalar
            || scalar instanceof ReadPlainScalar
        ) {
            this.writer.append(this.indent(scalar.value(), indentation))
                  .append(System.lineSeparator());
        } else if (scalar instanceof BaseFoldedScalar) {
            final BaseFoldedScalar foldedScalar = (BaseFoldedScalar) scalar;
            this.writer
                .append(">")
                .append(System.lineSeparator());
            for(final String line : foldedScalar.unfolded()) {
                this.writer
                    .append(this.indent(line, indentation + 2))
                    .append(System.lineSeparator());
            }
        } else if (scalar instanceof RtYamlScalarBuilder.BuiltLiteralBlockScalar
            || scalar instanceof ReadLiteralBlockScalar
        ) {
            this.writer
                .append("|")
                .append(System.lineSeparator())
                .append(
                    this.indent(scalar.value(), indentation + 2)
                ).append(System.lineSeparator());
        }
    }

    /**
     * Indent a given String value. If the String has multiple lines,
     * they will all be indented together.
     * @param value String to indent.
     * @param indentation Indentation level.
     * @return Indented String.
     */
    private String indent(final String value, final int indentation) {
        StringBuilder alignment = new StringBuilder();
        int spaces = indentation;
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        String[] lines = value.split(System.lineSeparator());
        StringBuilder printed = new StringBuilder();
        for(final String line: lines) {
            printed.append(alignment);
            printed.append(line);
            printed.append(System.lineSeparator());
        }
        printed.delete(printed.length()-1, printed.length());
        return printed.toString();
    }
}
