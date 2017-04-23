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
import java.util.Iterator;

/**
 * AbstractYamlSequence implementing methods which should be the same across 
 * all final implementations of YamlSequence.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
abstract class AbstractYamlSequence extends AbstractYamlNode {
    
    /**
     * The number of Yaml elements (scalars, mappings and sequences) found in
     * this sequence.
     * @return Integer.
     */
    abstract int size();

    /**
     * Get the Yaml mapping  from the given index.
     * @param index Integer index.
     * @return Yaml mapping
     */
    abstract AbstractYamlMapping yamlMapping(final int index);

    /**
     * Get the Yaml sequence  from the given index.
     * @param index Integer index.
     * @return Yaml sequence
     */
    abstract AbstractYamlSequence yamlSequence(final int index);

    /**
     * Get the String from the given index.
     * @param index Integer index.
     * @return String
     */
    abstract String string(final int index);
    
    @Override
    public int hashCode() {
        int hash = 0;
        for(final AbstractYamlNode node : this.children()) {
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
        if (other == null || !(other instanceof AbstractYamlSequence)) {
            result = false;
        } else if (this == other) {
            result = true;
        } else {
            result = this.compareTo((AbstractYamlSequence) other) == 0;
        }
        return result;
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
     *  a value < 0 if this < o <br>
     *   0 if this == o or <br>
     *  a value > 0 if this > o
     */
    @Override
    public int compareTo(final AbstractYamlNode other) {
        int result = 0;
        if (other == null || other instanceof Scalar) {
            result = 1;
        } else if (other instanceof AbstractYamlMapping) {
            result = -1;
        } else if (this != other) {
            final Collection<AbstractYamlNode> nodes = this.children();
            nodes.hashCode();
            final Collection<AbstractYamlNode> others = other.children();
            if(nodes.size() > others.size()) {
                result = 1;
            } else if (nodes.size() < others.size()) {
                result = -1;
            } else {
                final Iterator<AbstractYamlNode> ietrator = others.iterator();
                final Iterator<AbstractYamlNode> here = nodes.iterator();
                while(ietrator.hasNext()) {
                    result = here.next().compareTo(ietrator.next());
                    if(result != 0) {
                        break;
                    }
                }
            }
        }
        return result;
    }

}
