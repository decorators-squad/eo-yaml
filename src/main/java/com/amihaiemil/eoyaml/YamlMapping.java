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

import java.util.Set;

/**
 * A Yaml mapping.
 * @checkstyle ExecutableStatementCount (300 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlMapping extends YamlNode {

    /**
     * Get the Yaml mapping associated with the given key.
     * @param key String key
     * @return Yaml mapping or null if the key is missing, or not pointing
     *  to a mapping.
     */
    YamlMapping yamlMapping(final String key);

    /**
     * Get the Yaml mapping associated with the given key.
     * @param key Yaml node (mapping or sequence) key
     * @return Yaml mapping or null if the key is missing, or not pointing
     *  to a mapping.
     */
    YamlMapping yamlMapping(final YamlNode key);

    /**
     * Get the Yaml sequence associated with the given key.
     * @param key String key
     * @return Yaml sequence or null if the key is missing, or not pointing
     *  to a sequence.
     */
    YamlSequence yamlSequence(final String key);

    /**
     * Get the Yaml sequence associated with the given key.
     * @param key Yaml node (mapping or sequence) key
     * @return Yaml sequence or null if the key is missing, or not pointing
     *  to a sequence
     */
    YamlSequence yamlSequence(final YamlNode key);

    /**
     * Get the String associated with the given key.
     * @param key String key
     * @return String or null if the key is missing, or not pointing
     *  to a scalar.
     */
    String string(final String key);

    /**
     * Get the String associated with the given key.
     * @param key Yaml node (mapping or sequence) key
     * @return String or null if the key is missing, or not pointing
     *  to a scalar.
     */
    String string(final YamlNode key);

    /**
     * Get the YamlNode mapped to the specified key.
     * @param key YamlNode key. Could be a simple scalar,
     *  a YamlMapping or a YamlSequence.
     * @return The found YamlNode or null if nothing is found.
     */
    YamlNode value(final YamlNode key);
    
    /**
     * Return the keys' set of this mapping.<br><br>
     * <b>Pay attention: </b> the keys are ordered.
     * @return Set of YamlNode keys.
     */
    Set<YamlNode> keys();

    /**
     * Indent this YamlMapping. This is a default method since indentation
     * logic should be identical for any kind of YamlMapping, regardless of
     * its implementation.
     * @param indentation Indentation to start with. Usually, it's 0, since we
     *  don't want to have spaces at the beginning. But in the case of nested
     *  YamlNodes, this value may be > 0.
     * @return String indented YamlMapping, by the specified indentation.
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
        for(final YamlNode key : this.keys()) {
            print.append(indent);
            final YamlNode value = this.value(key);
            if(key instanceof Scalar) {
                print.append(key.toString()).append(": ");
                if (value instanceof Scalar) {
                    print.append(value.toString()).append("\n");
                } else  {
                    print
                        .append("\n")
                        .append(value.indent(indentation + 2))
                        .append("\n");
                }
            } else {
                print
                    .append("?\n")
                    .append(key.indent(indentation + 2)).append("\n")
                    .append(indent).append(":");
                if(value instanceof Scalar) {
                    print
                        .append(" ").append(value);
                } else {
                    print
                        .append("\n")
                        .append(value.indent(indentation + 2));
                }
                print.append("\n");
            }
        }
        String printed = print.toString();
        if(printed.length() > 0) {
            printed = printed.substring(0, printed.length() - 1);
        }
        return printed;
    }
}
