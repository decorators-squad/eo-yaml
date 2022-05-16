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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * A Yaml sequence.
 * 
 * @checkstyle ReturnCount (1500 lines)
 * 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * 
 * @version $Id$
 * 
 * @since 1.0.0
 */
public interface YamlSequence extends YamlNode, Iterable<YamlNode> {
    
    /**
     * Fetch the values of this sequence.
     * 
     * @return The Collection containing all the of {@link YamlNode}s of this
     * sequence.
     */
    Collection<YamlNode> values();
    
    /**
     * Returns this YamlSequence's children Iterator.<br>
     * <br>
     * It is equivalent to YamlSequence.values().iterator().
     * 
     * @return The Iterator of the {@link #values()}.
     */
    default Iterator<YamlNode> iterator() {
        return values().iterator();
    }
    
    /**
     * The number of Yaml nodes (scalars, mappings and sequences) found in this
     * sequence.
     * 
     * @return The {@link #values()} size.
     */
    default int size() {
        return values().size();
    }
    
    /**
     * Get the index from the given YamlNode.
     * 
     * @param node The node to get the index from.
     * 
     * @return The index of the given node, if the given node is not part of
     * this sequence, -1 is returned.
     * 
     * @since 6.0.2
     */
    default int indexOf(YamlNode node) {
        int count = 0;
        
        for (final YamlNode value : values()) {
            if (value.equals(node)) {
                return count;
            }
            count++;
        }
        return -1;
    }
    
    /**
     * Get the Yaml node from the given index.
     * 
     * @param index The index to get the node from.
     * 
     * @return The Yaml node from the given index, if the index is out of
     * bounds, <code>null</code> is returned.
     * 
     * @since 6.0.2
     */
    default YamlNode value(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        int count = 0;
        
        for (final YamlNode value : values()) {
            if (count == index) {
                return value;
            }
            count++;
        }
        return null;
    }
    
    /**
     * Get the Yaml mapping from the given index.
     * 
     * @param index The index of the Yaml mapping in this sequence.
     * 
     * @return The Yaml mapping in the given index, if the index is out of
     * bounds or the node for the given index is not a instance of
     * {@link YamlMapping}, <code>null</code> is returned.
     */
    default YamlMapping yamlMapping(int index) {
        YamlNode value = value(index);
        
        if (value instanceof YamlMapping) {
            return (YamlMapping) value;
        }
        return null;
    }
    
    /**
     * Get the Yaml sequence from the given index.
     * 
     * @param index The index of the Yaml sequence in this sequence.
     * 
     * @return The Yaml sequence in the given index, if the index is out of
     * bounds or the node for the given index is not a instance of
     * {@link YamlSequence}, <code>null</code> is returned.
     */
    default YamlSequence yamlSequence(int index) {
        YamlNode value = value(index);
        
        if (value instanceof YamlSequence) {
            return (YamlSequence) value;
        }
        return null;
    }
    
    /**
     * Get the Yaml scalar from the given index.
     * 
     * @param index The index of the Yaml scalar in this sequence.
     * 
     * @return The Yaml scalar in the given index, if the index is out of bounds
     * or the node for the given index is not a instance of {@link Scalar},
     * <code>null</code> is returned.
     * 
     * @since 6.0.2
     */
    default Scalar yamlScalar(int index) {
        YamlNode value = value(index);
        
        if (value instanceof Scalar) {
            return (Scalar) value;
        }
        return null;
    }
    
