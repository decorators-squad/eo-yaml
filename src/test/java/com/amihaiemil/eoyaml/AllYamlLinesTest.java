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

import com.amihaiemil.eoyaml.exceptions.YamlReadingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link AllYamlLines}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @sinve 1.0.0
 */
public final class AllYamlLinesTest {

    /**
     * RtYamlLines can iterate over the lines properly.
     * It should iterate only over the lines which are at the
     * same indentation level.
     */
    @Test
    public void iteratesRight() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: ", 0));
        lines.add(new RtYamlLine("  - fourth", 1));
        lines.add(new RtYamlLine("  - fifth", 2));
        lines.add(new RtYamlLine("second: something", 3));
        lines.add(new RtYamlLine("third: somethingElse", 4));
        final Iterator<YamlLine> iterator = new AllYamlLines(lines).iterator();
        MatcherAssert.assertThat(iterator.next().number(), Matchers.is(0));
        MatcherAssert.assertThat(iterator.next().number(), Matchers.is(1));
        MatcherAssert.assertThat(iterator.next().number(), Matchers.is(2));
        MatcherAssert.assertThat(iterator.next().number(), Matchers.is(3));
        MatcherAssert.assertThat(iterator.next().number(), Matchers.is(4));
        MatcherAssert.assertThat(iterator.hasNext(), Matchers.is(false));
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). The lines should turn into a
     * literal block scalar.
     */
    @Test
    public void turnsIntoLiteralBlockScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("line1", 1));
        lines.add(new RtYamlLine("line2", 2));
        lines.add(new RtYamlLine("line3", 3));
        final YamlLines yamlLines = new AllYamlLines(lines);
        MatcherAssert.assertThat(
            yamlLines.toYamlNode(
                new RtYamlLine("literalScalar:|", 0),
                false
            ),
            Matchers.instanceOf(ReadLiteralBlockScalar.class)
        );
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). The lines should turn into a
     * folded block scalar.
     */
    @Test
    public void turnsIntoFoldedBlockScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("more lines for", 1));
        lines.add(new RtYamlLine("better understanding", 2));
        lines.add(new RtYamlLine("of text", 3));
        final YamlLines yamlLines = new AllYamlLines(lines);
        MatcherAssert.assertThat(
            yamlLines.toYamlNode(
                new RtYamlLine("foldedScalar:>", 0),
                false
            ),
            Matchers.instanceOf(ReadFoldedBlockScalar.class)
        );
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). The lines should turn into a
     * (folded) sequence.
     */
    @Test
    public void turnsIntoFoldedSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("value1", 1));
        lines.add(new RtYamlLine("value2", 2));
        lines.add(new RtYamlLine("value3", 3));
        final YamlLines yamlLines = new AllYamlLines(lines);
        final YamlNode seq =  yamlLines.toYamlNode(
            new RtYamlLine("foldedSequence:|-", 0), false
        );
        MatcherAssert.assertThat(
            seq, Matchers.instanceOf(ReadYamlSequence.class)
        );
        final Collection<YamlNode> values = ((YamlSequence) seq).values();
        MatcherAssert.assertThat(values.size(), Matchers.is(3));
        System.out.print(values);
        MatcherAssert.assertThat(
            values.contains(new PlainStringScalar("value1")),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            values.contains(
                new PlainStringScalar("value2")
            ),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            values.contains(new PlainStringScalar("value3")),
            Matchers.is(Boolean.TRUE)
        );
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). The lines should turn into a
     * (folded) sequence. There are spaces between the last 2 characters of
     * the previous line.
     */
    @Test
    public void turnsIntoFoldedSequenceWithSpaces() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("value1", 1));
        lines.add(new RtYamlLine("value2", 2));
        lines.add(new RtYamlLine("value3", 3));
        final YamlLines yamlLines = new AllYamlLines(lines);
        final YamlNode seq =  yamlLines.toYamlNode(
            new RtYamlLine("foldedSequence:| -", 0), false
        );
        MatcherAssert.assertThat(
            seq, Matchers.instanceOf(ReadYamlSequence.class)
        );
        final Collection<YamlNode> values = ((YamlSequence) seq).values();
        MatcherAssert.assertThat(values.size(), Matchers.is(3));
        System.out.print(values);
        MatcherAssert.assertThat(
            values.contains(new PlainStringScalar("value1")),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            values.contains(
                new PlainStringScalar("value2")
            ),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            values.contains(new PlainStringScalar("value3")),
            Matchers.is(Boolean.TRUE)
        );
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). The lines should turn
     * into a (detected) mapping.
     */
    @Test
    public void turnsIntoMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("some: mapping", 1));
        lines.add(new RtYamlLine("for: test", 2));
        final YamlLines yamlLines = new AllYamlLines(lines);
        MatcherAssert.assertThat(
            yamlLines.toYamlNode(new RtYamlLine("---", 0), false),
            Matchers.instanceOf(ReadYamlMapping.class)
        );
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). The lines should turn
     * into a (detected) sequence.
     */
    @Test
    public void turnsIntoSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- some", 1));
        lines.add(new RtYamlLine("- sequence", 2));
        final YamlLines yamlLines = new AllYamlLines(lines);
        MatcherAssert.assertThat(
            yamlLines.toYamlNode(new RtYamlLine("?", 0), false),
            Matchers.instanceOf(ReadYamlSequence.class)
        );
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). The lines should turn
     * into a (detected) sequence.
     */
    @Test
    public void turnsIntoPlainScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("justAScalar", 1));
        final YamlLines yamlLines = new AllYamlLines(lines);
        MatcherAssert.assertThat(
            yamlLines.toYamlNode(new RtYamlLine("---", 0), false),
            Matchers.instanceOf(ReadPlainScalar.class)
        );
    }

    /**
     * Unit test for AllYamlLines.toYamlNode(). It should complain, because
     * it cannot detect the type of node. The previous line does not point to
     * any specific node and the lines themselves are not a mapping, sequence
     * or a plains scalar.
     */
    @Test
    public void complainsWhenCannotTurn() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("this will fail", 0));
        lines.add(new RtYamlLine("because these lines", 1));
        lines.add(new RtYamlLine("have no YAML meaning.", 2));
        lines.add(new RtYamlLine("They are also not", 3));
        lines.add(new RtYamlLine("a folded or literal scalar", 4));
        final YamlLines yamlLines = new AllYamlLines(lines);
        try {
            yamlLines.toYamlNode(new RtYamlLine("---", -1), false);
            Assert.fail("Expected IllegalStateException!");
        } catch (final YamlReadingException ex) {
            final String message = ex.getMessage();
            MatcherAssert.assertThat(
                message,
                Matchers.startsWith("Could not parse YAML starting at line 1")
            );
            MatcherAssert.assertThat(
                message,
                Matchers.containsString("but it has 5 lines")
            );
        }
    }
}
