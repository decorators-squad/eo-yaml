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
package com.amihaiemil.eoyaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link ReadPipeScalar}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.2
 */
public final class ReadPipeScalarTest {
    
    /**
     * ReadPipeScalar should not have children.
     */
    @Test
    public void hasNoChildren() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line.", 1));
        lines.add(new RtYamlLine("Second Line.", 2));
        lines.add(new RtYamlLine("Third Line.", 3));
        final ReadPipeScalar scalar = 
            new ReadPipeScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.children(), Matchers.emptyIterable()
        );
    }

    /**
     * ReadPipeScalar can return the value of input YamlLines.
     */
    @Test
    public void returnsValue() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line.", 1));
        lines.add(new RtYamlLine("Second Line.", 2));
        lines.add(new RtYamlLine("Third Line.", 3));
        final ReadPipeScalar scalar = 
            new ReadPipeScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.is("First Line.\nSecond Line.\nThird Line.")
        );
    }
    
    /**
     * ReadPipeScalar can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line.", 1));
        lines.add(new RtYamlLine("Second Line.", 2));
        lines.add(new RtYamlLine("Third Line.", 3));
        final ReadPipeScalar scalar = 
            new ReadPipeScalar(new RtYamlLines(lines));
        RtYamlMapping map = new RtYamlMapping(
            new HashMap<YamlNode, YamlNode>()
        );
        MatcherAssert.assertThat(scalar.compareTo(map), Matchers.lessThan(0));
    }

    /**
     * ReadPipeScalar can compare itself to a Sequence.
     */
    @Test
    public void comparesToSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line.", 1));
        lines.add(new RtYamlLine("Second Line.", 2));
        lines.add(new RtYamlLine("Third Line.", 3));
        final ReadPipeScalar scalar = 
            new ReadPipeScalar(new RtYamlLines(lines));
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<YamlNode>());
        MatcherAssert.assertThat(scalar.compareTo(seq), Matchers.lessThan(0));
    }
    
    /**
     * ReadPipeScalar can compare itself to a Scalar.
     */
    @Test
    public void comparesToScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadPipeScalar pipeScalar = 
            new ReadPipeScalar(new RtYamlLines(lines));
        final Scalar scalar = new Scalar("Java");
        MatcherAssert.assertThat(pipeScalar.compareTo(scalar), Matchers.is(0));
    }
    
    /**
     * ReadPipeScalar can compare itself to other ReadPipeScalar.
     */
    @Test
    public void comparesToReadPipeScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadPipeScalar first = 
            new ReadPipeScalar(new RtYamlLines(lines));
        final ReadPipeScalar second = 
            new ReadPipeScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(first.compareTo(second), Matchers.is(0));
    }
    
    /**
     * ReadPipeScalar can indent first line.
     */
    @Test
    public void indentsValue() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line.", 1));
        lines.add(new RtYamlLine("Second Line.", 2));
        lines.add(new RtYamlLine("Third Line.", 3));
        final ReadPipeScalar scalar = 
            new ReadPipeScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.indent(4),
            Matchers.is("    First Line.\n    Second Line.\n    Third Line.")
        );
    }
}
