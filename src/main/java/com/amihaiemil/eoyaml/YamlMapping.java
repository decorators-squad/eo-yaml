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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A Yaml mapping.
 * 
 * @checkstyle ExecutableStatementCount (300 lines)
 * @checkstyle ReturnCount (1500 lines)
 * 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * 
 * @version $Id$
 * 
 * @since 1.0.0
 */
public interface YamlMapping extends YamlNode {
    
    /**
     * Return the keys' set of this mapping.<br>
     * <br>
     * 
     * @return Set of YamlNode keys.
     */
    Set<YamlNode> keys();
    
    /**
     * Get the YamlNode mapped to the specified key.
     * 
     * @param key YamlNode key. Could be a simple scalar, a YamlMapping or a
     * YamlSequence.
     * 
     * @return The found YamlNode or null if nothing is found.
     */
    YamlNode value(YamlNode key);
    
    /**
     * Get the YamlNode mapped to the specified key.
     * 
     * @param key String key.
     * 
     * @return The found YamlNode or null if nothing is found.
     */
    default YamlNode value(final String key) {
        return value(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Fetch the values of this mapping.
     * 
     * @return Collection of {@link YamlNode}
     */
    default Collection<YamlNode> values() {
        List<YamlNode> values = new ArrayList<>();
        
        for (final YamlNode key : keys()) {
            values.add(value(key));
        }
        return values;
    }
    
    /**
     * Get the Yaml mapping associated with the given key.
     * 
     * @param key String key.
     * 
     * @return Yaml mapping or null if the key is missing, or not pointing to a
     * mapping.
     */
    default YamlMapping yamlMapping(String key) {
        return yamlMapping(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Get the Yaml mapping associated with the given key.
     * 
     * @param key Yaml node (mapping or sequence) key.
     * 
     * @return Yaml mapping or null if the key is missing, or not pointing to a
     * mapping.
     */
    default YamlMapping yamlMapping(YamlNode key) {
        YamlNode value = value(key);
        
        if (value instanceof YamlMapping) {
            return (YamlMapping) value;
        }
        return null;
    }
    
    /**
     * Get the Yaml sequence associated with the given key.
     * 
     * @param key String key.
     * 
     * @return Yaml sequence or null if the key is missing, or not pointing to a
     * sequence.
     */
    default YamlSequence yamlSequence(String key) {
        return yamlSequence(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Get the Yaml sequence associated with the given key.
     * 
     * @param key Yaml node (mapping or sequence) key.
     * 
     * @return Yaml sequence or null if the key is missing, or not pointing to a
     * sequence.
     */
    default YamlSequence yamlSequence(YamlNode key) {
        YamlNode value = value(key);
        
        if (value instanceof YamlSequence) {
            return (YamlSequence) value;
        }
        return null;
    }
    
    /**
     * Get the Yaml scalar associated with the given key.
     * 
     * @param key String key.
     * 
     * @return Yaml scalar or null if the key is missing, or not pointing to a
     * scalar.
     * 
     * @see 6.0.2
     */
    default Scalar yamlScalar(String key) {
        return yamlScalar(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Get the Yaml scalar associated with the given key.
     * 
     * @param key Yaml node (mapping or sequence) key.
     * 
     * @return Yaml scalar or null if the key is missing, or not pointing to a
     * scalar.
     * 
     * @see 6.0.2
     */
    default Scalar yamlScalar(YamlNode key) {
        YamlNode value = value(key);
        
        if (value instanceof Scalar) {
            return (Scalar) value;
        }
        return null;
    }
    
    /**
     * Get the String associated with the given key.
     * 
     * @param key String key
     * 
     * @return String or null if the key is missing, or not pointing to a
     * scalar.
     */
    default String string(String key) {
        return string(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Get the String associated with the given key.
     * 
     * @param key Yaml node key
     * 
     * @return String or null if the key is missing, or not pointing to a
     * scalar.
     */
    default String string(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return null;
        }
        return value.value();
    }
    
    /**
     * Get the String folded block scalar associated with the given key.
     * 
     * @param key String key
     * 
     * @return String or null if the key is missing, or not pointing to a folded
     * block scalar.
     */
    default String foldedBlockScalar(String key) {
        return string(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Get the String folded block scalar associated with the given key.
     * 
     * @param key Yaml node key.
     * 
     * @return String or null if the key is missing, or not pointing to a folded
     * block scalar.
     */
    default String foldedBlockScalar(YamlNode key) {
        return string(key);
    }
    
    /**
     * Get the String lines of the literal block scalar associated with the
     * given key.
     * 
     * @param key String key
     * 
     * @return Collection of String or null if the key is missing, or not
     * pointing to a literal block scalar.
     */
    default Collection<String> literalBlockScalar(String key) {
        return literalBlockScalar(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Get the String lines of the literal block scalar associated with the
     * given key.
     * 
     * @param key String key
     * 
     * @return Collection of String or null if the key is missing, or not
     * pointing to a literal block scalar.
     */
    default Collection<String> literalBlockScalar(YamlNode key) {
        String value = string(key);
        
        if (value == null) {
            return null;
        }
        return Arrays.asList(value.split(System.lineSeparator()));
    }
    
    /**
     * Convenience method to directly read a boolean value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     boolean value = scalar.booleanValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found boolean or <code>false</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue(String key) {
        return booleanValue(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a boolean value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     boolean value = scalar.booleanValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found boolean or <code>false</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return false;
        }
        return value.booleanValue();
    }
    
    /**
     * Convenience method to directly read a boolean value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     boolean value = scalar.booleanValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param trueValue The value to compare the Scalar value with.
     * 
     * @return Found boolean or <code>false</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue(String key, String trueValue) {
        return booleanValue(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                trueValue);
    }
    
    /**
     * Convenience method to directly read a boolean value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     boolean value = scalar.booleanValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param trueValue The value to compare the Scalar value with.
     * 
     * @return Found boolean or <code>false</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue(YamlNode key, String trueValue) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return false;
        }
        return value.booleanValue(trueValue);
    }
    
    /**
     * Convenience method to directly read a BigDecimal value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigDecimal value = scalar.bigDecimalValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param mathContext The math context to use.
     * 
     * @return Found BigDecimal or <code>null</code> if there is no value for
     * the key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default BigDecimal bigDecimalNumber(String key, MathContext mathContext) {
        return bigDecimalNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                mathContext);
    }
    
    /**
     * Convenience method to directly read a BigDecimal value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigDecimal value = scalar.bigDecimalValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param mathContext The math context to use.
     * 
     * @return Found BigDecimal or <code>null</code> if there is no value for
     * the key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default BigDecimal bigDecimalNumber(YamlNode key, MathContext mathContext) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return null;
        }
        return value.bigDecimalValue(mathContext);
    }
    
    /**
     * Convenience method to directly read a BigDecimal value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigDecimal value = scalar.bigDecimalValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found BigDecimal or <code>null</code> if there is no value for
     * the key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default BigDecimal bigDecimalNumber(String key) {
        return bigDecimalNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a BigDecimal value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigDecimal value = scalar.bigDecimalValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found BigDecimal or <code>null</code> if there is no value for
     * the key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default BigDecimal bigDecimalNumber(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return null;
        }
        return value.bigDecimalValue();
    }
    
    /**
     * Convenience method to directly read a double value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     double value = scalar.doubleValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param mathContext The math context to use.
     * 
     * @return Found double or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default double doubleNumber(String key, MathContext mathContext) {
        return doubleNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                mathContext);
    }
    
    /**
     * Convenience method to directly read a double value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     double value = scalar.doubleValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param mathContext The math context to use.
     * 
     * @return Found double or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default double doubleNumber(YamlNode key, MathContext mathContext) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.doubleValue(mathContext);
    }
    
    /**
     * Convenience method to directly read a double value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     double value = scalar.doubleValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found double or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * double.
     */
    default double doubleNumber(String key) {
        return doubleNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a double value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     double value = scalar.doubleValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found double or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * double.
     */
    default double doubleNumber(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.doubleValue();
    }
    
    /**
     * Convenience method to directly read a float value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     float value = scalar.floatValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param mathContext The math context to use.
     * 
     * @return Found float or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default float floatNumber(String key, MathContext mathContext) {
        return floatNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                mathContext);
    }
    
    /**
     * Convenience method to directly read a float value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     float value = scalar.floatValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param mathContext The math context to use.
     * 
     * @return Found float or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default float floatNumber(YamlNode key, MathContext mathContext) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.floatValue(mathContext);
    }
    
    /**
     * Convenience method to directly read a float value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     float value = scalar.floatValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found float or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * float.
     */
    default float floatNumber(String key) {
        return floatNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a float value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     float value = scalar.floatValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found float or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * float.
     */
    default float floatNumber(final YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.floatValue();
    }
    
    /**
     * Convenience method to directly read a BigInteger value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigInteger value = scalar.bigIntegerValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found long or <code>null</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default BigInteger bigIntegerNumber(String key, int radix) {
        return bigIntegerNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                radix);
    }
    
    /**
     * Convenience method to directly read a BigInteger value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigInteger value = scalar.bigIntegerValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found BigInteger or <code>null</code> if there is no value for
     * the key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default BigInteger bigIntegerNumber(YamlNode key, int radix) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return null;
        }
        return value.bigIntegerValue(radix);
    }
    
    /**
     * Convenience method to directly read a BigInteger value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigInteger value = scalar.bigIntegerValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found BigInteger or <code>null</code> if there is no value for
     * the key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default BigInteger bigIntegerNumber(String key) {
        return bigIntegerNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a BigInteger value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     BigInteger value = scalar.bigIntegerValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found BigInteger or <code>null</code> if there is no value for
     * the key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default BigInteger bigIntegerNumber(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return null;
        }
        return value.bigIntegerValue();
    }
    
    /**
     * Convenience method to directly read a long value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     long value = scalar.longValue(radix);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found long or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default long longNumber(String key, int radix) {
        return longNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                radix);
    }
    
    /**
     * Convenience method to directly read a long value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     long value = scalar.longValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found long or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     */
    default long longNumber(YamlNode key, int radix) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.longValue(radix);
    }
    
    /**
     * Convenience method to directly read a long value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     long value = scalar.longValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found long or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable long.
     */
    default long longNumber(String key) {
        return longNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a long value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     long value = scalar.longValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found long or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable long.
     */
    default long longNumber(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.longValue();
    }
    
    /**
     * Convenience method to directly read a integer value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     int value = scalar.intValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found integer or <code>-1</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default int integer(String key, int radix) {
        return integer(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                radix);
    }
    
    /**
     * Convenience method to directly read a integer value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     int value = scalar.intValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found integer or <code>-1</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default int integer(YamlNode key, int radix) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.intValue(radix);
    }
    
    /**
     * Convenience method to directly read a integer value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     int value = scalar.intValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found integer or <code>-1</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * integer.
     */
    default int integer(String key) {
        return integer(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a integer value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     int value = scalar.intValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found integer or <code>-1</code> if there is no value for the
     * key, or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * integer.
     */
    default int integer(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.intValue();
    }
    
    /**
     * Convenience method to directly read a short value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     short value = scalar.shortValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found short or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default short shortNumber(String key, int radix) {
        return shortNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                radix);
    }
    
    /**
     * Convenience method to directly read a short value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     short value = scalar.shortValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found short or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default short shortNumber(YamlNode key, int radix) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.shortValue(radix);
    }
    
    /**
     * Convenience method to directly read a short value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     short value = scalar.shortValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found short or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * short.
     * 
     * @since 6.0.2
     */
    default short shortNumber(String key) {
        return shortNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a short value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     short value = scalar.shortValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found short or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * short.
     * 
     * @since 6.0.2
     */
    default short shortNumber(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.shortValue();
    }
    
    /**
     * Convenience method to directly read a byte value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     byte value = scalar.byteValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found byte or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default byte byteNumber(String key, int radix) {
        return byteNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar(),
                radix);
    }
    
    /**
     * Convenience method to directly read a byte value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     byte value = scalar.byteValue(...);
     * </pre>
     * 
     * @param key The key of the value.
     * @param radix The radix to be used in interpreting the scalar value.
     * 
     * @return Found byte or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     */
    default byte byteNumber(YamlNode key, int radix) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.byteValue(radix);
    }
    
    /**
     * Convenience method to directly read a byte value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     byte value = scalar.byteValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found byte or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable byte.
     * 
     * @since 6.0.2
     */
    default byte byteNumber(String key) {
        return byteNumber(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a byte value from this map. It is
     * equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     Scalar scalar = map.yamlScalar(...);
     *     byte value = scalar.byteValue();
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found byte or <code>-1</code> if there is no value for the key,
     * or the value is not a Scalar.
     * 
     * @throws NumberFormatException If the Scalar value is not a parsable byte.
     * 
     * @since 6.0.2
     */
    default byte byteNumber(YamlNode key) {
        Scalar value = yamlScalar(key);
        
        if (value == null) {
            return -1;
        }
        return value.byteValue();
    }
    
    /**
     * Convenience method to directly read a LocalDate value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     LocalDate date = LocalDate.parse(map.string(...));
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found LocalDate or null if there is no value for the key, or the
     * value is not a Scalar.
     * 
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDate date(String key) {
        return date(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a LocalDate value from this map. It
     * is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     LocalDate date = LocalDate.parse(map.string(...));
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found LocalDate or null if there is no value for the key, or the
     * value is not a Scalar.
     * 
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDate date(YamlNode key) {
        String value = string(key);
        
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value);
    }
    
    /**
     * Convenience method to directly read a LocalDateTime value from this map.
     * It is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     LocalDateTime dateTime = LocalDateTime.parse(map.string(...));
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found LocalDateTime or null if there is no value for the key, or
     * the value is not a Scalar.
     * 
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDateTime dateTime(String key) {
        return dateTime(
                Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar());
    }
    
    /**
     * Convenience method to directly read a LocalDateTime value from this map.
     * It is equivalent to:
     * 
     * <pre>
     *     YamlMapping map = ...;
     *     LocalDateTime dateTime = LocalDateTime.parse(map.string(...));
     * </pre>
     * 
     * @param key The key of the value.
     * 
     * @return Found LocalDateTime or null if there is no value for the key, or
     * the value is not a Scalar.
     * 
     * @throws DateTimeParseException - if the Scalar value cannot be parsed.
     */
    default LocalDateTime dateTime(YamlNode key) {
        String value = string(key);
        
        if (value == null) {
            return null;
        }
        return LocalDateTime.parse(value);
    }
}