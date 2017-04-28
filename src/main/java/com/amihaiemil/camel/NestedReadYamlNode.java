/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
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
package com.amihaiemil.camel;

import java.util.Collection;

/**
 * Read YamlNode which nested after one line (indented with 2 or more spaces).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.2
 *
 */
final class NestedReadYamlNode implements YamlNode {

    /**
     * This YamlNode.
     */
    private YamlNode self;

    /**
     * Ctor.
     * @param prev Prev line.
     * @param lines Lines of this read yaml node.
     */
    NestedReadYamlNode(final YamlLine prev, final AbstractYamlLines lines) {
        this.self = NestedReadYamlNode.linesToNode(prev, lines);
    }
    
    @Override
    public int compareTo(final YamlNode other) {
        return this.self.compareTo(other);
    }

    @Override
    public Collection<YamlNode> children() {
        return this.self.children();
    }

    @Override
    public String indent(final int indentation) {
        return this.self.indent(indentation);
    }
    
    /**
     * Turn the given lines into an appropriate YamlNode, based
     * on the last character(s) of the previous line.
     * @todo #107:30m/DEV This method needs to be implemented. It will be
     *  similar to former AbstractYamlLines.toYamlNode(), but it will also
     *  cover the wrapper sequence/scalar cases. When this method is ready,
     *  the tests from {@link NestedReadYamlNodeTest} should be enabled.
     * @param prev Previous YamlLine
     * @param lines Lines of this nested YamlNode
     * @return YamlNode
     */
    private static YamlNode linesToNode(
        final YamlLine prev, final AbstractYamlLines lines
    ) {
        final String trimmed = prev.trimmed();
        final String last = trimmed.substring(trimmed.length()-1);
        final YamlNode node;
        switch (last) {
            case Nested.YAML:
                final boolean sequence = lines.iterator()
                    .next().trimmed().startsWith("-");
                if(sequence) {
                    node = new ReadYamlSequence(lines);
                } else {
                    node = new ReadYamlMapping(lines);
                }
                break;
            case Nested.KEY_YAML:
                final boolean sequenceKey = lines.iterator()
                    .next().trimmed().startsWith("-");
                if(sequenceKey) {
                    node = new ReadYamlSequence(lines);
                } else {
                    node = new ReadYamlMapping(lines);
                }
                break;
            case Nested.WRAPPED_SEQUENCE:
                node = new ReadYamlSequence(lines);
                break;
            case Nested.PIPED_SCALAR:
                node = new ReadPipeScalar(lines);
                break;
            case Nested.POINTED_SCALAR:
                node = new ReadPointedScalar(lines);
                break;
            default:
                throw new IllegalStateException(
                    "No nested Yaml node after line " + prev.number() + 
                    " which has [" + last + "] character at the end"
                );
        }
        return node;
    }

}
