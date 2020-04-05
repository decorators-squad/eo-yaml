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
 * Decorator over some YamlLines which makes sure that lines
 * which satisfy one of the given conditions are ignored from
 * iteration.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
final class Skip implements YamlLines {

    /**
     * YamlLines.
     */
    private final YamlLines yamlLines;

    /**
     * If a YamlLine meets any of these conditions, it is skipped.
     */
    private final Condition[] conditions;

    /**
     * Ctor.
     * @param yamlLines The Yaml lines.
     * @param conditions Conditions.
     */
    Skip(final YamlLines yamlLines, final Condition... conditions) {
        this.yamlLines = yamlLines;
        this.conditions = conditions;
    }

    @Override
    public Iterator<YamlLine> iterator() {
        Iterator<YamlLine> iterator = this.yamlLines.iterator();
        if (iterator.hasNext()) {
            final List<YamlLine> notSkipped = new ArrayList<>();
            while (iterator.hasNext()) {
                boolean skip = false;
                final YamlLine current = iterator.next();
                for(int idx = 0; idx < this.conditions.length; idx++) {
                    if(this.conditions[idx].isMet(current)) {
                        skip = true;
                        break;
                    }
                }
                if(!skip) {
                    notSkipped.add(current);
                }
            }
            iterator = notSkipped.iterator();
        }
        return iterator;
    }

    @Override
    public Collection<YamlLine> original() {
        return this.yamlLines.original();
    }

    @Override
    public YamlNode toYamlNode(final YamlLine prev) {
        return this.yamlLines.toYamlNode(prev);
    }

    /**
     * Condition that a YamlLine has to satisfy in order to be
     * skipped/ignored from iteration.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 4.2.0
     */
    interface Condition {

        /**
         * Is this condition met or not?
         * @param line YamlLine.
         * @return True or false.
         */
        boolean isMet(final YamlLine line);

    }
}
