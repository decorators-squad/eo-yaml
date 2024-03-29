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

import java.util.List;
import java.util.stream.Collectors;

/**
 * YamlLine made up of multiple YAML lines collapsed onto one.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 8.0.0
 */
final class CollapsedYamlLine implements YamlLine {

    /**
     * Yaml lines to collapse onto one single line.
     */
    private final List<YamlLine> lines;

    /**
     * Ctor.
     * @param lines Yaml lines to collapse onto one single line.
     */
    CollapsedYamlLine(final List<YamlLine> lines) {
        this.lines = lines;
    }

    @Override
    public String value() {
        return this.lines.stream()
            .map(YamlLine::value)
            .collect(Collectors.joining(" "));
    }

    @Override
    public int number() {
        final int number;
        if(this.lines.isEmpty()) {
            number = -1;
        } else {
            number = this.lines.get(0).number();
        }
        return number;
    }

    @Override
    public int indentation() {
        final int indentation;
        if(this.lines.isEmpty()) {
            indentation = -1;
        } else {
            indentation = this.lines.get(0).indentation();
        }
        return indentation;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
