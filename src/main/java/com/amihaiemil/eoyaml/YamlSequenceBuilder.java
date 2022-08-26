/**
 * Copyright (c) 2016-2022, Mihai Emil Andronache
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
import java.util.stream.Stream;

/**
 * Builder of YamlSequence. Implementations should be thread-safe.
 * @author Salavat.Yalalov (s.yalalov@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlSequenceBuilder extends Iterable<YamlNode> {

    /**
     * Add a value to the sequence.
     * @param value String
     * @return This builder if the YamlSequenceBuilder implementantion is
     * mutable, else a new Builder is returned.
     */
    YamlSequenceBuilder add(String value);

    /**
     * Add a value to the sequence.
     * @param value String
     * @param inlineComment The inline comment for the value
     * @return This builder if the YamlSequenceBuilder implementantion is
     * mutable, else a new Builder is returned.
     */
    YamlSequenceBuilder add(String value, String inlineComment);

    /**
     * Add a value to the sequence.
     * @param node YamlNode
     * @return This builder if the YamlSequenceBuilder implementantion is
     * mutable, else a new Builder is returned.
     */
    YamlSequenceBuilder add(YamlNode node);

    /**
     * Removes the first occurrence of the specified value from this builder.
     * @param value String
     * @return This builder if the YamlSequenceBuilder implementantion is
     * mutable, else a new Builder is returned.
     */
    YamlSequenceBuilder remove(String value);

    /**
     * Removes the first occurrence of the specified node from this builder.
     * @param node YamlNode
     * @return This builder if the YamlSequenceBuilder implementantion is
     * mutable, else a new Builder is returned.
     */
    YamlSequenceBuilder remove(YamlNode node);

    /**
     * Returns an iterator over nodes present in the builder.
     * @throws UnsupportedOperationException If the implementation of the
     * YamlSequenceBuilder is immutable.
     */
    @Override
    Iterator<YamlNode> iterator() throws UnsupportedOperationException;

    /**
     * Returns an stream over the nodes present in the builder.
     * @return A thread safe stream of the values present in this
     * YamlSequenceBuilder.
     * @throws UnsupportedOperationException If the implementation of the
     * YamlSequenceBuilder is immutable.
     */
    Stream<YamlNode> stream() throws UnsupportedOperationException;

    /**
     * Build the YamlSequence.
     * @return Built YamlSequence
     */
    default YamlSequence build() {
        return this.build("");
    }

    /**
     * Build the YamlSequence and specify a comment referring to it.
     * @param comment The multiple line comment about the built YamlSequence.
     * @return Built YamlSequence
     */
    default YamlSequence build(Collection<String> comment) {
        return this.build(String.join(System.lineSeparator(), comment));
    }

    /**
     * Build the YamlSequence and specify a comment referring to it.
     * @param comment Comment about the built YamlSequence.
     * @return Built YamlSequence
     */
    YamlSequence build(String comment);
}
