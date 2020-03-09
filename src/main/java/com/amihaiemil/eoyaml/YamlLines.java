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

/**
 * Iterable yaml lines.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
interface YamlLines extends Iterable<YamlLine> {

    /**
     * Lines which are nested after the given YamlLine (lines which are
     * <br> indented by 2 or more spaces beneath it).
     * @param after Number of a YamlLine
     * @return YamlLines
     */
    YamlLines nested(final int after);

    /**
     * Number of lines.
     * @return Integer.
     */
    int count();

    /**
     * Indent these lines.
     * @param indentation Spaces to precede each line.
     * @return String with the pretty-printed, indented lines.
     */
    String indent(int indentation);

    /**
     * Turn these lines into a YamlNode.
     * @param prev Previous YamlLine
     * @return YamlNode
     * @todo #107:30min/DEV Add more tests to cover all the nested node
     *  possibilities.
     */
    default YamlNode toYamlNode(final YamlLine prev) {
        final String trimmed = prev.trimmed();
        final String last = trimmed.substring(trimmed.length()-1);
        final YamlNode node;
        switch (last) {
            case Nested.YAML:
                final boolean sequence = this.iterator()
                    .next().trimmed().startsWith("-");
                if(sequence) {
                    node = new ReadYamlSequence(this);
                } else {
                    node = new ReadYamlMapping(this);
                }
                break;
            case Nested.KEY_YAML:
                final boolean sequenceKey = this.iterator()
                    .next().trimmed().startsWith("-");
                if(sequenceKey) {
                    node = new ReadYamlSequence(this);
                } else {
                    node = new ReadYamlMapping(this);
                }
                break;
            case Nested.SEQUENCE:
                if(trimmed.length() == 1) {
                    final boolean elementSequence = this.iterator()
                        .next().trimmed().startsWith("-");
                    if(elementSequence) {
                        node = new ReadYamlSequence(this);
                    } else {
                        node = new ReadYamlMapping(this);
                    }
                } else {
                    node = new ReadYamlSequence(this);
                }
                break;
            case Nested.PIPED_SCALAR:
                node = new ReadPipeScalar(this);
                break;
            case Nested.POINTED_SCALAR:
                node = new ReadPointedScalar(this);
                break;
            default:
                throw new IllegalStateException(
                    "No nested Yaml node after line " + prev.number()
                    + " which has [" + last + "] character at the end"
                );
        }
        return node;
    }

}
