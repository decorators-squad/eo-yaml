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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for {@link ReadPointedScalar}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.2
 * @todo #109:30m/DEV Implement a new iterator() for YamlLines to fix
 *  different indentations test as indentsValueWithDifferentIndentation() test.
 */
public final class ReadPointedScalarTest {

    /**
     * ReadPointedScalar should not have children.
     */
    @Test
    public void hasNoChildren() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line.", 1));
        lines.add(new RtYamlLine("Second Line.", 2));
        lines.add(new RtYamlLine("Third Line.", 3));
        final ReadPointedScalar scalar =
            new ReadPointedScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.children(), Matchers.emptyIterable()
        );
    }

    /**
     * ReadPointedScalar can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("First Line", 1));
        lines.add(new RtYamlLine("Second Line", 2));
        lines.add(new RtYamlLine("Third Line", 3));
        final ReadPointedScalar scalar =
            new ReadPointedScalar(new RtYamlLines(lines));
        RtYamlMapping map = new RtYamlMapping(
            new HashMap<YamlNode, YamlNode>()
        );
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
        final ReadPointedScalar scalar =
            new ReadPointedScalar(new RtYamlLines(lines));
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
        final ReadPointedScalar pipeScalar =
            new ReadPointedScalar(new RtYamlLines(lines));
        final Scalar scalar = new Scalar("Java");
        MatcherAssert.assertThat(pipeScalar.compareTo(scalar), Matchers.is(0));
    }

    /**
     * ReadPointerScalar can compare itself to other ReadPipeScalar.
     */
    @Test
    public void comparesToReadPipeScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadPointedScalar first =
            new ReadPointedScalar(new RtYamlLines(lines));
        final ReadPipeScalar second =
            new ReadPipeScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(first.compareTo(second), Matchers.is(0));
    }

    /**
     * ReadPointerScalar can compare itself to other ReadPointedScalar.
     */
    @Test
    public void comparesToReadPointedScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Java", 1));
        final ReadPointedScalar first =
            new ReadPointedScalar(new RtYamlLines(lines));
        final ReadPointedScalar second =
            new ReadPointedScalar(new RtYamlLines(lines));
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
        final ReadPointedScalar scalar =
            new ReadPointedScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.is("Mark McGwire's year was crippled by a knee injury.")
        );
    }

    /**
     * ReadPointedScalar can return value when line has different indentation.
     */
    @Test
    //@Ignore
    public void returnsValueWithSeveralIndentation() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Sammy Sosa completed another", 1));
        lines.add(new RtYamlLine("fine season with great stats.", 2));
        lines.add(new RtYamlLine("\n", 3));
        lines.add(new RtYamlLine("  63 Home Runs", 4));
        lines.add(new RtYamlLine("  75 Hits", 5));
        lines.add(new RtYamlLine("\n", 6));
        lines.add(new RtYamlLine("What a year!", 7));
        final ReadPointedScalar scalar =
            new ReadPointedScalar(new RtYamlLines(lines, Boolean.FALSE));
        System.out.println(scalar);
        System.out.println("----");
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.is("Sammy Sosa completed another fine season with great stats.\n\n  63 Home Runs\n  75 Hits\n\nWhat a year!"
            )
//            Matchers.is("Sammy Sosa completed another"
//                + " fine season with great stats.\n"
//                + "\n  63 Home Runs\nWhat a year!"
//            )
        );
    }
    /**
     * ReadPointedScalar can indent lines with no indentation.
     */
    @Test
    public void indentsValueWithNoIndentation() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Mark McGwire's", 1));
        lines.add(new RtYamlLine("year was crippled", 2));
        lines.add(new RtYamlLine("by an injury.", 3));
        final ReadPointedScalar scalar =
            new ReadPointedScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.indent(4),
            Matchers.is("    Mark McGwire's year was crippled by an injury.")
        );
    }

    /**
     * ReadPointedScalar can indent lines with different indentation.
     */
    @Test
    //@Ignore
    public void indentsValueWithDifferentIndentation() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("Sammy Sosa completed another", 1));
        lines.add(new RtYamlLine("fine season with great stats.", 2));
        lines.add(new RtYamlLine("\n", 3));
        lines.add(new RtYamlLine("  63 Home Runs", 4));
        lines.add(new RtYamlLine("What a year!", 5));
        final ReadPointedScalar scalar =
            new ReadPointedScalar(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            scalar.indent(4),
            Matchers.is("    Sammy Sosa completed another"
                + " fine season with great stats.\n"
                + "\n      63 Home Runs\n    What a year!"
            )
        );
    }
}
