/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link CollapsedYamlLine}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @checkstyle ExecutableStatementCount (1000 lines)
 * @since 8.0.0
 */
public final class CollapsedYamlLineTestCase {

    /**
     * The number of the CollapsedYamlLine should be the number of the
     * first line.
     */
    @Test
    public void returnsNumberOfFirstLine() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("flowSeq: [a,", 4));
        lines.add(new RtYamlLine("b,c,", 5));
        lines.add(new RtYamlLine("d]", 6));
        MatcherAssert.assertThat(
            new CollapsedYamlLine(lines).number(),
            Matchers.is(4)
        );
    }

    /**
     * The number of the CollapsedYamlLine should be -1 if there are no lines.
     */
    @Test
    public void returnsNegativeNumberIfNoLines() {
        MatcherAssert.assertThat(
            new CollapsedYamlLine(new ArrayList<>()).number(),
            Matchers.is(-1)
        );
    }

    /**
     * The indentation of the CollapsedYamlLine should be the number of the
     * first line.
     */
    @Test
    public void returnsIndentationOfFirstLine() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("  - flowSeq: [a,", 4));
        lines.add(new RtYamlLine("      b,c,", 5));
        lines.add(new RtYamlLine("      d]", 6));
        MatcherAssert.assertThat(
            new CollapsedYamlLine(lines).indentation(),
            Matchers.is(2)
        );
    }

    /**
     * The indentation of the CollapsedYamlLine should be -1 if there are no
     * lines.
     */
    @Test
    public void returnsNegativeIndentationIfNoLines() {
        MatcherAssert.assertThat(
            new CollapsedYamlLine(new ArrayList<>()).indentation(),
            Matchers.is(-1)
        );
    }

    /**
     * The value of the CollapsedYamlLine should be all the values of all
     * the lines concatenated with spaces.
     */
    @Test
    public void returnsCollapsedValue() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("flowSeq: [a,", 4));
        lines.add(new RtYamlLine("b,c,", 5));
        lines.add(new RtYamlLine("d]", 6));
        MatcherAssert.assertThat(
            new CollapsedYamlLine(lines).value(),
            Matchers.is("flowSeq: [a, b,c, d]")
        );
    }

    /**
     * The value of the CollapsedYamlLine is identical to the one
     * of the encapsulated, if there is a single line.
     */
    @Test
    public void returnsCollapsedValueOneLine() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("flowSeq: [a, b, c]", 0));
        MatcherAssert.assertThat(
            new CollapsedYamlLine(lines).value(),
            Matchers.is("flowSeq: [a, b, c]")
        );
    }

    /**
     * The value of the CollapsedYamlLine should be empty if there are
     * no lines.
     */
    @Test
    public void returnsEmptyValueIfNoLines() {
        MatcherAssert.assertThat(
            new CollapsedYamlLine(new ArrayList<>()).value(),
            Matchers.is("")
        );
    }
}
