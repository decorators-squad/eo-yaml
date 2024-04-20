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
     * Line separator.
     */
    private final String lineSeparator;

    /**
     * Print all the nodes in block-style.
     */
    private final boolean alwaysBlock;

    /**
     * Constructor.
     * @param writer Destination writer.
     */
    RtYamlPrinter(final Writer writer) {
        this(writer, false);
    }

    /**
     * Constructor.
     * @param writer Destination writer.
     * @param alwaysBlock Print all the nodes in block-style.
     */
    RtYamlPrinter(final Writer writer, final boolean alwaysBlock) {
        this(writer, System.lineSeparator(), alwaysBlock);
    }

    /**
     * Constructor.
     * @param writer Destination writer.
     * @param lineSeparator Line separator.
     */
    RtYamlPrinter(final Writer writer, final String lineSeparator) {
        this(writer, lineSeparator, false);
    }

    /**
     * Constructor.
     * @param writer Destination writer.
     * @param lineSeparator Line separator.
     * @param alwaysBlock Print all the nodes in block-style.
     */
    RtYamlPrinter(
        final Writer writer,
        final String lineSeparator,
        final boolean alwaysBlock
    ) {
        this.writer = writer;
        this.lineSeparator = lineSeparator;
        this.alwaysBlock = alwaysBlock;
    }

    @Override
    public void print(final YamlNode node) throws IOException  {
        try {
            final YamlVisitor<String> visitor = new YamlPrintVisitor(
                this.lineSeparator, this.alwaysBlock
            );
            if (node.type().equals(Node.SCALAR)) {
                this.writer.append("---")
                    .append(this.lineSeparator)
                    .append(printPossibleComment(node))
                    .append(node.accept(visitor))
                    .append(this.lineSeparator)
                    .append("...");
            } else {
                final String comment = printPossibleComment(node);
                this.writer.append(comment);
                if (!comment.isEmpty()) {
                    this.writer.append("---").append(this.lineSeparator);
                }
                this.writer.append(node.accept(visitor));
            }
        } finally {
            this.writer.close();
        }
    }

    /**
     * Print a comment. Make sure to split the lines if there are more
     * lines separated by NewLine and also add a '# ' in front of each
     * line.
     * @param node Node containing the Comment.
     * @return Printed comment.
     */
    private String printPossibleComment(final YamlNode node) {
        final StringWriter wrtr = new StringWriter();
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
                    wrtr
                        .append("# ")
                        .append(line)
                        .append(this.lineSeparator);
                }
            }
        }
        return wrtr.toString();
    }
}
