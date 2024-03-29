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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Returns an iterator which collapses the flow-style lines into one single
 * line.<br>
 * <pre>
 * //collapse the lines of flow-style sequeces
 * final YamlLines collapsed = new CollapsedFlowLines(
 *     new AllYamlLines(lines), '[', ']'
 * );
 * </pre>
 * Initial lines:<br>
 * <pre>
 * ---
 * name: eo-yaml
 * developers: [
 *   amihaiemil,
 *   sherif,
 *   rultor]
 * architect: amihaiemil
 * </pre>
 * Lines as iterated by this class:<br>
 * <pre>
 * ---
 * name: eo-yaml
 * developers: [amihaiemil, sherif, rultor]
 * architect: amihaiemil
 * </pre>
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @checkstyle ExecutableStatementCount (1000 lines)
 * @checkstyle CyclomaticComplexity (1000 lines)
 * @since 8.0.0
 */
final class CollapsedFlowLines implements YamlLines {
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
    CollapsedFlowLines(
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
    public YamlNode nextYamlNode(final YamlLine prev) {
        return this.lines.nextYamlNode(prev);
    }

    @Override
    public Iterator<YamlLine> iterator() {
        int bracketCount = 0;
        boolean closedBracketFound = false;
        final List<YamlLine> collapsed = new ArrayList<>();
        List<YamlLine> flow = new ArrayList<>();
        boolean startedQuoteEscape = false;
        boolean startedApEscape = false;
        for(final YamlLine line : this.lines) {
            String text = line.trimmed();
            for (int i = 0; i < text.length(); i++) {
                final char currentChar = text.charAt(i);
                if(currentChar == this.openingBracket
                    && !startedApEscape && !startedQuoteEscape) {
                    bracketCount++;
                } else if(currentChar == this.closingBracket
                    && !startedApEscape && !startedQuoteEscape) {
                    bracketCount--;
                    closedBracketFound = true;
                }
                if (isEscapeChar(i, '\"', text) && !startedApEscape) {
                    startedQuoteEscape = !startedQuoteEscape;
                } else if (isEscapeChar(i, '\'', text) && !startedQuoteEscape) {
                    startedApEscape = !startedApEscape;
                }
            }
            if(bracketCount % 2 != 0) {
                flow.add(line);
            } else if(bracketCount == 0 && closedBracketFound) {
                flow.add(line);
                collapsed.add(new CollapsedYamlLine(flow));
                flow = new ArrayList<>();
                closedBracketFound = false;
            } else {
                collapsed.add(line);
            }
        }
        return collapsed.iterator();
    }

    /**
     * Does the string contain an escape chartacter at the specified index?
     * @param start Index.
     * @param escapeChar Escape character to look for.
     * @param nodes String containing the characters.
     * @return True if the escape character is found and it is
     *  not preceded by a backslash.
     */
    private boolean isEscapeChar(
        final int start, final char escapeChar, final String nodes
    ) {
        final boolean result;
        if (nodes.charAt(start) == escapeChar) {
            result = start == 0 || nodes.charAt(start - 1) != '\\';
        } else {
            result = false;
        }
        return result;
    }
}
