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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * YamlSequenceBuilder implementation. "Rt" stands for "Runtime".
 * This class is immutable and thread-safe.
 * @author Salavat.Yalalov (s.yalalov@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class RtYamlSequenceBuilder implements YamlSequenceBuilder {
    /**
     * Added nodes.
     */
    private final List<YamlNode> nodes;

    /**
     * Default ctor.
     */
    RtYamlSequenceBuilder() {
        this(new ArrayList<>());
    }

    /**
     * Constructor.
     * @param nodes Nodes used in building the YamlSequence
     */
    RtYamlSequenceBuilder(final List<YamlNode> nodes) {
        this.nodes = Collections.synchronizedList(nodes); // List is now thread-safe
    }

    @Override
    public int size() {
        return this.nodes.size();
    }

    @Override
    public boolean isEmpty() {
        return this.nodes.isEmpty();
    }

    @Override
    public YamlSequenceBuilder add(final String value) {
        return this.add(new PlainStringScalar(value));
    }

    @Override
    public YamlSequenceBuilder add(String value, String inlineComment) {
        return this.add(new PlainStringScalar(value, inlineComment));
    }

    @Override
    public YamlSequenceBuilder add(final YamlNode node) {
        nodes.add(node);
        return this;
    }

    @Override
    public YamlSequenceBuilder add(String value, int index) {
        return this.add(new PlainStringScalar(value), index);
    }

    @Override
    public YamlSequenceBuilder add(String value, String inlineComment, int index) {
        return this.add(new PlainStringScalar(value, inlineComment), index);
    }

    @Override
    public YamlSequenceBuilder add(YamlNode node, int index) {
        nodes.add(index, node);
        return this;
    }

    @Override
    public YamlNode remove(int index) {
        return this.nodes.remove(index);
    }

    @Override
    public YamlSequence build(final String comment) {
        YamlSequence sequence = new RtYamlSequence(this.nodes, comment);
        if (this.nodes.isEmpty()) {
            sequence = new EmptyYamlSequence(sequence);
        }
        return sequence;
    }
}
