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
 * When reading a single Scalar, the first of the given lines
 * dictates the type of the scalar: '>' is folded, '|' is literal etc.
 *
 * The lines will be passed altogether to the read scalar, so we have
 * to ignore these markers as they are not part of the scalar itself.
 * This class can be used as follows:
 * <pre>
 *   YamlLines noDirs = new NoScalarMarkers(
 *     new AllYamlLines(lines)
 *   );
 * </pre>
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
final class NoScalarMarkers implements YamlLines {

    /**
     * YamlLines.
     */
    private final YamlLines yamlLines;

    /**
     * Ctor.
     * @param yamlLines The Yaml lines.
     */
    NoScalarMarkers(final YamlLines yamlLines) {
        this.yamlLines = yamlLines;
    }

    /**
     * Returns an iterator over these Yaml lines.
     * It ignores the lines containing YAML Directives.
     * @return Iterator over these yaml lines.
     */
    @Override
    public Iterator<YamlLine> iterator() {
        Iterator<YamlLine> iterator = this.yamlLines.iterator();
        if (iterator.hasNext()) {
            final List<YamlLine> noScalarMarkers = new ArrayList<>();
            while (iterator.hasNext()) {
                final YamlLine current = iterator.next();
                final String line = current.trimmed();
                if (line.endsWith(">") || line.endsWith("|")) {
                    continue;
                } else {
                    noScalarMarkers.add(current);
                }
            }
            iterator = noScalarMarkers.iterator();
        }
        return iterator;
    }

    @Override
    public Collection<YamlLine> lines() {
        return this.yamlLines.lines();
    }

    @Override
    public AllYamlLines nested(final int after) {
        return this.yamlLines.nested(after);
    }

    @Override
    public YamlNode toYamlNode(final YamlLine prev) {
        return this.yamlLines.toYamlNode(prev);
    }
}
