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
import java.util.List;

/**
 * Unit tests for {@ling ReadPlainScalar}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.3
 */
public final class ReadPlainScalarTest {

    /**
     * ReadPlainScalar can return the scalar's value from a mapping line.
     */
    @Test
    public void returnsValueFromMappingLine() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("key: value", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("value"));
    }

    /**
     * ReadPlainScalar can return the scalar's unescaped value
     * from a mapping line.
     */
    @Test
    public void returnsUnescapedValueFromMappingLine() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("password: \"Password!@#\"", 0)
        );
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo("Password!@#")
        );
    }

    /**
     * ReadPlainScalar can return the scalar's null value from a mapping line.
     */
    @Test
    public void returnsNullValueFromMappingLine() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("key: null", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.nullValue());
    }

    /**
     * ReadPlainScalar can return the "null" String value from a mapping line,
     * because it is escaped.
     */
    @Test
    public void returnsEscapedNullStringFromMappingLine() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("key: 'null'", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("null"));
    }

    /**
     * ReadPlainScalar can return the scalar's value from a sequence line.
     */
    @Test
    public void returnsValueFromSequenceLine() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("- value", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("value"));
    }

    /**
     * ReadPlainScalar can return the scalar's null value from a sequence line.
     */
    @Test
    public void returnsNullValueFromSequenceLine() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("- null", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.nullValue());
    }

    /**
     * ReadPlainScalar can return the "null" String, because
     * it is escaped.
     */
    @Test
    public void returnsEscapedNullStringFromSequenceLine() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("- \"null\"", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("null"));
    }

    /**
     * ReadPlainScalar can return the comment referring to a scalar
     * in a sequence.
     */
    @Test
    public void returnsCommentFromSequenceLine() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- value1", 0));
        lines.add(new RtYamlLine("- value2 # Comment here", 1));
        lines.add(new RtYamlLine("- value3", 2));
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(lines), lines.get(1)
        );
        final Comment comment = scalar.comment();
        MatcherAssert.assertThat(
            comment.value(),
            Matchers.equalTo("Comment here")
        );
        MatcherAssert.assertThat(comment.yamlNode(), Matchers.is(scalar));
    }

    /**
     * ReadPlainScalar can return the comment referring to a scalar
     * in a mapping.
     */
    @Test
    public void returnsCommentFromMappingLine() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("key1: value1", 0));
        lines.add(new RtYamlLine("key2: value2 # Comment here", 1));
        lines.add(new RtYamlLine("key3: value3", 2));
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(lines), lines.get(1)
        );
        final Comment comment = scalar.comment();
        MatcherAssert.assertThat(
            comment.value(),
            Matchers.equalTo("Comment here")
        );
        MatcherAssert.assertThat(comment.yamlNode(), Matchers.is(scalar));
    }

    /**
     * ReadPlainScalar will return the plain scalar.
     */
    @Test
    public void returnsPlainScalar() {
        final YamlLine line = new RtYamlLine("value", 0);
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            line
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("value"));
    }

    /**
     * ReadPlainScalar will return the null scalar.
     */
    @Test
    public void returnsNullScalar() {
        final YamlLine line = new RtYamlLine("null", 0);
        final Scalar scalar = new ReadPlainScalar(
                new AllYamlLines(new ArrayList<>()),
                line
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.nullValue());
    }

    /**
     * ReadPlainScalar will return the "null" String scalar, not null,
     * because it is escaped.
     */
    @Test
    public void returnsEscapedNullScalar() {
        final YamlLine line = new RtYamlLine("'null'", 0);
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            line
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("null"));
    }

    /**
     * ReadPlainScalar will return empty string if the line is empty.
     * Should never be the case in real practice, however, because empty
     * lines are omitted when read.
     */
    @Test
    public void returnsEmptyScalar() {
        final YamlLine line = new RtYamlLine("", 0);
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            line
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.isEmptyString());
    }

    /**
     * Unit test for toString.
     */
    @Test
    public void toStringWorks() {
        final Scalar scalar = new ReadPlainScalar(
            new AllYamlLines(new ArrayList<>()),
            new RtYamlLine("key: value", 0)
        );
        MatcherAssert.assertThat(
            scalar.toString(),
            Matchers.equalTo(
            "---"
                + System.lineSeparator()
                + "value"
                + System.lineSeparator()
                + "..."
            )
        );
    }
}
