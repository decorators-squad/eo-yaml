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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * A Yaml sequence.
 * @checkstyle ReturnCount (400 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlSequence extends YamlNode, Iterable<YamlNode> {

    /**
     * Fetch the values of this sequence.
     * @return Collection of {@link YamlNode}
     */
    Collection<YamlNode> values();

    /**
     * Returns this YamlSequence's children Iterator.<br><br>
     * It is equivalent to YamlSequence.values().iterator().
     * @return Iterator of YamlNode.
     */
    default Iterator<YamlNode> iterator() {
        return this.values().iterator();
    }

    /**
     * The number of Yaml elements (scalars, mappings and sequences) found in
     * this sequence.
     * @return Integer.
     */
    default int size() {
        return this.values().size();
    }

    /**
     * Get the index in this YamlSequence for the given YamlNode.
     * @param node The node to get the index from.
     * @return The index of the given node, if the given node is not part of
     * this sequence, -1 is returned.
     * @since 6.0.2
     */
    default int indexOf(final YamlNode node) {
        int index = 0;

        for (final YamlNode value : this.values()) {
            if (value.equals(node)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Get the Yaml node from the given index.
     * @param index The index to get the node from.
     * @return The Yaml node from the given index, if the index is out of
     * bounds, <code>null</code> is returned.
     * @since 6.0.2
     */
    default YamlNode value(final int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        int count = 0;
        
        for (final YamlNode value : this.values()) {
            if (count == index) {
                return value;
            }
            count++;
        }
        return null;
    }

    /**
     * Get the Yaml mapping from the given index.
     * @param index Integer index.
     * @return Yaml mapping.
     */
    default YamlMapping yamlMapping(final int index) {
        final YamlNode value = this.value(index);
        if (value instanceof YamlMapping) {
            return (YamlMapping) value;
        }
        return null;
    }

    /**
     * Get the Yaml sequence from the given index.
     * @param index Integer index.
     * @return Yaml sequence.
     */
    default YamlSequence yamlSequence(final int index) {
        final YamlNode value = this.value(index);
        if (value instanceof YamlSequence) {
            return (YamlSequence) value;
        }
        return null;
    }

    /**
     * Get the String from the given index.
     * @param index Integer index.
     * @return String.
     */
    default String string(final int index) {
        final YamlNode value = this.value(index);
        if (value instanceof Scalar) {
            return ((Scalar) value).value();
        }
        return null;
    }

    /**
     * Get the folded block scalar from the given index.
     * @param index Integer index.
     * @return The folded block scalar as String.
     */
    default String foldedBlockScalar(final int index) {
        return this.string(index);
    }

    /**
     * Get the literal block scalar from the given index.
     * @param index Integer index.
     * @return The folded block scalar as String.
     */
    default Collection<String> literalBlockScalar(final int index) {
        final String value = this.string(index);
        if (value != null) {
            return Arrays.asList(value.split(System.lineSeparator()));
        }
        return null;
    }

    /**
     * Convenience method to directly read an integer value
     * from this sequence. It is equivalent to:
     * <pre>
     *     YamlSequence sequence = ...;
     *     int value = Integer.parseInt(sequence.string(...));
     * </pre>
     * @param index The index of the value.
     * @return Found integer.
     * @throws NumberFormatException - if the Scalar value
     *  is not a parsable integer.
     */
    default int integer(final int index) {
        final String value = this.string(index);
        if(value != null && !value.isEmpty()) {
            return Integer.parseInt(value);
        }
        return -1;
    }

    /**
     * Convenience method to directly read a float value
     * from this sequence. It is equivalent to:
     * <pre>
     *     YamlSequence sequence = ...;
     *     float value = Float.parseFloat(sequence.string(...));
     * </pre>
     * @param index The index of the value.
     * @return Found float.
     * @throws NumberFormatException - if the Scalar value
     *  is not a parsable float.
     */
    default float floatNumber(final int index) {
        final String value = this.string(index);
        if(value != null && !value.isEmpty()) {
            return Float.parseFloat(value);
        }
        return -1;
    }

    /**
     * Convenience method to directly read a double value
     * from this sequence. It is equivalent to:
     * <pre>
     *     YamlSequence sequence = ...;
     *     double value = Double.parseDouble(sequence.string(...));
     * </pre>
     * @param index The index of the value.
     * @return Found double.
     * @throws NumberFormatException - if the Scalar value
     *  is not a parsable double.
     */
    default double doubleNumber(final int index) {
        final String value = this.string(index);
        if(value != null && !value.isEmpty()) {
            return Double.parseDouble(value);
        }
        return -1.0;
    }

    /**
     * Convenience method to directly read a long value
     * from this sequence. It is equivalent to:
     * <pre>
     *     YamlSequence sequence = ...;
     *     long value = Long.parseLong(sequence.string(...));
     * </pre>
     * @param index The index of the value.
     * @return Found long.
     * @throws NumberFormatException - if the Scalar value
     *  is not a parsable long.
     */
    default long longNumber(final int index) {
        final String value = this.string(index);
        if(value != null && !value.isEmpty()) {
            return Long.parseLong(value);
        }
        return -1L;
    }

    /**
     * Convenience method to directly read a LocalDate value
     * from this sequence. It is equivalent to:
     * <pre>
     *     YamlSequence sequence = ...;
     *     LocalDate dateTime = LocalDate.parse(sequence.string(...));
     * </pre>
     * @param index The index of the value.
     * @return Found LocalDate.
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDate date(final int index) {
        final String value = this.string(index);
        if(value != null && !value.isEmpty()) {
            return LocalDate.parse(value);
        }
        return null;
    }

    /**
     * Convenience method to directly read a LocalDateTime value
     * from this sequence. It is equivalent to:
     * <pre>
     *     YamlSequence sequence = ...;
     *     LocalDateTime dateTime = LocalDateTime.parse(sequence.string(...));
     * </pre>
     * @param index The index of the value.
     * @return Found LocalDateTime.
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDateTime dateTime(final int index) {
        final String value = this.string(index);
        if(value != null && !value.isEmpty()) {
            return LocalDateTime.parse(value);
        }
        return null;
    }
}
