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
import java.util.LinkedList;

/**
 * YAML Plain scalar to be used when building a YAML, via
 * one of the builders.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#scalar//
 */
final class BuiltPlainScalar extends ComparableScalar {

    /**
     * This scalar's value.
     */
    private String value;

    /**
     * Ctor.
     * @param value Given value for this scalar.
     */
    BuiltPlainScalar(final String value) {
        this.value = value;
    }

    /**
     * Value of this scalar.
     * @return Value of type T.
     */
    @Override
    public String value() {
        return this.value;
    }

    @Override
    public Collection<YamlNode> values() {
        return new LinkedList<YamlNode>();
    }

    @Override
    public String toString() {
        return this.indent(0);
    }

    @Override
    public String indent(final int indentation) {
        int spaces = indentation;
        StringBuilder printed = new StringBuilder();
        while (spaces > 0) {
            printed.append(" ");
            spaces--;
        }
        return printed.append(this.value).toString();
    }

}
