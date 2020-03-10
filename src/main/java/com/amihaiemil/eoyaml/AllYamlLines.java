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

import java.util.Collection;

/**
 * YamlLines default implementation. "All" refers to the fact that
 * we iterate over all of them, irrespective of indentation. There
 * are cases where we need to iterate only over the lines which are
 * at the same indentation level and for that we use the decorator
 * {@link SameIndentationLevel}.
 * @checkstyle ExecutableStatementCount (400 lines)
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
            builder.append(line.toString()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public Collection<YamlLine> lines() {
        return this.lines;
    }
    
    @Override
    public int count() {
        return this.lines.size();
    }

    @Override
    public YamlNode toYamlNode(final YamlLine prev) {
        final String trimmed = prev.trimmed();
        final String last = trimmed.substring(trimmed.length()-1);
        final YamlNode node;
        switch (last) {
            case Nested.YAML:
                final boolean sequence = this.iterator()
                    .next().trimmed().startsWith("-");
                if(sequence) {
                    node = new ReadYamlSequence(this);
                } else {
                    node = new ReadYamlMapping(this);
                }
                break;
            case Nested.KEY_YAML:
                final boolean sequenceKey = this.iterator()
                    .next().trimmed().startsWith("-");
                if(sequenceKey) {
                    node = new ReadYamlSequence(this);
                } else {
                    node = new ReadYamlMapping(this);
                }
                break;
            case Nested.SEQUENCE:
                if(trimmed.length() == 1) {
                    final boolean elementSequence = this.iterator()
                        .next().trimmed().startsWith("-");
                    if(elementSequence) {
                        node = new ReadYamlSequence(this);
                    } else {
                        node = new ReadYamlMapping(this);
                    }
                } else {
                    node = new ReadYamlSequence(this);
                }
                break;
            case Nested.PIPED_SCALAR:
                node = new ReadPipeScalar(this);
                break;
            case Nested.POINTED_SCALAR:
                node = new ReadPointedScalar(this);
                break;
            default:
                node = null;
                break;
        }
        return node;
    }
    
}
