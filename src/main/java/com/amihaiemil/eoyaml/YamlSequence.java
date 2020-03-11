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

/**
 * A Yaml sequence.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlSequence extends YamlNode, Iterable<YamlNode> {

    /**
     * The number of Yaml elements (scalars, mappings and sequences) found in
     * this sequence.
     * @return Integer.
     */
    int size();

    /**
     * Get the Yaml mapping  from the given index.
     * @param index Integer index.
     * @return Yaml mapping
     */
    YamlMapping yamlMapping(final int index);

    /**
     * Get the Yaml sequence  from the given index.
     * @param index Integer index.
     * @return Yaml sequence
     */
    YamlSequence yamlSequence(final int index);

    /**
     * Get the String from the given index.
     * @param index Integer index.
     * @return String
     */
    String string(final int index);

    /**
     * Returns this YamlSequence's children Iterator.<br><br>
     * It is equivalent to YamlSequence.values().iterator().
     * @return Iterator of YamlNode.
     */
    Iterator<YamlNode> iterator();
    
    /**
     * Indent this YamlSequence. This is a default method since indentation
     * logic should be identical for any kind of YamlSequence, regardless of
     * its implementation.
     * @param indentation Indentation to start with. Usually, it's 0, since we
     *  don't want to have spaces at the beginning. But in the case of nested
     *  YamlNodes, this value may be greater than 0.
     * @return String indented YamlSequence, by the specified indentation.
     */
    default String indent(final int indentation) {
        if(indentation < 0) {
            throw new IllegalArgumentException(
                "Indentation level has to be >=0"
            );
        }
        final StringBuilder print = new StringBuilder();
        int spaces = indentation;
        final StringBuilder indent = new StringBuilder();
        while (spaces > 0) {
            indent.append(" ");
            spaces--;
        }
        for (final YamlNode node : this.values()) {
            print.append(indent)
                .append("- ");
            if (node instanceof Scalar) {
                print.append(node.toString()).append("\n");
            } else  {
                print.append("\n").append(node.indent(indentation + 2))
                    .append("\n");
            }
        }
        String printed = print.toString();
        if(printed.length() > 0) {
            printed = printed.substring(0, printed.length() - 1);
        }
        return printed;
    }
}
