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
 * YamlLines default implementation. It handles ALL the given lines, doesn't
 * jump over or alter them in any way.
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

    /**
     * Returns an iterator over these Yaml lines.
     * It <b>only</b> iterates over the lines which are at the same
     * level of indentation with the first!
     * @return Iterator over these yaml lines.
     */
    @Override
    public Iterator<YamlLine> iterator() {
        Iterator<YamlLine> iterator = this.lines.iterator();
        if (iterator.hasNext()) {
            final List<YamlLine> sameIndentation = new ArrayList<>();
            final YamlLine first = iterator.next();
            sameIndentation.add(first);
            while (iterator.hasNext()) {
                YamlLine current = iterator.next();
                if(current.indentation() == first.indentation()) {
                    sameIndentation.add(current);
                }
            }
            iterator = sameIndentation.iterator();
        }
        return iterator;
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
    public String indent(final int indentation) {
        final StringBuilder indented = new StringBuilder();
        final Iterator<YamlLine> linesIt = this.lines.iterator();
        if(linesIt.hasNext()) {
            final YamlLine first = linesIt.next();
            if (first.indentation() == indentation) {
                indented.append(first.toString()).append("\n");
                while (linesIt.hasNext()) {
                    indented.append(linesIt.next().toString()).append("\n");
                }
            } else {
                final int offset = indentation - first.indentation();
                for (final YamlLine line : this.lines) {
                    int correct = line.indentation() + offset;
                    while (correct > 0) {
                        indented.append(" ");
                        correct--;
                    }
                    indented.append(line.trimmed()).append("\n");
                }
            }
        }
        return indented.toString();
    }

}
