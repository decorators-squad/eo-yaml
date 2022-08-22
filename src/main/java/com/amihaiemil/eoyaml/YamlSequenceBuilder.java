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

/**
 * Builder of YamlSequence. Implementations should be immutable and thread-safe.
 * @author Salavat.Yalalov (s.yalalov@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlSequenceBuilder {

    /**
     * Add a value to the sequence.
     * @param value String
     * @return This builder
     */
    YamlSequenceBuilder add(final String value);

    /**
     * Get the number of elements in the generated sequence.
     * @return The number of elements in the generated sequence.
     * 
     * @since 6.0.4
     */
    int size();

    /**
     * Returns true if this sequence builder has no elements.
     * @return true if this sequence builder has no elements.
     * 
     * @since 6.0.4
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Add a value to the sequence.
     * @param value String value to be added.
     * @param inlineComment The yaml comment for the value.
     * @return This builder.
     * 
     * @since 6.0.4
     */
    YamlSequenceBuilder add(final String value, final String inlineComment);

    /**
     * Add a value to the sequence.
     * @param node YamlNode
     * @return This builder
     */
    YamlSequenceBuilder add(final YamlNode node);

    /**
     * Add a value to the sequence.
     * @param value String value to be added.
     * @param index The index location to add the value into.
     * @return This builder.
     * 
     * @since 6.0.4
     * 
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    YamlSequenceBuilder add(final String value, final int index);

    /**
     * Add a value to the sequence.
     * @param value String value to be added.
     * @param inlineComment The yaml comment for the value.
     * @param index The index location to add the value into.
     * @return This builder.
     * 
     * @since 6.0.4
     * 
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    YamlSequenceBuilder add(final String value, final String inlineComment, final int index);

    
    /**
     * Add a value to the sequence.
     * @param node The YamlNode to be added.
     * @param index The index location to add the value into.
     * @return This builder.
     * 
     * @since 6.0.4
     * 
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    YamlSequenceBuilder add(final YamlNode node, final int index);

    /**
     * Remove a value from the sequence in the given index.
     * @param index The index of the element to be removed.
     * @return The removed node.
     * 
     * @since 6.0.4
     * 
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    YamlNode remove(int index);

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
    default YamlSequence build(final Collection<String> comment) {
        return this.build(String.join(System.lineSeparator(), comment));
    }

    /**
     * Build the YamlSequence and specify a comment referring to it.
     * @param comment Comment about the built YamlSequence.
     * @return Built YamlSequence
     */
    YamlSequence build(final String comment);
}
