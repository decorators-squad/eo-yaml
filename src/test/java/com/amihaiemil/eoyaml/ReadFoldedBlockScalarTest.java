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

import java.util.*;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link ReadFoldedBlockScalar}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.2
 */
public final class ReadFoldedBlockScalarTest {

    /**
     * ReadPointedScalar can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line", 1));
        lines.add(new RtYamlLine("Second Line", 2));
        lines.add(new RtYamlLine("Third Line", 3));
        final ReadFoldedBlockScalar scalar =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        RtYamlMapping map = new RtYamlMapping(new LinkedHashMap<>());
        MatcherAssert.assertThat(scalar.compareTo(map), Matchers.lessThan(0));
    }

    /**
     * ReadPointedScalar can compare itself to a Sequence.
     */
    @Test
    public void comparesToSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line", 1));
        lines.add(new RtYamlLine("Second Line", 2));
        lines.add(new RtYamlLine("Third Line", 3));
        final ReadFoldedBlockScalar scalar =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<YamlNode>());
        MatcherAssert.assertThat(scalar.compareTo(seq), Matchers.lessThan(0));
    }

    /**
     * ReadPointedScalar can compare itself to a Scalar.
     */
    @Test
    public void comparesToScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadFoldedBlockScalar pipeScalar =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        final PlainStringScalar scalar = new PlainStringScalar("Java");
        MatcherAssert.assertThat(pipeScalar.compareTo(scalar), Matchers.is(0));
    }

    /**
     * ReadPointerScalar can compare itself to other ReadPipeScalar.
     */
    @Test
    public void comparesToReadPipeScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadFoldedBlockScalar first =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        final ReadLiteralBlockScalar second =
            new ReadLiteralBlockScalar(new AllYamlLines(lines));
        MatcherAssert.assertThat(first.compareTo(second), Matchers.is(0));
    }

    /**
     * ReadPointerScalar can compare itself to other ReadPointedScalar.
     */
    @Test
    public void comparesToReadPointedScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadFoldedBlockScalar first =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        final ReadFoldedBlockScalar second =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        MatcherAssert.assertThat(first.compareTo(second), Matchers.is(0));
    }

    /**
     * ReadPointedScalar ignores first newline ("folded value"), and
     * returns the lines as a single sentence. See Example 2.14:
     * In the folded scalars, newlines become spaces
     */
    @Test
    public void returnsFoldedValue() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Mark McGwire's", 1));
        lines.add(new RtYamlLine("year was crippled", 2));
        lines.add(new RtYamlLine("by a knee injury.", 3));
        final ReadFoldedBlockScalar scalar =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.is("Mark McGwire's year was crippled by a knee injury.")
        );
    }

    /**
     * ReadFoldedBlockScalar can return the value referring to it.
     */
    @Test
    public void returnsComment() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("key: value", 0));
        lines.add(new RtYamlLine("# Folded scalar as value in map", 1));
        lines.add(new RtYamlLine("folded: >", 2));
        lines.add(new RtYamlLine("  Mark McGwire's", 3));
        lines.add(new RtYamlLine("  year was crippled", 4));
        lines.add(new RtYamlLine("  by a knee injury.", 5));
        final ReadFoldedBlockScalar scalar =
            new ReadFoldedBlockScalar(lines.get(2), new AllYamlLines(lines));
        final Comment comment = scalar.comment();
        MatcherAssert.assertThat(
            comment.value(),
            Matchers.equalTo("Folded scalar as value in map")
        );
        MatcherAssert.assertThat(comment.yamlNode(), Matchers.is(scalar));
    }

    /**
     * ReadPointedScalar works with Spec Example 2.15:
     * Folded newlines are preserved for "more indented"
     * and blank lines.
     */
    @Test
    public void preservesFoldedNewlinesAndBlankLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Sammy Sosa completed another", 1));
        lines.add(new RtYamlLine("fine season with great stats.", 2));
        lines.add(new RtYamlLine("", 3));
        lines.add(new RtYamlLine("  63 Home Runs", 4));
        lines.add(new RtYamlLine("  75 Hits", 5));
        lines.add(new RtYamlLine("", 6));
        lines.add(new RtYamlLine("What a year!", 7));
        final ReadFoldedBlockScalar scalar =
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.is(
                "Sammy Sosa completed another "
                + "fine season with great stats."
                + System.lineSeparator()
                + System.lineSeparator()
                + "  63 Home Runs"
                + System.lineSeparator()
                + "  75 Hits"
                + System.lineSeparator()
                + System.lineSeparator()
                + "What a year!"
            )
        );
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
            new ReadFoldedBlockScalar(new AllYamlLines(lines));
        System.out.print(scalar);
        MatcherAssert.assertThat(
            scalar.toString(),
            Matchers.equalTo(
            "---"
                + System.lineSeparator()
                + ">"
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
}
