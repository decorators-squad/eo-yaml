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
import java.util.Iterator;

/**
 * Base YamlSequence which all implementations should extend.
 * It implementing toString(), equals, hashcode and compareTo methods.
 * <br><br>
 * These methods should be default methods on the interface,
 * but we are not allowed to have default implementations of java.lang.Object
 * methods.<br><br>
 * This class also offers the package-protected indent(...) method, which
 * returns the indented value of the sequence, used in printing YAML. This
 * method should NOT be visible to users.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
abstract class BaseYamlSequence extends BaseYamlNode implements YamlSequence {

    @Override
    public int hashCode() {
        int hash = 0;
        for(final YamlNode node : this.values()) {
            hash += node.hashCode();
        }
        return hash;
    }

    /**
     * Equals method for YamlSequence. It returns true if the compareTo(...)
     * method returns 0.
     * @param other The YamlSequence to which this is compared.
     * @return True or false
     */
    @Override
    public boolean equals(final Object other) {
        final boolean result;
        if (other == null || !(other instanceof YamlSequence)) {
            result = false;
        } else if (this == other) {
            result = true;
        } else {
            result = this.compareTo((YamlSequence) other) == 0;
        }
        return result;
    }

    /**
     * Compare this Sequence to another node.<br><br>
     *
     * A Sequence is always considered greater than a Scalar and less than
     * a Mapping.<br>
     *
     * If other is a Sequence, their integer lengths are compared - the one with
     * the greater length is considered greater. If the lengths are equal,
     * then the 2 Sequences are equal if all elements are equal. If the
     * elements are not identical, the comparison of the first unequal
     * elements is returned.
     *
     * @param other The other YamlNode.
     * @checkstyle NestedIfDepth (100 lines)
     * @checkstyle LineLength (100 lines)
     * @return
     *   a value &lt; 0 if this &lt; other <br>
     *   0 if this == other or <br>
     *   a value &gt; 0 if this &gt; other
     */
    @Override
    public int compareTo(final YamlNode other) {
        int result = 0;
        if (other == null || other instanceof Scalar) {
            result = 1;
        } else if (other instanceof YamlMapping) {
            result = -1;
        } else if (this != other) {
            final Collection<YamlNode> nodes = this.values();
            final Collection<YamlNode> others = ((YamlSequence) other).values();
            if(nodes.size() > others.size()) {
                result = 1;
            } else if (nodes.size() < others.size()) {
                result = -1;
            } else {
                final Iterator<YamlNode> iterator = others.iterator();
                final Iterator<YamlNode> here = nodes.iterator();
                while(iterator.hasNext()) {
                    result = here.next().compareTo(iterator.next());
                    if(result != 0) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Indent this YamlSequence. This is a base method since indentation
     * logic should be identical for any kind of YamlSequence, regardless of
     * its implementation.
     *
     * Keep this method package-protected, it should NOT be visible to users.
     *
     * @param indentation Indentation to start with. Usually, it's 0, since we
     *  don't want to have spaces at the beginning. But in the case of nested
     *  YamlNodes, this value may be greater than 0.
     * @return String indented YamlSequence, by the specified indentation.
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
        for (final YamlNode node : this.values()) {
            print
                .append(alignment)
                .append("- ");
            if (node instanceof Scalar) {
                this.printScalar((Scalar) node, print, indentation);
            } else  {
                print
                    .append(newLine)
                    .append(((BaseYamlNode) node).indent(indentation + 2))
                    .append(newLine);
            }
        }
        String printed = print.toString();
        if(printed.length() > 0) {
            printed = printed.substring(0, printed.length() - 1);
        }
        return printed;
    }

    /**
     * Print a Scalar.
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
     * When printing a YamlSequence, just call this.indent(0), no need for
     * other wrappers.
     * @return This printed YamlSequence.
     */
    @Override
    public final String toString() {
        return this.indent(0);
    }
}
