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
import java.util.LinkedList;

/**
 * YAML scalar.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#scalar//
 */
final class Scalar implements YamlNode {

    /**
     * This scalar's value.
     */
    private String value;

    /**
     * Ctor.
     * @param value Given value for this scalar.
     */
    Scalar(final String value) {
        this.value = value;
    }

    /**
     * Value of this scalar.
     * @return Value of type T.
     */
    public String value() {
        return this.value;
    }

    @Override
    public Collection<YamlNode> children() {
        return new LinkedList<YamlNode>();
    }

    /**
     * Equality of two objects.
     * @param anotherObject Reference to righthand object
     * @return True if object are equal and False if are not.
     */
    @Override
    public boolean equals(final Object anotherObject) {
        if (this == anotherObject) {
            return true;
        }
        if (anotherObject == null || getClass() != anotherObject.getClass()) {
            return false;
        }

        final Scalar scalar = (Scalar) anotherObject;

        return this.value.equals(scalar.value);

    }

    /**
     * Hash Code of this scalar.
     * @return Value of hashCode() of type int.
     */
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    /**
     * Compare this Scalar to another node.<br><br>
     * 
     * A Scalar is always considered less than a Sequence or a Mapping.<br>
     * If o is Scalar then their String values are compared lexicographically
     * 
     * @param other The other AbstractNode.
     * @return
     *  a value < 0 if this < o <br>
     *   0 if this == o or <br>
     *  a value > 0 if this > o
     */
    @Override
    public int compareTo(final YamlNode other) {
        int result = -1;
        if (this == other) {
            result = 0;
        } else if (other == null) {
            result = 1;
        } else if (other instanceof Scalar) {
            result = this.value.compareTo(((Scalar) other).value);
        }
        return result;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
