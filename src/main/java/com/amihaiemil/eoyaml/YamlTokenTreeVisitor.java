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

import java.io.StringWriter;

/**
 * Print the tree of tokens in the YAML. Useful for debugging purposes.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 8.0.1
 */
final class YamlTokenTreeVisitor implements YamlVisitor<String> {

    /**
     * How many indentation spaces to use?
     */
    private final int indentation;

    /**
     * Line separator.
     */
    private final String lineSeparator;

    /**
     * Ctor.
     * @param lineSeparator Line separator.
     */
    YamlTokenTreeVisitor(final String lineSeparator) {
        this(2, lineSeparator);
    }

    /**
     * Ctor.
     * @param indentation Number of spaces to use for indentation.
     * @param lineSeparator Line separator.
     */
    YamlTokenTreeVisitor(
        final int indentation,
        final String lineSeparator
    ) {
        this.indentation = indentation;
        this.lineSeparator = lineSeparator;
    }

    @Override
    public String visitYamlNode(final YamlNode node) {
        final StringWriter writer = new StringWriter();
        if (node instanceof Scalar) {
            writer.append(this.visitScalar((Scalar) node));
        } else if (node instanceof YamlSequence) {
            writer.append(this.visitYamlSequence((YamlSequence) node));
        } else if (node instanceof YamlMapping) {
            writer.append(this.visitYamlMapping((YamlMapping) node));
        } else if (node instanceof YamlStream) {
            writer.append(this.visitYamlStream((YamlStream) node));
        }
        return writer.toString();
    }

    @Override
    public String visitYamlMapping(final YamlMapping node) {
        final StringWriter writer = new StringWriter();
        final String symbol;
        if((node instanceof ReadFlowMapping
            || node instanceof JsonYamlMapping)) {
            symbol = "+MAP {}";
        } else {
            symbol = "+MAP";
        }
        writer.append(symbol + this.lineSeparator);
        writer.append(indent(this.visitChildren(node), this.indentation));
        writer.append("-MAP" + this.lineSeparator);
        return writer.toString();
    }

    @Override
    public String visitYamlSequence(final YamlSequence node) {
        final StringWriter writer = new StringWriter();
        final String symbol;
        if((node instanceof ReadFlowSequence
            || node instanceof JsonYamlSequence)) {
            symbol = "+SEQ []";
        } else {
            symbol = "+SEQ";
        }
        writer.append(symbol + this.lineSeparator);
        writer.append(indent(this.visitChildren(node), this.indentation));
        writer.append("-SEQ" + this.lineSeparator);
        return writer.toString();
    }

    @Override
    public String visitScalar(final Scalar node) {
        return "=VAL " + node.value() + this.lineSeparator;
    }

    @Override
    public String visitYamlStream(final YamlStream node) {
        final StringWriter writer = new StringWriter();
        node.forEach(
            doc -> {
                writer.append("+DOC" + this.lineSeparator);
                writer.append(indent(this.visitYamlNode(doc), this.indentation));
                writer.append("-DOC" + this.lineSeparator);
            }
        );
        return writer.toString();
    }


    @Override
    public String defaultResult() {
        return "";
    }

    @Override
    public String aggregateResult(
        final String aggregate, final String nextResult
    ) {
        return aggregate + nextResult;
    }

    /**
     * Indent a text with a number of spaces.
     * @param print Text to indent.
     * @param numberOfSpaces Number of indentation spaces.
     * @return Indented text.
     */
    private String indent(final String print, final int numberOfSpaces) {
        int spaces = numberOfSpaces;
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
        return indented.toString();
    }
}
