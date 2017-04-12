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
 * Read Yaml Scalar when previous yaml line ends with '|' character.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.2
 *
 */
final class ReadPipeScalar implements YamlNode {

    /**
     * Value of the wrapped scalar.
     */
    private String value;
    
    /**
     * Ctor.
     * @param lines Given lines to represent.
     */
    ReadPipeScalar(final AbstractYamlLines lines) {
        StringBuilder builder = new StringBuilder();
        for(final YamlLine line: lines) {
            builder.append(line.trimmed());
            builder.append('\n');
        }
        builder.delete(builder.length()-1, builder.length());
        this.value = builder.toString();
    }
   
    @Override
    public int compareTo(final YamlNode other) {
        int result = -1;
        if (this == other) {
            result = 0;
        } else if (other == null) {
            result = 1;
        } else if (other instanceof Scalar) {
            result = this.value.compareTo(((Scalar) other).value());
        } else if (other instanceof ReadPipeScalar) {
            result = this.value.compareTo(((ReadPipeScalar) other).value);
        }
        return result;
    }

    @Override
    public Collection<YamlNode> children() {
        return new LinkedList<YamlNode>();
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
    
    /**
     * Value of this scalar.
     * @return String
     */
    public String value() {
        return this.value;
    }

}
