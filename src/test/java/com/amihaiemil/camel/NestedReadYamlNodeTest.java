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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for {@link NestedReadYamlNode}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.2
 *
 */
public final class NestedReadYamlNodeTest {

    /**
     * NestedReadYamlNode can compare itself with an identical Yaml.
     */
    @Test
    @Ignore
    public void comparesToSameYaml() {
        final YamlLine prev = new RtYamlLine("key: ", 0);
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("  nexted: map", 1));
        lines.add(new RtYamlLine("  some: value", 2));
        final YamlNode nested = new NestedReadYamlNode(
            prev, new RtYamlLines(lines)
        );
        final YamlMapping map = new ReadYamlMapping(new RtYamlLines(lines));
        MatcherAssert.assertThat(nested.compareTo(map), Matchers.is(0));
    }
    
    /**
     * NestedReadYamlNode (map) can compare itself with a Yaml sequence.
     */
    @Test
    @Ignore
    public void mapComparesToSequence() {
        final YamlLine prev = new RtYamlLine("key: ", 0);
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("  nexted: map", 1));
        lines.add(new RtYamlLine("  some: value", 2));
        final YamlNode nested = new NestedReadYamlNode(
            prev, new RtYamlLines(lines)
        );
        final YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("some").add("sequence").add("values").build();
        MatcherAssert.assertThat(
            nested.compareTo(sequence), Matchers.greaterThan(0)
        );
    }

    /**
     * NestedReadYamlNode (sequence) can compare itself with a Yaml mapping.
     */
    @Test
    @Ignore
    public void sequenceComparesToMap() {
        final YamlLine prev = new RtYamlLine("key: ", 0);
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("  - sequecne", 1));
        lines.add(new RtYamlLine("  - values", 2));
        final YamlNode nested = new NestedReadYamlNode(
            prev, new RtYamlLines(lines)
        );
        final YamlMapping mapping = Yaml.createYamlMappingBuilder()
            .add("some", "map")
            .build();
        MatcherAssert.assertThat(
            nested.compareTo(mapping), Matchers.lessThan(0)
        );
    }

    /**
     * NestedReadYamlNode (wrapped sequence) can return its children.
     */
    @Test
    @Ignore
    public void wrappedSequenceChildren() {
        final YamlLine prev = new RtYamlLine("key: |-", 0);
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("  wrapped", 1));
        lines.add(new RtYamlLine("  sequence", 2));
        final YamlNode nested = new NestedReadYamlNode(
            prev, new RtYamlLines(lines)
        );
        final Collection<YamlNode> children = nested.children();        
        MatcherAssert.assertThat(children.size(), Matchers.is(2));
        MatcherAssert.assertThat(
            children.iterator().next().equals(new Scalar("wrapped")),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            children.iterator().next().equals(new Scalar("sequence")),
            Matchers.is(true)
        );
    }

    /**
     * NestedReadYamlNode can return its children.
     */
    @Test
    @Ignore
    public void returnsChildren() {
        final YamlLine prev = new RtYamlLine("key: ", 0);
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("  nexted: map", 1));
        lines.add(new RtYamlLine("  some: value", 2));
        final YamlNode nested = new NestedReadYamlNode(
            prev, new RtYamlLines(lines)
        );
        final Collection<YamlNode> children = nested.children();        
        MatcherAssert.assertThat(children.size(), Matchers.is(2));
        MatcherAssert.assertThat(
            children.iterator().next().equals(new Scalar("map")),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            children.iterator().next().equals(new Scalar("value")),
            Matchers.is(true)
        );

    }
}
