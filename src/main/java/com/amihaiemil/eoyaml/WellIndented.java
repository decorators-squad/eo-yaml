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

import com.amihaiemil.eoyaml.exceptions.YamlIndentationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * YamlLines decorator which iterates over them and verifies
 * that their indentation is correct.<br><br>
 * Initially, we used to do this validation at line level, right when each
 * line is being read (in {@link RtYamlInput}). However, we decided
 * to do it at YamlLines level, as a decorator, because
 * in some cases, we need to eliminate some of them first (markers,
 * directives etc). <br><br>
 *
 * This class can be used as follows:
 *
 * <pre>
 *
 * YamlLines wellIndented = new SameIndentationLevel(
 *     new WellIndented(lines)
 * );//Iterate over the lines which are at the same indentation level
 * </pre>
 * or
 * <pre>
 *
 * YamlLines wellIndented = new SameIndentationLevel(
 *     new WellIndented(
 *         new NoDirectivesOrMarkers(
 *             lines
 *         )//ignore markers or directives
 *     )
 * );//Iterate over the lines which are at the same indentation level
 * </pre>
 *
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.2
 *
 */
final class WellIndented implements YamlLines {

    /**
     * YamlLines.
     */
    private final YamlLines yamlLines;

    /**
     * Ctor.
     * @param yamlLines The Yaml lines.
     */
    WellIndented(final YamlLines yamlLines) {
        this.yamlLines = yamlLines;
    }

    /**
     * Returns an iterator over these Yaml lines.
     * It will verify that each line is properly indented in relation
     * to the previous one and will complain if the indentation is not
     * correct.
     * @checkstyle LineLength (50 lines)
     * @return Iterator over these yaml lines.
     */
    @Override
    public Iterator<YamlLine> iterator() {
        final Iterator<YamlLine> iterator = this.yamlLines.iterator();
        final List<YamlLine> wellIndented = new ArrayList<>();
        YamlLine previous;
        if(iterator.hasNext()) {
            previous = iterator.next();
            wellIndented.add(previous);
            while(iterator.hasNext()) {
                YamlLine line = iterator.next();
                if(!(previous instanceof YamlLine.NullYamlLine)) {
                    int prevIndent = previous.indentation();
                    int lineIndent = line.indentation();
                    if(previous.requireNestedIndentation()) {
                        if(lineIndent != prevIndent+2) {
                            throw new YamlIndentationException(
                                "Indentation of line " + (line.number() + 1)
                                + " is not ok. It should be greater than the one"
                                + " of line " + (previous.number() + 1)
                                + " by 2 spaces."
                            );
                        }
                    } else {
                        if(!"---".equals(previous.trimmed()) && lineIndent > prevIndent) {
                            throw new YamlIndentationException(
                                "Indentation of line " + (line.number() +1) + " is "
                                + "greater than the one of line "
                                + (previous.number() + 1) + ". "
                                + "It should be less or equal."
                            );
                        }
                    }
                }
                previous = line;
                wellIndented.add(line);
            }
        }
        return wellIndented.iterator();
    }

    @Override
    public Collection<YamlLine> original() {
        return this.yamlLines.original();
    }

    @Override
    public YamlNode toYamlNode(final YamlLine prev) {
        return this.yamlLines.toYamlNode(prev);
    }

}
