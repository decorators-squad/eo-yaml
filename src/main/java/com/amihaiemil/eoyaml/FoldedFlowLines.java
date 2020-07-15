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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A flow YAML node which may span multiple lines.
 * The iterator will fold all the lines into a single line.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 5.2.0
 */
final class FoldedFlowLines implements YamlLines {

    /**
     * The flow's opening bracket.
     */
    private final char openingBracket;

    /**
     * The flow's closing bracket.
     */
    private final char closingBracket;

    /**
     * Lines to fold.
     */
    private final YamlLines lines;

    /**
     * Ctor.
     * @param lines Lines to fold.
     * @param openingBracket Opening.
     * @param closingBracket Closing.
     */
    FoldedFlowLines(
        final YamlLines lines,
        final char openingBracket,
        final char closingBracket
    ) {
        this.lines = lines;
        this.openingBracket = openingBracket;
        this.closingBracket = closingBracket;
    }

    @Override
    public Collection<YamlLine> original() {
        return this.lines.original();
    }

    @Override
    public YamlNode toYamlNode(
        final YamlLine prev,
        final boolean guessIndentation
    ) {
        return this.lines.toYamlNode(prev, guessIndentation);
    }

    @Override
    public Iterator<YamlLine> iterator() {
        final YamlLine first = this.lines.iterator().next();
        final int openBracketIndex = first
            .trimmed().indexOf(this.openingBracket);
        if(openBracketIndex == -1) {
            throw new IllegalStateException(
                "Expected opening "
              + this.openingBracket
              + " on line " + (first.number() + 1)
            );
        }
        final Bracket closing = this.findClosingBracketIndex(this.lines);
        if(closing.getIndex() == -1) {
            throw new IllegalStateException(
                "Did not find the closing bracket for opening bracket "
              + "on line " + (first.number() + 1)
            );
        }
        final List<YamlLine> one = new ArrayList<>();
        one.add(
            new FlowOnOneLine(
                new Bracket(openBracketIndex, first),
                closing,
                this.lines
            )
        );
        return one.iterator();
    }

    /**
     * Find the closing bracket within the given YamlLines.
     * @param all Yaml lines.
     * @return ClosingBracket.
     */
    private Bracket findClosingBracketIndex(final YamlLines all) {

        final YamlLine first = all.iterator().next();
        int index = first.trimmed().indexOf(this.openingBracket);
        int bracketCount = 1;

        final Bracket closing = new Bracket(index, first);

        for(final YamlLine line : all) {
            String text = line.trimmed();
            for (int i = index + 1; i < text.length(); i++) {
                if (text.charAt(i) == this.openingBracket) {
                    bracketCount++;
                } else if (text.charAt(i) == this.closingBracket) {
                    bracketCount--;
                }

                if (bracketCount == 0) {
                    index = i;
                    closing.setLine(line);
                    break;
                }
            }
            if(bracketCount == 0) {
                break;
            }
            index = -1;
        }

        closing.setIndex(index);
        return closing;
    }

    /**
     * Index and line of a bracket.
     * @checkstyle JavadocMethod (100 lines)
     */
    static class Bracket {

        /**
         * Line where the bracket is.
         */
        private YamlLine foundLine;

        /**
         * Index of the bracket.
         */
        private int idx;

        /**
         * Ctor.
         * @param index Index.
         * @param line Line.
         */
        Bracket(final int index, final YamlLine line) {
            this.foundLine = line;
            this.idx = index;
        }

        public YamlLine getLine() {
            return this.foundLine;
        }

        public void setLine(final YamlLine line) {
            this.foundLine = line;
        }

        public int getIndex() {
            return this.idx;
        }

        public void setIndex(final int index) {
            this.idx = index;
        }
    }
}
