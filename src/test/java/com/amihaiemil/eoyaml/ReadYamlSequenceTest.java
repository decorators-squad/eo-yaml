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
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Unit tests for {@link ReadYamlSequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class ReadYamlSequenceTest {

    /**
     * ReadYamlSequence can return the YamlMapping from a given index.
     * The YamlMapping starts after the dash line.
     */
    @Test
    public void returnsYamlMappingFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(sequence);
        final YamlMapping alfa = sequence.yamlMapping(2);
        MatcherAssert.assertThat(alfa, Matchers.notNullValue());
        MatcherAssert.assertThat(alfa, Matchers.instanceOf(YamlMapping.class));
        MatcherAssert.assertThat(
            alfa.yamlMapping("alfa").string("key"), Matchers.equalTo("value")
        );
    }

    /**
     * ReadYamlSequence can return the YamlMapping which starts right
     * at the dash line. The YamlMapping has a scalar key and another mapping
     * as value of this key.
     */
    @Test
    public void returnsYamlMappingWithMappingValueStartingAtDash(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- scalar0", 0));
        lines.add(new RtYamlLine("- scalar1", 1));
        lines.add(new RtYamlLine("- alfa:", 2));
        lines.add(new RtYamlLine("    key: value", 3));
        lines.add(new RtYamlLine("    key2: value2", 4));
        lines.add(new RtYamlLine("- scalar2", 5));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(sequence);
        final YamlMapping alfa = sequence.yamlMapping(2);
        MatcherAssert.assertThat(alfa, Matchers.notNullValue());
        MatcherAssert.assertThat(alfa, Matchers.instanceOf(YamlMapping.class));
        MatcherAssert.assertThat(
            alfa.yamlMapping("alfa").string("key"), Matchers.equalTo("value")
        );
        MatcherAssert.assertThat(
            alfa.yamlMapping("alfa").string("key2"), Matchers.equalTo("value2")
        );
        for(final YamlNode value : sequence.values()) {
            System.out.println(value.type());
        }
    }

    /**
     * ReadYamlSequence can return the YamlMapping which starts right
     * at the dash line.
     */
    @Test
    public void returnsYamlMappingWithScalarValuesStartingAtDash(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- scalar0", 0));
        lines.add(new RtYamlLine("- scalar1", 1));
        lines.add(new RtYamlLine("- alfa: beta", 2));
        lines.add(new RtYamlLine("  teta: gama", 3));
        lines.add(new RtYamlLine("  omega: value", 4));
        lines.add(new RtYamlLine("- scalar2", 5));
        final YamlSequence sequence = new ReadYamlSequence(
                new AllYamlLines(lines)
        );
        System.out.println(sequence);
        final YamlMapping dashMap = sequence.yamlMapping(2);
        MatcherAssert.assertThat(dashMap, Matchers.notNullValue());
        MatcherAssert.assertThat(
            dashMap, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            dashMap.string("alfa"), Matchers.equalTo("beta")
        );
        MatcherAssert.assertThat(
            dashMap.string("teta"), Matchers.equalTo("gama")
        );
        MatcherAssert.assertThat(
            dashMap.string("omega"), Matchers.equalTo("value")
        );
    }

    /**
     * ReadYamlSequence can return the YamlMapping which starts right at the
     * dash line and has only one entry.
     * @checkstyle ExecutableStatementCount (100 lines)
     */
    @Test
    public void returnsShortYamlMappingWithScalarValuesStartingAtDash(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- beta1: somethingElse", 0));
        lines.add(new RtYamlLine("- scalar1", 1));
        lines.add(new RtYamlLine("- \"beta2\": somethingElse", 2));
        lines.add(new RtYamlLine("- scalar2", 3));
        lines.add(new RtYamlLine("- beta3: \"something-else\"", 4));
        lines.add(new RtYamlLine("- scalar3", 5));
        lines.add(new RtYamlLine("- beta4: somethingElse", 6));
        lines.add(new RtYamlLine("- \"scalar:4\"", 7));
        lines.add(new RtYamlLine("- beta5: somethingElse", 8));
        lines.add(new RtYamlLine("- beta6: somethingElse", 9));
        lines.add(new RtYamlLine("- 'scalar:5'", 10));
        final YamlSequence sequence = new ReadYamlSequence(
                new AllYamlLines(lines)
        );
        System.out.println(sequence);
        MatcherAssert.assertThat(sequence.size(), Matchers.is(11));
        MatcherAssert.assertThat(
                sequence.yamlMapping(0).string("beta1"),
                Matchers.equalTo("somethingElse")
        );
        MatcherAssert.assertThat(sequence.string(1),
                Matchers.equalTo("scalar1")
        );
        MatcherAssert.assertThat(
                sequence.yamlMapping(2).string("beta2"),
                Matchers.equalTo("somethingElse")
        );
        MatcherAssert.assertThat(sequence.string(3),
                Matchers.equalTo("scalar2")
        );
        MatcherAssert.assertThat(
                sequence.yamlMapping(4).string("beta3"),
                Matchers.equalTo("something-else")
        );
        MatcherAssert.assertThat(sequence.string(5),
                Matchers.equalTo("scalar3")
        );
        MatcherAssert.assertThat(
                sequence.yamlMapping(6).string("beta4"),
                Matchers.equalTo("somethingElse")
        );
        MatcherAssert.assertThat(sequence.string(7),
                Matchers.equalTo("scalar:4")
        );
        MatcherAssert.assertThat(
                sequence.yamlMapping(8).string("beta5"),
                Matchers.equalTo("somethingElse")
        );
        MatcherAssert.assertThat(
                sequence.yamlMapping(9).string("beta6"),
                Matchers.equalTo("somethingElse")
        );
        MatcherAssert.assertThat(sequence.string(10),
                Matchers.equalTo("scalar:5")
        );
    }

    /**
     * ReadYamlSequence can return the YamlSequence from a given index.
     */
    @Test
    public void returnsYamlSequenceFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  - rultor", 1));
        lines.add(new RtYamlLine("  - 0pdd", 2));
        lines.add(new RtYamlLine("- scalar", 3));
        lines.add(new RtYamlLine("- otherScalar", 4));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(sequence);
        final YamlSequence devops = sequence.yamlSequence(0);
        MatcherAssert.assertThat(devops, Matchers.notNullValue());
        MatcherAssert.assertThat(
            devops, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(devops.size(), Matchers.equalTo(2));
    }

    /**
     * ReadYamlSequence can return the plain scalar string from a given index.
     */
    @Test
    public void returnsStringFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- rultor", 0));
        lines.add(new RtYamlLine("- 0pdd", 1));
        final YamlSequence devops = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(devops);
        MatcherAssert.assertThat(devops.string(0), Matchers.equalTo("rultor"));
        MatcherAssert.assertThat(devops.string(1), Matchers.equalTo("0pdd"));
    }

    /**
     * ReadYamlSequence can return the folded block scalar as a string,
     * from a given index.
     */
    @Test
    @Ignore
    public void returnsFoldedBlockScalarStringFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- rultor", 0));
        lines.add(new RtYamlLine("- 0pdd", 1));
        lines.add(new RtYamlLine("- >", 2));
        lines.add(new RtYamlLine("  first", 3));
        lines.add(new RtYamlLine("  second", 4));
        lines.add(new RtYamlLine("- another", 5));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(sequence);
        MatcherAssert.assertThat(
            sequence.foldedBlockScalar(2),
            Matchers.equalTo("first second")
        );
    }

    /**
     * ReadYamlSequence can return the folded block scalar as a collection of
     * string lines from a given index.
     */
    @Test
    public void returnsLiteralBlockScalarFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- rultor", 0));
        lines.add(new RtYamlLine("- 0pdd", 1));
        lines.add(new RtYamlLine("- |", 2));
        lines.add(new RtYamlLine("  first", 3));
        lines.add(new RtYamlLine("  second", 4));
        lines.add(new RtYamlLine("- another", 5));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(sequence);
        final Collection<String> literalLines = sequence.literalBlockScalar(2);
        MatcherAssert.assertThat(
            literalLines.size(),
            Matchers.is(2)
        );
        final Iterator<String> linesIt = literalLines.iterator();
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("first")
        );
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("second")
        );
        MatcherAssert.assertThat(
            sequence.yamlMapping(2), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlSequence can return its size.
     */
    @Test
    public void returnsSize(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(sequence);
        MatcherAssert.assertThat(sequence.size(), Matchers.is(3));
    }

    /**
     * ReadYamlSequence can be iterated.
     */
    @Test
    public void readSequenceIsIterable(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence seq = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        System.out.println(seq);
        MatcherAssert.assertThat(seq, Matchers.not(Matchers.emptyIterable()));
        MatcherAssert.assertThat(seq, Matchers.iterableWithSize(3));
    }

    /**
     * ReadYamlSequence can return null on a misread scalar.
     */
    @Test
    public void returnsNullOnMisreadScalar(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            sequence.string(0),
            Matchers.nullValue()
        );
    }

    /**
     * ReadYamlSequence can return null on a misread sequence.
     */
    @Test
    public void returnsNullOnMisreadSequence(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            sequence.yamlSequence(0),
            Matchers.nullValue()
        );
    }

    /**
     * ReadYamlSequence can return null on a misread sequence.
     */
    @Test
    public void returnsNullOnMisreadMapping(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            sequence.yamlMapping(1),
            Matchers.nullValue()
        );
    }

    /**
     * ReadYamlSequence can return null if the specified index does not
     * exist.
     */
    @Test
    public void returnsNullOnOutOfIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            sequence.yamlMapping(10),
            Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            sequence.string(10),
            Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            sequence.yamlSequence(10),
            Matchers.nullValue()
        );
    }

    /**
     * ReadYamlSequence returns the correct value for empty maps and sequences.
     */
    @Test
    public void dontTurnEmptyMapsAndArraysIntoStrings() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("# A", 0));
        lines.add(new RtYamlLine("- {}", 1));
        lines.add(new RtYamlLine("# B", 2));
        lines.add(new RtYamlLine("- []", 3));
        final YamlSequence seq = new ReadYamlSequence(new AllYamlLines(lines));
        Iterator<YamlNode> iterator = seq.values().iterator();
        YamlMapping actualMap = iterator.next().asMapping();
        YamlMapping expectedMap = Yaml.createYamlMappingBuilder().build("A");
        MatcherAssert.assertThat(
                actualMap,
                Matchers.equalTo(expectedMap)
        );
        MatcherAssert.assertThat(
                actualMap.comment().value(),
                Matchers.equalTo(expectedMap.comment().value())
        );
        YamlSequence actualSeq = iterator.next().asSequence();
        YamlSequence expectedSeq = Yaml.createYamlSequenceBuilder().build("B");
        MatcherAssert.assertThat(
                actualSeq,
                Matchers.equalTo(expectedSeq)
        );
        MatcherAssert.assertThat(
                actualSeq.comment().value(),
                Matchers.equalTo(expectedSeq.comment().value())
        );
    }

    /**
     * An empty ReadYamlSequence can be printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void printsEmptyYaml() throws Exception {
        final YamlSequence sequence = new ReadYamlSequence(
            new AllYamlLines(new ArrayList<YamlLine>())
        );
        MatcherAssert.assertThat(sequence.toString(), Matchers.isEmptyString());
    }

    /**
     * Reads scalar containing colon properly.
     */
    @Test
    public void dontReturnMappingForScalarWithColon() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- scalar:with-colon", 0));
        final YamlSequence sequence = new ReadYamlSequence(
                new AllYamlLines(lines)
        );

        MatcherAssert.assertThat(sequence.values(), Matchers.hasSize(1));
        YamlNode sequenceItem = sequence.values().iterator().next();
        MatcherAssert.assertThat(
            sequenceItem.type(),
            Matchers.equalTo(Node.SCALAR)
        );
        MatcherAssert.assertThat(
            sequenceItem.asScalar().value(),
            Matchers.equalTo("scalar:with-colon")
        );
    }
}
