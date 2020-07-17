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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Unit tests for {@link FlowOnOneLine}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 6.0.0
 */
public final class FlowOnOneLineTestCase {

    /**
     * FlowOnOneLine can fold empty lines.
     */
    @Test
    public void foldsEmptyLines() {
        final YamlLine folded = new FlowOnOneLine(
            new FoldedFlowLines.Bracket(0, new RtYamlLine("[", 0)),
            new FoldedFlowLines.Bracket(0, new RtYamlLine("]", 1)),
            new AllYamlLines(new ArrayList<>())
        );
        MatcherAssert.assertThat(
            folded.trimmed(),
            Matchers.isEmptyString()
        );
    }

    /**
     * FlowOnOneLine can fold a single line.
     */
    @Test
    public void foldsOneLine() {
        final YamlLine original = new RtYamlLine("- [a, b, c]", 0);
        final YamlLine folded = new FlowOnOneLine(
            new FoldedFlowLines.Bracket(2, original),
            new FoldedFlowLines.Bracket(10, original),
            new AllYamlLines(Arrays.asList(original))
        );
        MatcherAssert.assertThat(
            folded.trimmed(),
            Matchers.equalTo("[a, b, c]")
        );
    }

    /**
     * FlowOnOneLine can fold only the first line
     * (the whole interval is there).
     */
    @Test
    public void foldsFirstLine() {
        final YamlLine first = new RtYamlLine("seq: [a, b, c]", 0);
        final YamlLine folded = new FlowOnOneLine(
            new FoldedFlowLines.Bracket(5, first),
            new FoldedFlowLines.Bracket(13, first),
            new AllYamlLines(
                Arrays.asList(
                    first,
                    new RtYamlLine("key: [value, ada]", 1),
                    new RtYamlLine("key2: value2", 2)
                )
            )
        );
        MatcherAssert.assertThat(
            folded.trimmed(),
            Matchers.equalTo("[a, b, c]")
        );
    }

    /**
     * FlowOnOneLine can fold a few lines if the interval spans on
     * more of them.
     */
    @Test
    public void foldsSomeLines() {
        final YamlLine first = new RtYamlLine("seq: [a, ", 0);
        final YamlLine last = new RtYamlLine("c]", 2);
        final YamlLine folded = new FlowOnOneLine(
            new FoldedFlowLines.Bracket(5, first),
            new FoldedFlowLines.Bracket(1, last),
            new AllYamlLines(
                Arrays.asList(
                    first,
                    new RtYamlLine("   b,", 1),
                    last,
                    new RtYamlLine("key: [value, ada]", 3),
                    new RtYamlLine("key2: value2", 4)
                )
            )
        );
        MatcherAssert.assertThat(
            folded.trimmed(),
            Matchers.equalTo("[a,b,c]")
        );
    }

    /**
     * FlowOnOneLine can fold all the lines, if the interval
     * spans on all of them.
     */
    @Test
    public void foldsAllLines() {
        final YamlLine first = new RtYamlLine("seq: [a, ", 0);
        final YamlLine last = new RtYamlLine("g]", 4);
        final YamlLine folded = new FlowOnOneLine(
            new FoldedFlowLines.Bracket(5, first),
            new FoldedFlowLines.Bracket(1, last),
            new AllYamlLines(
                Arrays.asList(
                    first,
                    new RtYamlLine("   b,", 1),
                    new RtYamlLine(" [c, d],", 2),
                    new RtYamlLine("{e: f},", 3),
                    last
                )
            )
        );
        MatcherAssert.assertThat(
            folded.trimmed(),
            Matchers.equalTo("[a,b,[c, d],{e: f},g]")
        );
    }

}