    /**
     * Get the String from the given index.
     * 
     * @param index The index of the String in this sequence.
     * 
     * @return The String in the given index, if the index is out of bounds or
     * the node for the given index is not a instance of {@link Scalar},
     * <code>null</code> is returned.
     */
    default String string(final int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return null;
        }
        return value.value();
    }
    
    /**
     * Get the folded block scalar from the given index.
     * 
     * @param index The index of the folded block scalar in this sequence.
     * 
     * @return The folded block scalar as String, if the index is out of bounds
     * or the node for the given index is not a instance of {@link Scalar},
     * <code>null</code> is returned.
     */
    default String foldedBlockScalar(final int index) {
        return string(index);
    }
    
    /**
     * Get the literal block scalar from the given index.
     * 
     * @param index The index of the literal block scalar in this sequence.
     * 
     * @return The folded block scalar as a Collection of Strings, if the index
     * is out of bounds or the node for the given index is not a instance of
     * {@link Scalar}, <code>null</code> is returned.
     */
    default Collection<String> literalBlockScalar(int index) {
        String value = string(index);
        
        if (value == null) {
            return null;
        }
        return Arrays.asList(value.split(System.lineSeparator()));
    }
    
    /**
     * Convenience method to directly read a boolean value from this sequence.
     * It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     boolean value = scalar.booleanValue();
     * </pre>
     * 
     * @param index The index of the boolean value in this sequence.
     * 
     * @return The boolean value in the given index, false otherwise.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return false;
        }
        return value.booleanValue();
    }
    
    /**
     * Convenience method to directly read a boolean value from this sequence.
     * It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     boolean value = scalar.booleanValue(...);
     * </pre>
     * 
     * @param index The index of the boolean value in this sequence.
     * @param trueValue The value to compare the Scalar value with.
     * 
     * @return The boolean value in the given index, false otherwise.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue(int index, String trueValue) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return false;
        }
        return value.booleanValue(trueValue);
    }
    
    /**
     * Convenience method to directly read a BigDecimal value from this
     * sequence. It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     BigDecimal value = scalar.bigDecimalValue(...);
     * </pre>
     * 
     * @param index The index of the BigDecimal value in this sequence.
     * @param mathContext The context to use.
     * 
     * @return The BigDecimal value in the given index, <code>null</code>
     * otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default BigDecimal bigDecimalNumber(int index, MathContext mathContext) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return null;
        }
        return value.bigDecimalValue(mathContext);
    }
    
    /**
     * Convenience method to directly read a BigInteger value from this
     * sequence. It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     BigInteger value = scalar.bigIntegerValue();
     * </pre>
     * 
     * @param index The index of the BigInteger value in this sequence.
     * 
     * @return The BigInteger value in the given index, <code>null</code>
     * otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default BigDecimal bigDecimalNumber(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return null;
        }
        return value.bigDecimalValue();
    }
    
    /**
     * Convenience method to directly read a double value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     double value = scalar.doubleValue(...);
     * </pre>
     * 
     * @param index The index of the double value in this sequence.
     * @param mathContext The context to use.
     * 
     * @return The double value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default double doubleNumber(int index, MathContext mathContext) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.doubleValue(mathContext);
    }
    
    /**
     * Convenience method to directly read a double value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     double value = scalar.doubleValue();
     * </pre>
     * 
     * @param index The index of the double value in this sequence.
     * 
     * @return The double value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * double.
     */
    default double doubleNumber(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.doubleValue();
    }
    
    /**
     * Convenience method to directly read a float value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     float value = scalar.floatValue(...);
     * </pre>
     * 
     * @param index The index of the float value in this sequence.
     * @param mathContext The context to use.
     * 
     * @return The float value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default float floatNumber(int index, MathContext mathContext) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.floatValue(mathContext);
    }
    
    /**
     * Convenience method to directly read a float value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     float value = scalar.floatValue();
     * </pre>
     * 
     * @param index The index of the float value in this sequence.
     * 
     * @return The float value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * float.
     */
    default float floatNumber(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.floatValue();
    }
    
    /**
     * Convenience method to directly read a BigInteger value from this
     * sequence. It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     BigInteger value = scalar.bigIntegerValue(...);
     * </pre>
     * 
     * @param index The index of the BigInteger value in this sequence.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return The BigInteger value in the given index, <code>null</code>
     * otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default BigInteger bigIntegerNumber(int index, int radix) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return null;
        }
        return value.bigIntegerValue(radix);
    }
    
    /**
     * Convenience method to directly read a BigInteger value from this
     * sequence. It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     BigInteger value = scalar.bigIntegerValue();
     * </pre>
     * 
     * @param index The index of the BigInteger value in this sequence.
     * 
     * @return The BigInteger value in the given index, <code>null</code>
     * otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default BigInteger bigIntegerNumber(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return null;
        }
        return value.bigIntegerValue();
    }
    
    /**
     * Convenience method to directly read a long value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     long value = scalar.longValue(...);
     * </pre>
     * 
     * @param index The index of the long value in this sequence.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return The long value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default long longNumber(int index, int radix) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.longValue(radix);
    }
    
    /**
     * Convenience method to directly read a long value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     long value = scalar.longValue();
     * </pre>
     * 
     * @param index The index of the long value in this sequence.
     * 
     * @return The long value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable long.
     */
    default long longNumber(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.longValue();
    }
    
    /**
     * Convenience method to directly read a integer value from this sequence.
     * It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     int value = scalar.intValue(...);
     * </pre>
     * 
     * @param index The index of the integer value in this sequence.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return The integer value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default int integer(int index, int radix) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.intValue(radix);
    }
    
    /**
     * Convenience method to directly read an integer value from this sequence.
     * It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     int value = scalar.intValue();
     * </pre>
     * 
     * @param index The index of the integer value in this sequence.
     * 
     * @return The integer value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * Integer.
     */
    default int integer(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.intValue();
    }
    
    /**
     * Convenience method to directly read a short value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     short value = scalar.shortValue(...);
     * </pre>
     * 
     * @param index The index of the short value in this sequence.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return The short value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default short shortNumber(int index, int radix) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.shortValue(radix);
    }
    
    /**
     * Convenience method to directly read a short value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     short value = scalar.shortValue();
     * </pre>
     * 
     * @param index The index of the short value in this sequence.
     * 
     * @return The short value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * Short.
     * 
     * @since 6.0.2
     */
    default short shortNumber(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.shortValue();
    }
    
    /**
     * Convenience method to directly read a byte value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     byte value = scalar.byteValue(...);
     * </pre>
     * 
     * @param index The index of the byte value in this sequence.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return The byte value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default byte byteNumber(int index, int radix) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.byteValue(radix);
    }
    
    /**
     * Convenience method to directly read a byte value from this sequence. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     Scalar scalar = sequence.scalar(...);
     *     byte value = scalar.byteValue();
     * </pre>
     * 
     * @param index The index of the byte value in this sequence.
     * 
     * @return The byte value in the given index, -1 otherwise.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable Byte.
     * 
     * @since 6.0.2
     */
    default byte byteNumber(int index) {
        Scalar value = yamlScalar(index);
        
        if (value == null) {
            return -1;
        }
        return value.byteValue();
    }
    
    /**
     * Convenience method to directly read a LocalDate value from this sequence.
     * It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     LocalDate dateTime = LocalDate.parse(sequence.string(...));
     * </pre>
     * 
     * @param index The index of the value.
     * 
     * @return Found LocalDate.
     * 
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDate date(int index) {
        String value = string(index);
        
        if (value != null && !value.isEmpty()) {
            return LocalDate.parse(value);
        }
        return null;
    }
    
    /**
     * Convenience method to directly read a LocalDateTime value from this
     * sequence. It is equivalent to:
     * 
     * <pre>
     *     YamlSequence sequence = ...;
     *     LocalDateTime dateTime = LocalDateTime.parse(sequence.string(...));
     * </pre>
     * 
     * @param index The index of the value.
     * 
     * @return Found LocalDateTime.
     * 
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDateTime dateTime(int index) {
        String value = string(index);
        
        if (value != null && !value.isEmpty()) {
            return LocalDateTime.parse(value);
        }
        return null;
    }
}