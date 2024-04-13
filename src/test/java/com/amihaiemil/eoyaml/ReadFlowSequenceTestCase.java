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
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Unit tests for {@link ReadFlowSequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 6.0.0
 */
public final class ReadFlowSequenceTestCase {

    /**
     * Sequences in flow/json style have no comment.
     */
    @Test
    public void hasNoComment() {
        final YamlSequence seq = new ReadFlowSequence(
            Mockito.mock(YamlLine.class),
            Mockito.mock(YamlLine.class),
            new AllYamlLines(new ArrayList<>())
        );
        MatcherAssert.assertThat(
            seq.comment().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            seq.comment().yamlNode(),
            Matchers.is(seq)
        );
    }

    /**
     * A ReadFlowSequence can be empty.
     */
    @Test
    public void hasNoValues() {
        final YamlSequence seq = new ReadFlowSequence(
            new RtYamlLine("[]", 0),
            Mockito.mock(YamlLine.class),
            new AllYamlLines(new ArrayList<>())
        );
        MatcherAssert.assertThat(
            seq.values(),
            Matchers.emptyIterable()
        );
    }

    /**
     * A ReadFlowSequence works if it has only scalars in it.
     */
    @Test
    public void hasOnlyScalars() {
        final YamlSequence seq = new ReadFlowSequence(
            new RtYamlLine("[a, b, c, d:e]", 0),
            Mockito.mock(YamlLine.class),
            new AllYamlLines(new ArrayList<>())
        );
        final Collection<YamlNode> values = seq.values();
        MatcherAssert.assertThat(values, Matchers.iterableWithSize(4));
        final Iterator<YamlNode> iterator = values.iterator();
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("a")
        );
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("b")
        );
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("c")
        );
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("d:e")
        );
    }

    /**
     * A ReadFlowSequence works if it has only sequences in it.
     */
    @Test
    public void hasOnlySequences() {
        final YamlSequence seq = new ReadFlowSequence(
            new RtYamlLine("[[a, b], [c,\"[a]\",'b]['], [d]]", 0),
            Mockito.mock(YamlLine.class),
            new AllYamlLines(new ArrayList<>())
        );
        System.out.println(seq);
        MatcherAssert.assertThat(
            seq.values(),
            Matchers.iterableWithSize(3)
        );
        final YamlSequence first = seq.yamlSequence(0);
        MatcherAssert.assertThat(first, Matchers.iterableWithSize(2));
        MatcherAssert.assertThat(
            first.string(0),
            Matchers.equalTo("a")
        );
        MatcherAssert.assertThat(
            first.string(1),
            Matchers.equalTo("b")
        );

        final YamlSequence second = seq.yamlSequence(1);
        MatcherAssert.assertThat(second, Matchers.iterableWithSize(3));
        MatcherAssert.assertThat(
            second.string(0),
            Matchers.equalTo("c")
        );
        MatcherAssert.assertThat(
            second.string(1),
            Matchers.equalTo("[a]")
        );
        MatcherAssert.assertThat(
            second.string(2),
            Matchers.equalTo("b][")
        );

        final YamlSequence third = seq.yamlSequence(2);
        MatcherAssert.assertThat(third, Matchers.iterableWithSize(1));
        MatcherAssert.assertThat(
            third.string(0),
            Matchers.equalTo("d")
        );
    }

    /**
     * A ReadFlowSequence should be able to hold both
     * scalars and other sequences.
     */
    @Test
    public void hasSequencesAndScalars() {
        final YamlSequence seq = new ReadFlowSequence(
            new RtYamlLine(
                "[scalar, \"u][\", 'v}{', 'escalar', [a, b], other, [d]]", 0
            ),
            Mockito.mock(YamlLine.class),
            new AllYamlLines(new ArrayList<>())
        );
        System.out.println(seq);
        MatcherAssert.assertThat(
            seq.values(),
            Matchers.iterableWithSize(7)
        );
        MatcherAssert.assertThat(
            seq.string(0),
            Matchers.equalTo("scalar")
        );
        MatcherAssert.assertThat(
            seq.string(1),
            Matchers.equalTo("u][")
        );
        MatcherAssert.assertThat(
            seq.string(2),
            Matchers.equalTo("v}{")
        );
        MatcherAssert.assertThat(
            seq.string(3),
            Matchers.equalTo("escalar")
        );
        final YamlSequence firstSeq = seq.yamlSequence(4);
        MatcherAssert.assertThat(firstSeq, Matchers.iterableWithSize(2));
        MatcherAssert.assertThat(
            firstSeq.string(0),
            Matchers.equalTo("a")
        );
        MatcherAssert.assertThat(
            firstSeq.string(1),
            Matchers.equalTo("b")
        );
        MatcherAssert.assertThat(
            seq.string(5),
            Matchers.equalTo("other")
        );
        final YamlSequence secondSeq = seq.yamlSequence(6);
        MatcherAssert.assertThat(secondSeq, Matchers.iterableWithSize(1));
        MatcherAssert.assertThat(
            secondSeq.string(0),
            Matchers.equalTo("d")
        );
    }

    /**
     * A ReadFlowSequence should be able to hold both
     * scalars and other sequences on more than one line.
     */
    @Test
    public void hasSequencesAndScalarsMultiline() {
        final YamlSequence seq = new ReadFlowSequence(
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("[a, ", 0),
                    new RtYamlLine("   b,", 1),
                    new RtYamlLine(" [c, d],", 2),
                    new RtYamlLine("{e: f},", 3),
                    new RtYamlLine("g]", 4)
                )
            )
        );
        System.out.println(seq);
        MatcherAssert.assertThat(
            seq.values(),
            Matchers.iterableWithSize(5)
        );
        MatcherAssert.assertThat(
            seq.string(0),
            Matchers.equalTo("a")
        );
        MatcherAssert.assertThat(
            seq.string(1),
            Matchers.equalTo("b")
        );
        final YamlSequence firstSeq = seq.yamlSequence(2);
        MatcherAssert.assertThat(firstSeq, Matchers.iterableWithSize(2));
        MatcherAssert.assertThat(
            firstSeq.string(0),
            Matchers.equalTo("c")
        );
        MatcherAssert.assertThat(
            firstSeq.string(1),
            Matchers.equalTo("d")
        );
        MatcherAssert.assertThat(
            seq.yamlMapping(3).string("e"),
            Matchers.equalTo("f")
        );
        MatcherAssert.assertThat(
            seq.string(4),
            Matchers.equalTo("g")
        );
    }
}
