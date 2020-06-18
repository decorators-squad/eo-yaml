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
 * Iterate over the lines which are YAML comments (begin with "#") and break
 * iteration when a non-comment line is found. In essence, this reads the lines
 * of the first comment from a given YamlLines, if it exists.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
final class FirstCommentFound implements YamlLines {

    /**
     * Lines where we look for the comment.
     */
    private final YamlLines lines;

    /**
     * Are we looking for inline comments?
     * E.g. Comments after a scalar:
     * <pre>
     *   some: scalar # this comment
     * </pre>
     */
    private final boolean inLine;

    /**
     * Ctor.
     * @param lines The Yaml lines where we look for the comment.
     */
    FirstCommentFound(final YamlLines lines) {
        this(lines, false);
    }

    /**
     * Ctor.
     * @param lines The Yaml lines where we look for the comment.
     * @param inLine Looking for inline comment or not?
     */
    FirstCommentFound(final YamlLines lines, final boolean inLine) {
        this.lines = lines;
        this.inLine = inLine;
    }

    /**
     * Returns an iterator over the lines of the first found comment.
     * @return Iterator over these yaml lines.
     */
    @Override
    public Iterator<YamlLine> iterator() {
        Iterator<YamlLine> iterator = this.lines.iterator();
        if (iterator.hasNext()) {
            final List<YamlLine> comment = new ArrayList<>();
            while (iterator.hasNext()) {
                YamlLine line = iterator.next();
                if(!line.comment().isEmpty()) {
                    if(line.trimmed().startsWith("#")) {
                        comment.add(line);
                    } else if(this.inLine) {
                        comment.add(line);
                    }
                } else {
                    break;
                }
            }
            iterator = comment.iterator();
        }
        return iterator;
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
}
