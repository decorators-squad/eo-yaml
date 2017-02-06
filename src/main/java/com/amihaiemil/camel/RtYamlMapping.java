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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * YAML mapping implementation (rt means runtime).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#mapping//
 */
final class RtYamlMapping implements YamlMapping {

    /**
     * Key:value tree map (ordered keys).
     */
    private final Map<YamlNode, YamlNode> mappings =
        new TreeMap<YamlNode, YamlNode>();

    /**
     * Ctor.
     * @param entries Entries contained in this mapping.
     */
    RtYamlMapping(final Map<YamlNode, YamlNode> entries) {
        this.mappings.putAll(entries);
    }

    @Override
    public YamlMapping yamlMapping(final String key) {
        return this.yamlMapping(new Scalar(key));
    }

    @Override
    public YamlMapping yamlMapping(final YamlNode key) {
        final YamlNode value = this.mappings.get(key);
        final YamlMapping found;
        if (value != null && value instanceof YamlMapping) {
            found = (YamlMapping) value;
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public YamlSequence yamlSequence(final String key) {
        return this.yamlSequence(new Scalar(key));
    }

    @Override
    public YamlSequence yamlSequence(final YamlNode key) {
        final YamlNode value = this.mappings.get(key);
        final YamlSequence found;
        if (value != null && value instanceof YamlSequence) {
            found =  (YamlSequence) value;
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public String string(final String key) {
        return this.string(new Scalar(key));
    }

    @Override
    public String string(final YamlNode key) {
        final YamlNode value = this.mappings.get(key);
        final String found;
        if (value != null && value instanceof Scalar) {
            found = ((Scalar) value).value();
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public Collection<YamlNode> children() {
        return this.mappings.values();
    }

    /**
     * Compare this Mapping to another node.<br><br>
     * 
     * A Mapping is always considered greater than a Scalar or a Sequence.<br>
     * 
     * If o is a Mapping, their integer lengths are compared - the one with
     * the greater length is considered greater. If the lengths are equal,
     * then the 2 Mappings are equal if all elements are equal (K==K and V==V).
     * If the elements are not identical, the comparison of the first unequal
     * elements is returned.
     * 
     * @param other The other AbstractNode.
     * @checkstyle NestedIfDepth (100 lines)
     * @checkstyle ExecutableStatementCount (100 lines)
     * @return
     *  a value < 0 if this < o <br>
     *   0 if this == o or <br>
     *  a value > 0 if this > o
     */
    @Override
    public int compareTo(final YamlNode other) {
        int result = 0;
        if (other == null || !(other instanceof RtYamlMapping)) {
            result = 1;
        } else if (this != other) {
            RtYamlMapping map = (RtYamlMapping) other;
            if(this.mappings.size() > map.mappings.size()) {
                result = 1;
            } else if (this.mappings.size() < map.mappings.size()) {
                result = -1;
            } else {
                Iterator<Entry<YamlNode, YamlNode>> entries =
                    this.mappings.entrySet().iterator();
                Iterator<Entry<YamlNode, YamlNode>> others =
                    map.mappings.entrySet().iterator();
                int keys;
                int values;
                while(entries.hasNext()) {
                    Entry<YamlNode, YamlNode> entry = entries.next();
                    Entry<YamlNode, YamlNode> oEntry = others.next();
                    keys = entry.getKey().compareTo(oEntry.getKey());
                    values = entry.getValue().compareTo(oEntry.getValue());
                    if(keys != 0) {
                        result = keys;
                        break;
                    }
                    if(values != 0) {
                        result = values;
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.indent(0);
    }

    @Override
    public String indent(final int indentation) {
        StringBuilder print = new StringBuilder();
        int spaces = indentation;
        StringBuilder indent = new StringBuilder();
        while (spaces > 0) {
            indent.append(" ");
            spaces--;
        }
        for(final Map.Entry<YamlNode, YamlNode> entry
            : this.mappings.entrySet()
        ) {
            print.append(indent);
            YamlNode key = entry.getKey();
            YamlNode value = entry.getValue();
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
                print.append("?\n").append(key.indent(indentation + 2))
                    .append("\n").append(indent).append(":\n")
                    .append(value.indent(indentation + 2))
                    .append("\n");
            }
        }
        
        String printed = print.toString();
        printed = printed.substring(0, printed.length() - 1);
        return printed;
    }
}
