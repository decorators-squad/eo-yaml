/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
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
package com.amihaiemil.camel;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Ordered YamlLines.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class OrderedYamlLines extends AbstractYamlLines {

    /**
     * Lines to order.
     */
    private AbstractYamlLines unordered;

    /**
     * Ctor.
     * @param unordered Decorated lines.
     */
    OrderedYamlLines(final AbstractYamlLines unordered) {
        this.unordered = unordered;
    }

    /**
     * Iterates over the lines with the same indentation. The lines
     * are ordered.
     * @return Iterator over the ordered lines.
     */
    @Override
    public Iterator<YamlLine> iterator() {
        final Iterator<YamlLine> lines = this.unordered.iterator();
        final List<YamlLine> ordered = new LinkedList<>();
        while (lines.hasNext()) {
            ordered.add(lines.next());
        }
        Collections.sort(ordered);
        return ordered.iterator();
    }

    /**
     * Returns the lines which are nested after the given line. 
     * The lines are not necessarily ordered. If the resulting lines
     * should be ordered (be iterated in order), then they have
     * to be wrapped inside a new OrderedYamlLines.
     * @return AbstractYamlLines
     * @param after The number of the parent line
     */
    @Override
    AbstractYamlLines nested(final int after) {
        return this.unordered.nested(after);
    }

    @Override
    int count() {
        return this.unordered.count();
    }

}
