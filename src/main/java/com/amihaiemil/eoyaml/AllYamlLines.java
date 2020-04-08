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

import com.amihaiemil.eoyaml.exceptions.YamlReadingException;
import java.util.Collection;

/**
 * YamlLines default implementation. "All" refers to the fact that
 * we iterate over all of them, irrespective of indentation. There
 * are cases where we need to iterate only over the lines which are
 * at the same indentation level and for that we use the decorator
 * {@link SameIndentationLevel}.
 * @checkstyle ExecutableStatementCount (400 lines)
 * @checkstyle CyclomaticComplexity (400 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class AllYamlLines implements YamlLines {

    /**
     * Yaml lines.
     */
    private Collection<YamlLine> lines;

    /**
     * Ctor.
     * @param lines Yaml lines collection.
     */
    AllYamlLines(final Collection<YamlLine> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final YamlLine line : this.lines) {
            builder.append(line.toString()).append(System.lineSeparator());
        }
        return builder.toString();
    }

    @Override
    public Collection<YamlLine> original() {
        return this.lines;
    }

    @Override
    public YamlNode toYamlNode(final YamlLine prev) {
        final YamlNode node;
        final String prevLine = prev.trimmed();
        if(prevLine.isEmpty()) {
            node = this.mappingSequenceOrPlainScalar(prev);
        } else {
            final String lastChar = prevLine.substring(prevLine.length() - 1);

            if (lastChar.equals(Follows.LITERAL_BLOCK_SCALAR)) {
                node = new ReadLiteralBlockScalar(prev, this);
            } else if (lastChar.equals(Follows.FOLDED_BLOCK_SCALAR)) {
                node = new ReadFoldedBlockScalar(prev, this);
            } else if (prevLine.matches(Follows.FOLDED_SEQUENCE)) {
                node = new ReadYamlSequence(prev, this);
            } else {
                node = this.mappingSequenceOrPlainScalar(prev);
            }
        }
        return node;
    }

    /**
     * Try to figure out what YAML node (mapping, sequence or scalar) do these
     * lines represent.
     * @param prev YamlLine just previous to the node we're trying to find.
     * @return Found YamlNode.
     */
    private YamlNode mappingSequenceOrPlainScalar(final YamlLine prev) {
        final YamlNode node;
        final YamlLine first = new Skip(
            this,
            line -> line.number() <= prev.number(),
            line -> line.trimmed().startsWith("#"),
            line -> line.trimmed().startsWith("---"),
            line -> line.trimmed().startsWith("..."),
            line -> line.trimmed().startsWith("%"),
            line -> line.trimmed().startsWith("!!")
        ).iterator().next();
        if(first.trimmed().startsWith("-")) {
            node = new ReadYamlSequence(prev, this);
        } else if (first.trimmed().contains(":")){
            node = new ReadYamlMapping(prev, this);
        } else if(this.original().size() == 1) {
            node = new ReadPlainScalar(this, first);
        } else {
            System.out.println("LINE IS: [" + first.trimmed() + "]");
            throw new YamlReadingException(
                "Could not parse YAML starting at line " + (first.number() + 1)
                + " . It should be a sequence (line should start with '-'), "
                + "a mapping (line should contain ':') or it should be a plain "
                + "scalar, but it has " + this.lines.size() + " lines, "
                + "while a plain scalar should be only 1 line!"
            );
        }
        return node;
    }

}
