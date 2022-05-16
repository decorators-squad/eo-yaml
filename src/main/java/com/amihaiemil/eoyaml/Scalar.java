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

/**
 * Yaml Scalar.
 * 
 * @checkstyle ReturnCount (100 lines)
 * 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * 
 * @version $Id$
 * 
 * @since 3.1.3
 */
public interface Scalar extends YamlNode {
    
    /**
     * Value of this scalar. The value returned by this method should be
     * un-escaped. e.g. if the scalar is escaped, say '#404040', the returned
     * value should be #404040.
     * 
     * @return String value.
     * 
     * @throws IllegalStateException In the case of reading YAML, this exception
     * is thrown if the Scalar isn't found where it's supposed to be.
     */
    String value();
    
    /**
     * Convenience method to directly read the char value of this scalar.
     * 
     * @return The char value of this scalar.
     * 
     * @since 6.0.2
     */
    default char charValue() {
        if (isEmpty()) {
            return (char) 0;
        }
        return value().charAt(0);
    }
    
    /**
     * Convenience method to directly read the boolean value of this scalar.
     * 
     * @return The boolean value of this scalar.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue() {
        return Boolean.valueOf(value());
    }
    
    /**
     * Convenience method to directly read the boolean value of this scalar.
     * 
     * @param trueValue The value to compare {@link #value()} with.
     * 
     * @return True if the value of this scalar is equal to the given trueValue.
     * 
     * @since 6.0.2
     */
    default boolean booleanValue(String trueValue) {
        return value().equalsIgnoreCase(trueValue);
    }
    
    /**
     * Convenience method to directly read the BigDecimal value of this scalar.
     * 
     * @param mathContext The math context to use.
     * 
     * @return The BigDecimal value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     * 
     * @see BigDecimal#BigDecimal(String, MathContext)
     */
    default BigDecimal bigDecimalValue(MathContext mathContext) {
        return new BigDecimal(value(), mathContext);
    }
    
    /**
     * Convenience method to directly read the BigDecimal value of this scalar.
     * 
     * @return The BigDecimal value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     * 
     * @see BigDecimal#BigDecimal(String)
     */
    default BigDecimal bigDecimalValue() {
        return new BigDecimal(value());
    }
    
    /**
     * Convenience method to directly read the double value of this scalar.
     * 
     * @param mathContext The math context to use.
     * 
     * @return The double value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     * 
     * @see BigDecimal#doubleValue()
     */
    default double doubleValue(MathContext mathContext) {
        return bigDecimalValue(mathContext).doubleValue();
    }
    
    /**
     * Convenience method to directly read the double value of this scalar.
     * 
     * @return The double value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * double.
     * 
     * @since 6.0.2
     */
    default double doubleValue() {
        return Double.parseDouble(value());
    }
    
    /**
     * Convenience method to directly read the float value of this scalar.
     * 
     * @param mathContext The math context to use.
     * 
     * @return The float value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigDecimal.
     * 
     * @since 6.0.2
     * 
     * @see BigDecimal#floatValue()
     */
    
    default float floatValue(MathContext mathContext) {
        return bigDecimalValue(mathContext).floatValue();
    }
    
    /**
     * Convenience method to directly read the float value of this scalar.
     * 
     * @return The float value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * float.
     * 
     * @since 6.0.2
     */
    default float floatValue() {
        return Float.parseFloat(value());
    }
    
    /**
     * Convenience method to directly read the BigInteger value of this scalar.
     * 
     * @param radix Radix to be used in interpreting {@link #value()}.
     * 
     * @return The BigInteger value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     * 
     * @see BigInteger#BigInteger(String, int)
     */
    default BigInteger bigIntegerValue(int radix) {
        return new BigInteger(value(), radix);
    }
    
    /**
     * Convenience method to directly read the BigInteger value of this scalar.
     * 
     * @return The BigInteger value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     * 
     * @see BigInteger#BigInteger(String)
     */
    default BigInteger bigIntegerValue() {
        return new BigInteger(value());
    }
    
    /**
     * Convenience method to directly read the long value of this scalar.
     * 
     * @param radix Radix to be used in interpreting {@link #value()}.
     * 
     * @return The long value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     * 
     * @see #bigIntegerValue(int)
     */
    default long longValue(int radix) {
        return bigIntegerValue(radix).longValue();
    }
    
    /**
     * Convenience method to directly read the long value of this scalar.
     * 
     * @return The long value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * long.
     * 
     * @since 6.0.2
     */
    default long longValue() {
        return Long.parseLong(value());
    }
    
    /**
     * Convenience method to directly read the int value of this scalar.
     * 
     * @param radix Radix to be used in interpreting {@link #value()}.
     * 
     * @return The int value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     * 
     * @see #bigIntegerValue(int)
     */
    default int intValue(int radix) {
        return bigIntegerValue(radix).intValue();
    }
    
    /**
     * Convenience method to directly read the int value of this scalar.
     * 
     * @return The int value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * int.
     * 
     * @since 6.0.2
     */
    default int intValue() {
        return Integer.parseInt(value());
    }
    
    /**
     * Convenience method to directly read the short value of this scalar.
     * 
     * @param radix Radix to be used in interpreting {@link #value()}.
     * 
     * @return The short value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     * 
     * @see #bigIntegerValue(int)
     */
    default short shortValue(int radix) {
        return bigIntegerValue(radix).shortValue();
    }
    
    /**
     * Convenience method to directly read the short value of this scalar.
     * 
     * @return The short value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * short.
     * 
     * @since 6.0.2
     */
    default short shortValue() {
        return Short.parseShort(value());
    }
    
    /**
     * Convenience method to directly read the byte value of this scalar.
     * 
     * @param radix Radix to be used in interpreting {@link #value()}.
     * 
     * @return The byte value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * BigInteger.
     * 
     * @since 6.0.2
     * 
     * @see #bigIntegerValue(int)
     */
    default byte byteValue(int radix) {
        return bigIntegerValue(radix).byteValue();
    }
    
    /**
     * Convenience method to directly read the byte value of this scalar.
     * 
     * @return The byte value of this scalar.
     * 
     * @throws NumberFormatException If the {@link #value()} is not a parsable
     * byte.
     * 
     * @since 6.0.2
     */
    default byte byteValue() {
        return Byte.parseByte(value());
    }
}