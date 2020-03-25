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
import java.util.Iterator;

/**
 * Iterable yaml lines.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
interface YamlLines extends Iterable<YamlLine> {

    /**
     * All the YAML lines, as a collection.<br>
     * This method should always return all the lines
     * there are, irrespective of iteration logic!
     * @return These YamlLines as a Collection.
     */
    Collection<YamlLine> lines();

    /**
     * Turn these lines into a YamlNode.
     * @param prev Previous YamlLine
     * @return YamlNode
     */
    YamlNode toYamlNode(final YamlLine prev);

    /**
     * Default iterator which doesn't skip any line,
     * iterates over all of them.
     * @return Iterator of YamlLine.
     */
    default Iterator<YamlLine> iterator() {
        return this.lines().iterator();
    }

    /**
     * Lines which are nested after the given YamlLine.
     * @param after Number of a YamlLine
     * @return YamlLines
     */
    AllYamlLines nested(final int after);

    /**
     * Get a certain YamlLine.
     * @param number Number of the line.
     * @return YamlLine or throws {@link IndexOutOfBoundsException}.
     */
    default YamlLine line(final int number) {
        final Collection<YamlLine> lines = this.lines();
        int index = 0;
        for(final YamlLine line : lines){
            if(index == number) {
                return line;
            }
            index++;
        }
        throw new IllegalArgumentException(
            "Couldn't find line " + number
          + ". Pay attention, there are "
          + this.lines().size() + " lines!");
    }

}
