/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
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
import java.io.StringWriter;
import java.util.List;

/**
 * Visitor which prints the YAML in block format.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 7.2.0
 */
//TODO finish printing with indentation logic. Also print comments afterwards.
public class YamlPrintVisitor implements YamlVisitor<String>{

    /**
     * How many indentation spaces to use?
     */
    private final int indentation;

    /**
     * Line separator.
     */
    private final String lineSeparator;

    /**
     * Ctor. Defaults to indentation 2 and the System line separator.
     */
    public YamlPrintVisitor() {
        this(2, System.lineSeparator());
    }

    /**
     * Ctor.
     * @param indentation Number of indentation spaces.
     */
    public YamlPrintVisitor(final int indentation) {
        this(indentation, System.lineSeparator());
    }

    /**
     * Ctor.
     * @param lineSeparator Line separator.
     */
    public YamlPrintVisitor(final String lineSeparator) {
        this(2, lineSeparator);
    }

    /**
     * Ctor.
     * @param indentation Number of spaces to use for indentation.
     * @param lineSeparator Line separator.
     */
    public YamlPrintVisitor(final int indentation, final String lineSeparator) {
        this.indentation = indentation;
        this.lineSeparator = lineSeparator;
    }
    @Override
    public String visitYamlMapping(final YamlMapping node) {
        final StringWriter writer = new StringWriter();
        writer.append(this.printPossibleComment(node));
        for (final YamlNode key : node.keys()) {
            final YamlNode value = node.value(key);
            writer.append(this.printPossibleComment(value));
            if(key.type().equals(Node.SCALAR)) {
                writer.append(this.visitYamlNode(key));
            } else {
                writer.append("?").append(this.lineSeparator);
                final String printedValue = this.visitYamlNode(key);
                writer.append(this.indent(printedValue, this.indentation));
                writer.append(this.lineSeparator);
            }
            writer.append(": ");
            if(value.type().equals(Node.SCALAR)) {
                writer.append(this.visitYamlNode(value));
            } else {
                writer.append(this.lineSeparator);
                final String printedValue = this.visitYamlNode(value);
                writer.append(this.indent(printedValue, this.indentation));
            }
            writer.append(this.lineSeparator);
        }
        final String printedMapping = writer.toString();
        return printedMapping.substring(0, printedMapping.length() - 1);
    }

    @Override
    public String visitYamlSequence(final YamlSequence node) {
        final StringWriter writer = new StringWriter();
        for(final YamlNode value : node.values()) {
            writer.append(this.printPossibleComment(value));
            writer.append("- ");
            if(value.type().equals(Node.SCALAR)) {
                writer.append(this.visitYamlNode(value));
            } else {
                writer.append(this.lineSeparator);
                final String printedValue = this.visitYamlNode(value);
                writer.append(this.indent(printedValue, this.indentation));
            }
            writer.append(this.lineSeparator);
        }
        final String printedSequence = writer.toString();
        return printedSequence.substring(0, printedSequence.length() - 1);
    }

    @Override
    public String visitScalar(final Scalar node) {
        final StringWriter writer = new StringWriter();
        if (node instanceof BaseFoldedScalar) {
            final BaseFoldedScalar foldedScalar = (BaseFoldedScalar) node;
            writer.append(">");
            if(!node.comment().value().isEmpty()) {
                writer.append(" # ").append(node.comment().value());
            }
            writer.append(this.lineSeparator);
            final List<String> unfolded = foldedScalar.unfolded();
            for(int idx = 0; idx < unfolded.size(); idx++) {
                writer.append(
                    this.indent(
                        unfolded.get(idx).trim(),
                        indentation
                    )
                );
                if(idx < unfolded.size() - 1) {
                    writer.append(this.lineSeparator);
                }
            }
        } else if (node instanceof RtYamlScalarBuilder.BuiltLiteralBlockScalar
            || node instanceof ReadLiteralBlockScalar
        ) {
            writer.append("|");
            if(!node.comment().value().isEmpty()) {
                writer.append(" # ").append(node.comment().value());
            }
            writer
                .append(this.lineSeparator)
                .append(
                    this.indent(node.value(), indentation)
                );
        } else {
            writer.append(new Escaped(node).value());
            final Comment comment = node.comment();
            if (comment instanceof ScalarComment) {
                final ScalarComment scalarComment = (ScalarComment) comment;
                if (!scalarComment.inline().value().isEmpty()) {
                    writer.append(" # ").append(
                        scalarComment.inline().value()
                    );
                }
            }
        }
        return writer.toString();
    }

