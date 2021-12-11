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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link ReadLiteralBlockScalar}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.2
 */
public final class ReadLiteralBlockScalarTest {

    /**
     * ReadPipeScalar can return the value of input YamlLines.
     */
    @Test
    public void returnsValue() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line.", 1));
        lines.add(new RtYamlLine("Second Line.", 2));
        lines.add(new RtYamlLine("Third Line.", 3));
        final ReadLiteralBlockScalar scalar =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.is(
            "First Line." + System.lineSeparator()
                + "Second Line." + System.lineSeparator()
                + "Third Line." + System.lineSeparator()
            )
        );
    }

    /**
     * ReadLiteralBlockScalar can return the value referring to it.
     */
    @Test
    public void returnsComment() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("key: value", 0));
        lines.add(new RtYamlLine("# Literal scalar as value in map", 1));
        lines.add(new RtYamlLine("literal: |", 2));
        lines.add(new RtYamlLine("  line 1", 3));
        lines.add(new RtYamlLine("  line 2", 4));
        lines.add(new RtYamlLine("  line 3", 5));
        final ReadLiteralBlockScalar scalar =
            new ReadLiteralBlockScalar(lines.get(2), new AllYamlLines(lines));
        final Comment comment = scalar.comment();
        MatcherAssert.assertThat(
            comment.value(),
            Matchers.equalTo("Literal scalar as value in map")
        );
        MatcherAssert.assertThat(comment.yamlNode(), Matchers.is(scalar));
    }

    /**
     * ReadLiteralBlockScalar can return properly indented values.
     */
    @Test
    public void handlesIndenting() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("literal: |", 0));
        lines.add(new RtYamlLine("  line 1", 1));
        lines.add(new RtYamlLine("   line 2", 2));
        lines.add(new RtYamlLine("  end", 3));
        final ReadLiteralBlockScalar scalar =
            new ReadLiteralBlockScalar(lines.get(0), new AllYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo("line 1\n line 2\nend\n")
        );
    }

    /**
     * ReadLiteralBlockScalar can return properly indented
     * values and trailing spaces are preserved.
     */
    @Test
    public void handlesTrailingSpaces() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("literal: |", 0));
        lines.add(new RtYamlLine("  trailing spaces   ", 1));
        lines.add(new RtYamlLine("  trailing tab\t", 2));
        final ReadLiteralBlockScalar scalar =
            new ReadLiteralBlockScalar(lines.get(0), new AllYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo("trailing spaces   \ntrailing tab\t\n")
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
        final ReadLiteralBlockScalar scalar =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        RtYamlMapping map = new RtYamlMapping(new LinkedHashMap<>());
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
        final ReadLiteralBlockScalar scalar =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<>());
        MatcherAssert.assertThat(scalar.compareTo(seq), Matchers.lessThan(0));
    }

    /**
     * ReadPipeScalar can compare itself to a Scalar.
     */
    @Test
    public void comparesToScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadLiteralBlockScalar pipeScalar =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        final PlainStringScalar scalar = new PlainStringScalar("Java\n");
        MatcherAssert.assertThat(pipeScalar.compareTo(scalar), Matchers.is(0));
    }

    /**
     * ReadPipeScalar can compare itself to other ReadPipeScalar.
     */
    @Test
    public void comparesToReadPipeScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadLiteralBlockScalar first =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        final ReadLiteralBlockScalar second =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        MatcherAssert.assertThat(first.compareTo(second), Matchers.is(0));
    }


    /**
     * Method toString should print it as a valid YAML document.
     */
    @Test
    public void toStringPrintsYaml() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line", 0));
        lines.add(new RtYamlLine("Second Line", 1));
        lines.add(new RtYamlLine("Third Line", 2));
        final Scalar scalar =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.toString(),
            Matchers.equalTo(
            "---"
                + System.lineSeparator()
                + "|"
                + System.lineSeparator()
                + "  First Line"
                + System.lineSeparator()
                + "  Second Line"
                + System.lineSeparator()
                + "  Third Line"
                + System.lineSeparator()
                + "..."
            )
        );
    }

    /**
     * Prints a block scalar from string.
     * <br/>
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/480">
     *     See bug</a>
     * @throws IOException If something is wrong.
     */
    @Test
    public void printsLiteralBlockScalarFromString() throws IOException {
        final String input = readTestResource("issue_480_bug_printing.yml");

        YamlMapping mapping = Yaml.createYamlInput(input, true)
            .readYamlMapping();
        MatcherAssert.assertThat(
            mapping.string("test1"),
            Matchers.equalTo("  this is a test message 1\r\n")
        );
        MatcherAssert.assertThat(
            mapping.string("test2"),
            Matchers.equalTo("  this is a test message 2\r\n")
        );
    }

    /**
     * Read a test resource file's contents.
     * @param fileName File to read.
     * @return File's contents as String.
     * @throws FileNotFoundException If something is wrong.
     * @throws IOException If something is wrong.
     */
    private String readTestResource(final String fileName)
        throws FileNotFoundException, IOException {
        return new String(
            IOUtils.toByteArray(
                new FileInputStream("src/test/resources/" + fileName)
            )
        );
    }
}
