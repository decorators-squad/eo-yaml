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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RtYamlSequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class RtYamlSequenceTest {

    /**
     * Sequence can fetch its values.
     */
    @Test
    public void fetchesValues() {
        List<YamlNode> nodes = new LinkedList<>();
        nodes.add(Mockito.mock(YamlNode.class));
        nodes.add(Mockito.mock(YamlNode.class));
        nodes.add(Mockito.mock(YamlNode.class));
        RtYamlSequence seq = new RtYamlSequence(nodes);
        MatcherAssert.assertThat(
            seq.values(), Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(
            seq.values().size(),
            Matchers.equalTo(3)
        );
    }

    /**
     * Sequence can be iterated.
     */
    @Test
    public void sequenceIsIterable() {
        List<YamlNode> nodes = new LinkedList<>();
        nodes.add(Mockito.mock(YamlNode.class));
        nodes.add(Mockito.mock(YamlNode.class));
        nodes.add(Mockito.mock(YamlNode.class));
        YamlSequence seq = new RtYamlSequence(nodes);
        MatcherAssert.assertThat(seq, Matchers.not(Matchers.emptyIterable()));
        MatcherAssert.assertThat(seq, Matchers.iterableWithSize(3));
    }

    /**
     * A Sequence maintains its order of insertion.
     */
    @Test
    public void sequenceKeepsInsertionOrder() {
        List<YamlNode> nodes = new LinkedList<>();
        PlainStringScalar first = new PlainStringScalar("test");
        PlainStringScalar sec = new PlainStringScalar("mihai");
        PlainStringScalar third = new PlainStringScalar("amber");
        PlainStringScalar fourth = new PlainStringScalar("5433");
        nodes.add(first);
        nodes.add(sec);
        nodes.add(third);
        nodes.add(fourth);
        RtYamlSequence seq = new RtYamlSequence(nodes);
        Iterator<YamlNode> ordered = seq.values().iterator();
        MatcherAssert.assertThat(ordered.next(), Matchers.equalTo(first));
        MatcherAssert.assertThat(ordered.next(), Matchers.equalTo(sec));
        MatcherAssert.assertThat(ordered.next(), Matchers.equalTo(third));
        MatcherAssert.assertThat(ordered.next(), Matchers.equalTo(fourth));
    }

    /**
     * RtYamlSequence can return a Plain Scalar as a string.
     */
    @Test
    public void returnsYamlScalarAsString() {
        List<YamlNode> nodes = new LinkedList<>();
        nodes.add(new PlainStringScalar("test"));
        nodes.add(new PlainStringScalar("amber"));
        nodes.add(new PlainStringScalar("mihai"));
        YamlSequence seq = new RtYamlSequence(nodes);
        MatcherAssert.assertThat(
            seq.string(1), Matchers.equalTo("amber")
        );
        MatcherAssert.assertThat(
            seq.yamlMapping(1), Matchers.nullValue()
        );
    }

    /**
     * RtYamlSequence can return a folded block Scalar as a string.
     */
    @Test
    public void returnsFoldedBlockScalarAsString() {
        List<YamlNode> nodes = new LinkedList<>();
        nodes.add(new PlainStringScalar("test"));
        nodes.add(
            new RtYamlScalarBuilder.BuiltFoldedBlockScalar(
                Arrays.asList("first", "second")
            )
        );
        nodes.add(new PlainStringScalar("mihai"));
        YamlSequence seq = new RtYamlSequence(nodes);
        MatcherAssert.assertThat(
            seq.foldedBlockScalar(1),
            Matchers.equalTo("first second")
        );
        MatcherAssert.assertThat(
            seq.yamlMapping(1), Matchers.nullValue()
        );
    }

    /**
     * RtYamlSequence can return a literal block Scalar as a
     * Collection of string lines.
     */
    @Test
    public void returnsLiteralBlockScalar() {
        final List<YamlNode> nodes = new LinkedList<>();
        nodes.add(new PlainStringScalar("test"));
        nodes.add(
            new RtYamlScalarBuilder.BuiltLiteralBlockScalar(
                Arrays.asList("first", "second")
            )
        );
        nodes.add(new PlainStringScalar("mihai"));
        final YamlSequence seq = new RtYamlSequence(nodes);
        final Collection<String> literalLines = seq.literalBlockScalar(1);
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
            seq.yamlMapping(1), Matchers.nullValue()
        );
    }

    /**
     * RtYamlSequence can return a YamlMapping.
     */
    @Test
    public void returnsYamlMapping() {
        List<YamlNode> nodes = new LinkedList<>();
        nodes.add(new PlainStringScalar("test"));
        nodes.add(Mockito.mock(YamlMapping.class));
        nodes.add(new PlainStringScalar("mihai"));
        YamlSequence seq = new RtYamlSequence(nodes);
        MatcherAssert.assertThat(
            seq.yamlMapping(1), Matchers.notNullValue()
        );
    }

    /**
     * RtYamlSequence can return a YamlSequence.
     */
    @Test
    public void returnsYamlSequence() {
        List<YamlNode> nodes = new LinkedList<>();
        nodes.add(new PlainStringScalar("test"));
        nodes.add(Mockito.mock(YamlMapping.class));
        nodes.add(Mockito.mock(YamlSequence.class));
        YamlSequence seq = new RtYamlSequence(nodes);
        MatcherAssert.assertThat(
            seq.yamlSequence(2), Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            seq.yamlSequence(1), Matchers.nullValue()
        );
    }

    /**
     * Scalar can compare itself to another Scalar.
     */
    @Test
    public void comparesToScalar() {
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<YamlNode>());
        PlainStringScalar scalar = new PlainStringScalar("java");
        MatcherAssert.assertThat(
            seq.compareTo(scalar),
            Matchers.greaterThan(0)
        );
    }

    /**
     * Scalar can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<>());
        RtYamlMapping map = new RtYamlMapping(new LinkedHashMap<>());
        MatcherAssert.assertThat(seq.compareTo(map), Matchers.lessThan(0));
    }

    /**
     * Scalar can compare itself to a Sequence.
     * @checkstyle ExecutableStatementCount (100 lines)
     */
    @Test
    public void comparesToSequence() {
        List<YamlNode> nodes = new LinkedList<>();
        PlainStringScalar first = new PlainStringScalar("test");
        PlainStringScalar sec = new PlainStringScalar("mihai");
        PlainStringScalar third = new PlainStringScalar("amber");
        PlainStringScalar fourth = new PlainStringScalar("5433");
        nodes.add(first);
        nodes.add(sec);
        nodes.add(third);
        nodes.add(fourth);
        RtYamlSequence seq = new RtYamlSequence(nodes);
        RtYamlSequence other = new RtYamlSequence(nodes);

        nodes.remove(0);
        RtYamlSequence another = new RtYamlSequence(nodes);

        nodes.remove(0);
        nodes.add(0, new PlainStringScalar("yaml"));
        RtYamlSequence sameSize = new RtYamlSequence(nodes);

        MatcherAssert.assertThat(seq.compareTo(seq), Matchers.equalTo(0));
        MatcherAssert.assertThat(seq.compareTo(null), Matchers.greaterThan(0));
        MatcherAssert.assertThat(seq.compareTo(other), Matchers.equalTo(0));
        MatcherAssert.assertThat(
            seq.compareTo(another), Matchers.greaterThan(0)
        );
        MatcherAssert.assertThat(
            another.compareTo(sameSize), Matchers.lessThan(0)
        );
    }

    /**
     * A simple YamlSequecne can be pretty printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void prettyPrintsSimpleYamlSequence() throws Exception {
        YamlSequence seq = Yaml.createYamlSequenceBuilder()
            .add("amihaiemil")
            .add("rultor")
            .add("salikjan")
            .add("sherif")
            .build();
        String expected = this.readTestResource("simpleSequence.yml");
        MatcherAssert.assertThat(seq.toString(), Matchers.equalTo(expected));
    }

    /**
     * An empty YamlSequecne can be printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void printsEmptyYamlSequence() throws Exception {
        YamlSequence seq = Yaml.createYamlSequenceBuilder().build();
        MatcherAssert.assertThat(seq.toString(), Matchers.isEmptyString());
    }

    /**
     * A complex YamlSequecne can be pretty printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void prettyPrintsComplexYamlSequence() throws Exception {
        YamlSequence seq = Yaml.createYamlSequenceBuilder()
            .add("amihaiemil")
            .add("salikjan")
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("element1")
                    .add("element2")
                    .add("element3")
                    .build()
            ).add(
                Yaml.createYamlMappingBuilder()
                    .add("rule1", "test")
                    .add("rule2", "test2")
                    .add("rule3", "test3")
                    .build()
                )
            .build();
        String expected = this.readTestResource("complexSequence.yml");
        MatcherAssert.assertThat(seq.toString(), Matchers.equalTo(expected));
    }

    /**
     * RtYamlSequence can return its size.
     */
    @Test
    public void returnsSize(){
        List<YamlNode> nodes = new LinkedList<>();
        nodes.add(new PlainStringScalar("test"));
        nodes.add(Mockito.mock(YamlMapping.class));
        nodes.add(new PlainStringScalar("mihai"));
        YamlSequence seq = new RtYamlSequence(nodes);
        MatcherAssert.assertThat(seq.size(), Matchers.is(3));
    }

    /**
     * Read a test resource file's contents.
     * @param fileName File to read.
     * @return File's contents as String.
     * @throws FileNotFoundException If somethig is wrong.
     * @throws IOException If somethig is wrong.
     */
    private String readTestResource(final String fileName)
        throws FileNotFoundException, IOException {
        return new String(
            IOUtils.toByteArray(
                new FileInputStream(
                    new File("src/test/resources/" + fileName)
                )
            )
        );
    }
}