    @Override
    public String visitYamlStream(final YamlStream node) {
        final StringWriter writer = new StringWriter();
        node.forEach(
            yaml ->
                writer
                    .append("---")
                    .append(this.lineSeparator)
                    .append(this.indent(this.visitYamlNode(yaml), this.indentation))
                    .append(this.lineSeparator)
        );
        final String printedStream = writer.toString();
        return printedStream.substring(0, printedStream.length() - 1);
    }

    public String visitYamlNode(final YamlNode node) {
        final StringWriter writer = new StringWriter();
        if (node == null || node.isEmpty()) {
            if (node instanceof EmptyYamlSequence) {
                writer.append("[]");
            } else if (node instanceof EmptyYamlMapping) {
                writer.append("{}");
            } else {
                writer.append("null");
            }
        } else {
            if (node instanceof Scalar) {
                writer.append(this.visitScalar((Scalar) node));
            } else if (node instanceof YamlSequence) {
                writer.append(this.visitYamlSequence((YamlSequence) node));
            } else if (node instanceof YamlMapping) {
                writer.append(this.visitYamlMapping((YamlMapping) node));
            } else if (node instanceof YamlStream) {
                writer.append(this.visitYamlStream((YamlStream) node));
            }
        }
        return writer.toString();
    }

    @Override
    public String defaultResult() {
        return "";
    }

    @Override
    public String aggregateResult(String aggregate, String nextResult) {
        return aggregate + nextResult;
    }

    private String indent(final String print, int indentation) {
        int spaces = indentation;
        final StringBuilder indent = new StringBuilder();
        while (spaces > 0) {
            indent.append(" ");
            spaces--;
        }
        final String[] lines = print.split(this.lineSeparator);
        final StringWriter indented = new StringWriter();
        for(final String line : lines) {
            indented
                .append(indent.toString())
                .append(line)
                .append(this.lineSeparator);
        }
        final String str = indented.toString();
        return str.substring(0, str.length() - 1);
    }

    /**
     * Print a comment. Make sure to split the lines if there are more
     * lines separated by NewLine and also add a '# ' in front of each
     * line.
     * @param node Node containing the Comment.
     * @return Printed comment.
     * @throws IOException If any I/O problem occurs.
     */
    private String printPossibleComment(final YamlNode node) {
        final StringWriter writer = new StringWriter();
        if(node != null && node.comment() != null) {
            final Comment tmpComment;
            if(node.comment() instanceof ScalarComment) {
                tmpComment = ((ScalarComment) node.comment()).above();
            } else {
                tmpComment = node.comment();
            }
            final String com = tmpComment.value();
            if (com.trim().length() != 0) {
                String[] lines = com.split(this.lineSeparator);
                for (final String line : lines) {
                    writer
                        .append("# ")
                        .append(line)
                        .append(this.lineSeparator);
                }
            }
        }
        return writer.toString();
    }

    /**
     * A scalar which escapes its value.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 7.2.0
     * @checkstyle LineLength (100 lines)
     */
    static class Escaped extends BaseScalar {

        /**
         * Original unescaped scalar.
         */
        private final Scalar original;

        /**
         * Ctor.
         * @param original Unescaped scalar.
         */
        Escaped(final Scalar original) {
            this.original = original;
        }

        @Override
        public String value() {
            final String value = this.original.value();
            String toEscape;
            if(value == null) {
                toEscape = "null";
            } else {
                toEscape = value;
            }
            boolean alreadyEscaped = (toEscape.startsWith("'") && toEscape.endsWith("'"))
                || (toEscape.startsWith("\"") && toEscape.endsWith("\""));
            final String needsEscaping = ".*[?\\-#:>|$%&{}\\[\\]@`!*,'\"]+.*|[ ]+|null";
            if (!alreadyEscaped && toEscape.matches(needsEscaping)) {
                if(toEscape.contains("\"")) {
                    toEscape = "'" + toEscape + "'";
                } else {
                    toEscape = "\"" + toEscape + "\"";
                }
            }
            return toEscape;
        }

        @Override
        public Comment comment() {
            return this.original.comment();
        }
    }
}
