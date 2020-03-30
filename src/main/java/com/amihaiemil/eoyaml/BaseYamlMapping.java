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

import java.util.Iterator;
import java.util.Set;


/**
 * Base YamlMapping which all implementations of YamlMapping should extend.
 * It implementing toString(), equals, hashcode and compareTo methods.
 * <br><br>
 * These methods should be default methods on the interface,
 * but we are not allowed to have default implementations of java.lang.Object
 * methods.<br><br>
 * This class also offers the package-protected indent(...) method, which
 * returns the indented value of the mapping, used in printing YAML. This
 * method should NOT be visible to users.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
public abstract class BaseYamlMapping
    extends BaseYamlNode implements YamlMapping {
    @Override
    public final int hashCode() {
        int hash = 0;
        for(final YamlNode key : this.keys()) {
            hash += key.hashCode();
        }
        for(final YamlNode value : this.values()) {
            hash += value.hashCode();
        }
        return hash;
    }

    /**
     * Equals method for YamlMapping. It returns true if the compareTo(...)
     * method returns 0.
     * @param other The YamlMapping to which this is compared.
     * @return True or false.
     */
    @Override
    public final boolean equals(final Object other) {
        final boolean result;
        if (other == null || !(other instanceof YamlMapping)) {
            result = false;
        } else if (this == other) {
            result = true;
        } else {
            result = this.compareTo((YamlMapping) other) == 0;
        }
        return result;
    }

    /**
     * Compare this Mapping to another node.<br><br>
     *
     * A Mapping is always considered greater than a Scalar or a Sequence.<br>
     *
     * If other is a Mapping, their integer lengths are compared - the one with
     * the greater length is considered greater. If the lengths are equal,
     * then the 2 Mappings are equal if all elements are equal (K==K and V==V).
     * If the elements are not identical, the comparison of the first unequal
     * elements is returned.
     *
     * @param other The other AbstractNode.
     * @checkstyle NestedIfDepth (100 lines)
     * @checkstyle ExecutableStatementCount (100 lines)
     * @return
     *   a value &lt; 0 if this &lt; other <br>
     *   0 if this == other or <br>
     *   a value &gt; 0 if this &gt; other
     */
    @Override
    public final int compareTo(final YamlNode other) {
        int result = 0;
        if (other == null || !(other instanceof YamlMapping)) {
            result = 1;
        } else if (this != other) {
            final BaseYamlMapping map = (BaseYamlMapping) other;
            final Set<YamlNode> keys = this.keys();
            final Set<YamlNode> otherKeys = map.keys();
            if(keys.size() > otherKeys.size()) {
                result = 1;
            } else if (keys.size() < otherKeys.size()) {
                result = -1;
            } else {
                final Iterator<YamlNode> keysIt = keys.iterator();
                final Iterator<YamlNode> otherKeysIt = otherKeys.iterator();
                final Iterator<YamlNode> values = this.values().iterator();
                final Iterator<YamlNode> otherVals = map.values().iterator();
                int keysComparison;
                int valuesComparison;
                while(values.hasNext()) {
                    keysComparison = keysIt.next()
                        .compareTo(otherKeysIt.next());
                    valuesComparison = values.next()
                        .compareTo(otherVals.next());
                    if(keysComparison != 0) {
                        result = keysComparison;
                        break;
                    }
                    if(valuesComparison != 0) {
                        result = valuesComparison;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Indent this YamlMapping. This is a base method since indentation
     * logic should be identical for any kind of YamlMapping, regardless of
     * its implementation.
     *
     * Keep this method package-protected, it should NOT be visible to users.
     *
     * @param indentation Indentation to start with. Usually, it's 0, since we
     *  don't want to have spaces at the beginning. But in the case of nested
     *  YamlNodes, this value may be greater than 0.
     * @return String indented YamlMapping, by the specified indentation.
     */
    final String indent(final int indentation) {
        if(indentation < 0) {
            throw new IllegalArgumentException(
                "Indentation level has to be >=0"
            );
        }
        final String newLine = System.lineSeparator();
        final StringBuilder print = new StringBuilder();
        int spaces = indentation;
        final StringBuilder alignment = new StringBuilder();
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        for(final YamlNode key : this.keys()) {
            print.append(alignment);
            final BaseYamlNode indKey = (BaseYamlNode) key;
            final BaseYamlNode value = (BaseYamlNode) this.value(key);
            if(indKey instanceof Scalar) {
                print
                    .append(indKey.indent(0))
                    .append(":");
                if (value instanceof Scalar) {
                    print.append(" ");
                    this.printScalar((Scalar) value, print, indentation);
                } else  {
                    print
                        .append(newLine)
                        .append(value.indent(indentation + 2))
                        .append(newLine);
                }
            } else {
                print
                    .append("?")
                    .append(newLine)
                    .append(indKey.indent(indentation + 2))
                    .append(newLine)
                    .append(alignment)
                    .append(":");
                if(value instanceof Scalar) {
                    print.append(" ");
                    this.printScalar((Scalar) value, print, indentation);
                } else {
                    print
                        .append(newLine)
                        .append(value.indent(indentation + 2))
                        .append(newLine);
                }
            }
        }
        String printed = print.toString();
        if(printed.length() > 0) {
            printed = printed.substring(0, printed.length() - 1);
        }
        return printed;
    }

    /**
     * Print a Scalar. We need to check what kind of Scalar is it.
     * If it's a plain scalar, we print it on the same line. If it's
     * a folded or literal scalar, we must first print a line containing
     * '>' or '|', then print the Scalar's lines bellow, with a +2 indentation.
     * @checkstyle LineLength (50 lines)
     * @param scalar YamlNode scalar.
     * @param print Printer to add it to.
     * @param indentation How much to indent it?
     */
    private void printScalar(
        final Scalar scalar,
        final StringBuilder print,
        final int indentation
    ) {
        final BaseScalar indentable = (BaseScalar) scalar;
        if (indentable instanceof PlainStringScalar
            || indentable instanceof ReadPlainScalarValue
        ) {
            print.append(indentable.indent(0)).append(System.lineSeparator());
        } else if (indentable instanceof RtYamlScalarBuilder.BuiltFoldedBlockScalar
            || indentable instanceof ReadFoldedBlockScalar
        ) {
            print
                .append(">")
                .append(System.lineSeparator())
                .append(
                    indentable.indent(indentation + 2)
                ).append(System.lineSeparator());
        } else if (indentable instanceof RtYamlScalarBuilder.BuiltLiteralBlockScalar
            || indentable instanceof ReadLiteralBlockScalar
        ) {
            print
                .append("|")
                .append(System.lineSeparator())
                .append(
                    indentable.indent(indentation + 2)
                ).append(System.lineSeparator());
        }
    }

    /**
     * When printing a YamlMapping we dimply call this.indent(0), no need
     * to add other stuff.
     * @return This printed mapping.
     */
    @Override
    public final String toString() {
        return this.indent(0);
    }
}
