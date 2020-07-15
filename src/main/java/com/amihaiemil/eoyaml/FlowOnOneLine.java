/**
 * Copyright (c) 2020, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.eoyaml;

/**
 * Some YAML flow lines merged into one.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 5.2.0
 * @todo #368:30min Write unit tests for this class.
 *  Make sure to also cover the edge cases as much as possible.
 */
final class FlowOnOneLine implements YamlLine {

    /**
     * Merged line.
     */
    private final YamlLine merged;

    /**
     * Ctor.
     * @param opening Opening bracket.
     * @param closing Closing bracket.
     * @param lines Lines to fold.
     */
    FlowOnOneLine(
        final FoldedFlowLines.Bracket opening,
        final FoldedFlowLines.Bracket closing,
        final YamlLines lines
    ) {
        final StringBuilder flowLine = new StringBuilder();
        int startIndex = opening.getIndex();
        for(final YamlLine line : lines) {
            int endIndex;
            if(line.number() < closing.getLine().number()) {
                endIndex = line.trimmed().length();
            } else {
                endIndex = closing.getIndex() + 1;
            }
            for (int i = startIndex; i < endIndex; i++) {
                flowLine.append(line.trimmed().charAt(i));
            }
            if(line.number() == closing.getLine().number()) {
                break;
            }
            startIndex = 0;
        }
        this.merged = new RtYamlLine(
            flowLine.toString(),
            opening.getLine().number()
        );
    }

    @Override
    public String trimmed() {
        return this.merged.trimmed();
    }

    @Override
    public String comment() {
        return this.merged.comment();
    }

    @Override
    public int number() {
        return this.merged.number();
    }

    @Override
    public int indentation() {
        return this.merged.indentation();
    }

    @Override
    public boolean requireNestedIndentation() {
        return false;
    }

    @Override
    public int compareTo(final YamlLine other) {
        return this.merged.compareTo(other);
    }

    @Override
    public String toString() {
        return this.merged.toString();
    }
}
