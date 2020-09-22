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
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link ReadComment}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
public final class ReadCommentTest {

    /**
     * ReadComment can return the YamlNode that it refers to.
     */
    @Test
    public void returnsYamlNode() {
        final YamlNode node = Mockito.mock(YamlNode.class);
        MatcherAssert.assertThat(
            new ReadComment(
                new AllYamlLines(new ArrayList<>()), node
            ).yamlNode(),
            Matchers.is(node)
        );
    }

    /**
     * ReadComment can return the empty comment from empty yaml lines.
     */
    @Test
    public void returnsValueFromEmptyLines() {
        Comment readComment = new ReadComment(
            new AllYamlLines(new ArrayList<>()),
            Mockito.mock(YamlNode.class)
        );
        MatcherAssert.assertThat(
            readComment.value(),
            Matchers.isEmptyString()
        );
    }

    /**
     * ReadComment can return the comment from a single YamlLine.
     */
    @Test
    public void returnsValueFromSingleLine() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("# comment line", 0));
        Comment readComment = new ReadComment(
            new AllYamlLines(lines),
            Mockito.mock(YamlNode.class)
        );
        MatcherAssert.assertThat(
            readComment.value(),
            Matchers.equalTo("comment line")
        );
    }

    /**
     * ReadComment can return a comment spanning multiple lines.
     */
    @Test
    public void returnsMultiLineComment() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("# comment", 0));
        lines.add(new RtYamlLine("# on multiple", 1));
        lines.add(new RtYamlLine("# lines", 2));
        final StringBuilder expected = new StringBuilder();
        expected
            .append("comment").append(System.lineSeparator())
            .append("on multiple").append(System.lineSeparator())
            .append("lines");
        Comment readComment = new ReadComment(
            new AllYamlLines(lines),
            Mockito.mock(YamlNode.class)
        );
        MatcherAssert.assertThat(
            readComment.value(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * ReadComment can return a comment spanning multiple lines, that
     * also has empty lines.
     */
    @Test
    public void returnsMultiLineCommentWithEmptyLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("# comment", 0));
        lines.add(new RtYamlLine("# ", 1));
        lines.add(new RtYamlLine("# on multiple", 2));
        lines.add(new RtYamlLine("# ", 2));
        lines.add(new RtYamlLine("# lines", 3));
        final StringBuilder expected = new StringBuilder();
        expected
            .append("comment").append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("on multiple").append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("lines");
        Comment readComment = new ReadComment(
            new AllYamlLines(lines),
            Mockito.mock(YamlNode.class)
        );
        MatcherAssert.assertThat(
            readComment.value(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * ReadComment can return a comment starting on a different line.
     */
    @Test
    public void singleLineCommentStartingOnTheSecondLine() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("# comment", 1));
        final StringBuilder expected = new StringBuilder();
        expected.append("comment");
        Comment readComment = new ReadComment(
            new AllYamlLines(lines),
            Mockito.mock(YamlNode.class)
        );
        MatcherAssert.assertThat(
            readComment.value(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * ReadComment can return an empty string if the comment
     * line is empty.
     */
    @Test
    public void returnsValueFromEmptySingleComment() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("#", 0));
        MatcherAssert.assertThat(
            new ReadComment(
                new AllYamlLines(lines),
                Mockito.mock(YamlNode.class)
            ).value(),
            Matchers.isEmptyString()
        );
    }

}
