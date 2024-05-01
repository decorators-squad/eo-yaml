/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
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
 * YAML Visitor. Use this interface to implement your own YamlVisitor, to visit
 * all the nodes of a given YamlNode (sequence, mapping, stream etc). See class
 * {@link com.amihaiemil.eoyaml.YamlPrintVisitor} for an example.
 * @param <T> The return type of each visit.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 7.2.0
 */
public interface YamlVisitor<T> {

    /**
     * Visit a YAML Node.
     * @param node YamlNode, never null.
     * @return T returned type.
     */
    default T visitYamlNode(final YamlNode node) {
        T result;
        if (node instanceof Scalar) {
            result = this.visitScalar((Scalar) node);
        } else if (node instanceof YamlSequence) {
            result = this.visitYamlSequence((YamlSequence) node);
        } else if (node instanceof YamlMapping) {
            result = this.visitYamlMapping((YamlMapping) node);
        } else if (node instanceof YamlStream) {
            result = this.visitYamlStream((YamlStream) node);
        } else {
            result = this.visitChildren(node);
        }
        return result;
    }

    /**
     * Visit a Scalar.
     * @param node Scalar, never null.
     * @return T returned type.
     */
    default T visitScalar(final Scalar node) {
        return this.visitChildren(node);
    }

    /**
     * Visit a YamlMapping.
     * @param node YamlMapping, never null.
     * @return T returned type.
     */
    default T visitYamlMapping(final YamlMapping node) {
        return this.visitChildren(node);
    }

    /**
     * Visit a YamlSequence.
     * @param node YamlSequence, never null.
     * @return T returned type.
     */
    default T visitYamlSequence(final YamlSequence node) {
        return this.visitChildren(node);
    }

    /**
     * Visit a YamlStream.
     * @param node YamlStream, never null.
     * @return T returned type.
     */
    default T visitYamlStream(final YamlStream node) {
        return this.visitChildren(node);
    }

    /**
     * Visit the children of a given YamlNode.
     * @param node YamlNode, never null.
     * @return T returned type.
     */
    default T visitChildren(final YamlNode node) {
        T result = defaultResult();
        if(node != null) {
            for (final YamlNode child : node.children()) {
                if(child != null) {
                    T childResult = child.accept(this);
                    result = aggregateResult(result, childResult);
                }
            }
        }
        return result;
    }

    /**
     * Default result.
     * @return T returned type.
     */
    T defaultResult();

    /**
     * Aggregate results.
     * @param aggregate Previously aggregated results.
     * @param nextResult Next result.
     * @return T returned type.
     */
    T aggregateResult(final T aggregate, final T nextResult);
}
