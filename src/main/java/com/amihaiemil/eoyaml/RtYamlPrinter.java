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
import java.util.Iterator;
import java.util.List;

/**
 * Base implementation of YamlPrinter. "Rt" stands for "Runtime".
 * @checkstyle ExecutableStatementCount (400 lines)
 * @checkstyle CyclomaticComplexity (400 lines)
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
            this.writer.append(System.lineSeparator()).append("...");
        } else if(node instanceof YamlSequence) {
            this.printPossibleComment(node.comment(), "");
            this.printSequence((YamlSequence) node, 0);
        } else if(node instanceof YamlMapping) {
            this.printPossibleComment(node.comment(), "");
            this.printMapping((YamlMapping) node, 0);
        } else if(node instanceof YamlStream) {
            this.printStream((YamlStream) node, 0);
        }
    }

    /**
     * Print a YAML Stream of documents.
     * @param stream Given YamlStream.
     * @param indentation Level of indentation of the printed stream.
     * @throws IOException If an I/O problem occurs.
     */
    private void printStream(
        final YamlStream stream,
        final int indentation
    ) throws IOException {
        final String newLine = System.lineSeparator();
        int spaces = indentation;
        final StringBuilder indent = new StringBuilder();
        while (spaces > 0) {
            indent.append(" ");
            spaces--;
        }
        final Iterator<YamlNode> valuesIt = stream.values().iterator();
        while(valuesIt.hasNext()) {
            final YamlNode document = valuesIt.next();
            this.writer
                .append(indent)
                .append("---");
            this.printNode(document, true, indentation + 2);
            if(valuesIt.hasNext()) {
                this.writer.append(newLine);
            }
        }
    }

    /**
     * Print a YAML Mapping.
     * @param mapping Given YamlMapping.
     * @param indentation Level of indentation of the printed mapping.
     * @throws IOException If an I/O problem occurs.
     */
    private void printMapping(
        final YamlMapping mapping,
        final int indentation
    ) throws IOException {
        final String newLine = System.lineSeparator();
        int spaces = indentation;
        final StringBuilder alignment = new StringBuilder();
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        final Iterator<YamlNode> keysIt = mapping.keys().iterator();
        while(keysIt.hasNext()) {
            final YamlNode key = keysIt.next();
            this.printPossibleComment(
                key.comment(), alignment.toString()
            );
            this.writer.append(alignment);
            final YamlNode value = mapping.value(key);
            if(!(value instanceof Scalar)) {
                this.printPossibleComment(
                    value.comment(), alignment.toString()
                );
            }
            if(key instanceof Scalar) {
                this.printNode(key, false, 0);
                this.writer
                    .append(":");
            } else {
                this.writer
                    .append("?");
                this.printNode(key, true, indentation + 2);
                this.writer.append(newLine)
                    .append(alignment)
                    .append(":");
            }
            if (value instanceof Scalar) {
                this.writer.append(" ");
                this.printNode(value, false, 0);
            } else  {
                this.printNode(value, true, indentation + 2);
            }
            if(keysIt.hasNext()) {
                this.writer.append(newLine);
            }
        }
    }

    /**
     * Print a YAML Sequence.
     * @param sequence Given YamlSequence.
     * @param indentation Level of indentation of the printed Scalar.
     * @throws IOException If an I/O problem occurs.
     */
    private void printSequence(
        final YamlSequence sequence,
        final int indentation
    ) throws IOException {
        final String newLine = System.lineSeparator();
        int spaces = indentation;
        final StringBuilder alignment = new StringBuilder();
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        final Iterator<YamlNode> valuesIt = sequence.values().iterator();
        while(valuesIt.hasNext()) {
            final YamlNode node = valuesIt.next();
            if (node instanceof Scalar) {
                this.writer
                    .append(alignment)
                    .append("- ");
                this.printNode(node, false, 0);
            } else  {
                this.printPossibleComment(node.comment(), alignment.toString());
                this.writer
                    .append(alignment)
                    .append("-");
                this.printNode(node, true, indentation + 2);
            }
            if(valuesIt.hasNext()) {
                this.writer.append(newLine);
            }
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
            this.writer.append(this.indent(scalar.value(), indentation));
            if(!scalar.comment().value().isEmpty()) {
                this.writer.append(" # ").append(scalar.comment().value());
            }
        } else if (scalar instanceof BaseFoldedScalar) {
            final BaseFoldedScalar foldedScalar = (BaseFoldedScalar) scalar;
            this.writer
                .append(">");
            if(!scalar.comment().value().isEmpty()) {
                this.writer.append(" # ").append(scalar.comment().value());
            }
            this.writer.append(System.lineSeparator());
            final List<String> unfolded = foldedScalar.unfolded();
            for(int idx = 0; idx < unfolded.size(); idx++) {
                this.writer.append(
                    this.indent(
                        unfolded.get(idx).trim(),
                        indentation + 2
                    )
                );
                if(idx < unfolded.size() - 1) {
                    this.writer.append(System.lineSeparator());
                }
            }
        } else if (scalar instanceof RtYamlScalarBuilder.BuiltLiteralBlockScalar
            || scalar instanceof ReadLiteralBlockScalar
        ) {
            this.writer
                .append("|");
            if(!scalar.comment().value().isEmpty()) {
                this.writer.append(" # ").append(scalar.comment().value());
            }
            this.writer
                .append(System.lineSeparator())
                .append(
                    this.indent(scalar.value(), indentation + 2)
                );
        }
    }

    /**
     * This method should be used when printing children
     * nodes of a complex Node (mapping, scalar, stream etc).
     * @param node YAML Node to print.
     * @param onNewLine Should the child node be printed on a new line?
     * @param indentation Indentation of the print.
     * @throws IOException If any I/O error occurs.
     */
    private void printNode(
        final YamlNode node,
        final boolean onNewLine,
        final int indentation
    ) throws IOException {
        if(onNewLine) {
            this.writer.append(System.lineSeparator());
        }
        if(node instanceof Scalar) {
            this.printScalar((Scalar) node, indentation);
        } else if (node instanceof YamlSequence) {
            this.printSequence((YamlSequence) node, indentation);
        } else if (node instanceof YamlMapping) {
            this.printMapping((YamlMapping) node, indentation);
        } else if (node instanceof YamlStream) {
            this.printStream((YamlStream) node, indentation);
        }
    }

    /**
     * Print a comment. Make sure to split the lines if there are more
     * lines separated by NewLine and also add a '# ' in front of each
     * line.
     * @param comment Comment.
     * @param alignment Indentation.
     * @throws IOException If any I/O problem occurs.
     */
    private void printPossibleComment(
        final Comment comment,
        final String alignment
    ) throws IOException {
        final String com = comment.value();
        if(com.trim().length()!=0) {
            String[] lines = com.split(System.lineSeparator());
            for(final String line : lines) {
                this.writer
                    .append(alignment)
                    .append("# ")
                    .append(line)
                    .append(System.lineSeparator());
            }
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
