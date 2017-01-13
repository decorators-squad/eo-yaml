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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * YAML sequence.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#sequence//
 */
final class Sequence extends AbstractNode{

    /**
     * Nodes in this sequence.
     */
    private final List<AbstractNode> nodes = 
        new LinkedList<AbstractNode>();

    /**
     * Ctor.
     * @param parent The parent node of this sequence.
     */
    Sequence(final AbstractNode parent) {
        super(parent);
    }

    @Override
    public Collection<AbstractNode> children() {
        final List<AbstractNode> children = new LinkedList<>();
        children.addAll(this.nodes);
        Collections.sort(children);
        return children;
    }

    /**
     * Compare this Sequence to another node.<br><br>
     * 
     * A Sequence is always considered greater than a Scalar and less than
     * a Mapping.<br>
     * 
     * If o is a Sequence, their integer lengths are compared - the one with
     * the greater length is considered greater. If the lengths are equal,
     * then the 2 Sequences are equal if all elements are equal. If the
     * elements are not identical, the comparison of the first unequal
     * elements is returned.
     * 
     * @param other The other AbstractNode.
     * @checkstyle NestedIfDepth (100 lines)
     * @return
     *  -1 if this < o <br>
     *   0 if this == o or <br>
     *   1 if this > o
     */
    @Override
    public int compareTo(final AbstractNode other) {
        int result = 0;
        if (other == null || other instanceof Scalar) {
            result = 1;
        } else if (other instanceof Mapping) {
            result = -1;
        } else {
            Sequence seq = (Sequence) other;
            if(this.nodes.size() > seq.nodes.size()) {
                result = 1;
            } else if (this.nodes.size() < seq.nodes.size()) {
                result = -1;
            } else {
                for (int i=0; i< this.nodes.size(); i++) {
                    result = this.nodes.get(i).compareTo(seq.nodes.get(i));
                    if(result != 0) {
                        break;
                    }
                }
            }
        }
        return result;
    }
}
