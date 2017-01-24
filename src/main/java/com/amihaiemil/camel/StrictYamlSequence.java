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

import java.util.Collection;

/**
 * Decorator for a {@link YamlSequence} which throws YamlNodeNotFoundException
 * if any of the methods of the decorated YamlSequence returns null
 * (if the given index points to a YamlNode that is not a YamlMapping,
 * for instance).
 * @author Salavat.Yalalov (s.yalalov@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class StrictYamlSequence implements YamlSequence {
    /**
     * Original YamlSequence.
     */
    private YamlSequence decorated;

    /**
     * Ctor.
     * @param decorated Original YamlSequence
     */
    public StrictYamlSequence(final YamlSequence decorated) {
        this.decorated = decorated;
    }

    @Override
    public Collection<YamlNode> children() {
        return this.decorated.children();
    }

    @Override
    public int compareTo(final YamlNode other) {
        return this.decorated.compareTo(other);
    }

    /**
     * Returns the number of elements in this Yaml sequence.
     * @return Integer size >= 0
     */
    @Override
    public int size() {
        return this.decorated.size();
    }

    /**
     * Get the Yaml mapping  from the given index.
     * @param index Integer index.
     * @return Yaml mapping
     */
    @Override
    public YamlMapping yamlMapping(final int index) {
        YamlMapping found = this.decorated.yamlMapping(index);
        if (found == null) {
            throw new YamlNodeNotFoundException(
                "No YamlMapping found for index" + index
            );
        }
        return found;
    }

    /**
     * Get the Yaml sequence  from the given index.
     * @param index Integer index.
     * @return Yaml sequence
     */
    @Override
    public YamlSequence yamlSequence(final int index) {
        YamlSequence found = this.decorated.yamlSequence(index);
        if (found == null) {
            throw new YamlNodeNotFoundException(
                "No YamlSequence found for index" + index
            );
        }
        return found;
    }

    /**
     * Get the String from the given index.
     * @param index Integer index.
     * @return String
     */
    @Override
    public String string(final int index) {
        String found = this.decorated.string(index);
        if (found == null) {
            throw new YamlNodeNotFoundException(
                "No String found for index" + index
            );
        }
        return found;
    }
}
