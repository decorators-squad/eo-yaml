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
 * Unit tests for {@link RtYamlMapping}.
 * @checkstyle LineLength (1000 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @ince 1.0.0
 *
 */
public final class RtYamlMappingTest {

    /**
     * RtYamlMapping can fetch its values.
     */
    @Test
    public void fetchesValues() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key2"), new PlainStringScalar("value1"));
        mappings.put(new PlainStringScalar("key3"), new PlainStringScalar("value2"));
        mappings.put(new PlainStringScalar("key1"), new PlainStringScalar("value3"));
        YamlMapping map = new RtYamlMapping(mappings);
        final Collection<YamlNode> children = map.values();
        MatcherAssert.assertThat(
            children, Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(children.size(), Matchers.equalTo(3));
        final Iterator<YamlNode> iterator = children.iterator();
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new PlainStringScalar("value1"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new PlainStringScalar("value2"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new PlainStringScalar("value3"))
        );
    }

    /**
     * RtYamlMapping can fetch its keys.
     */
    @Test
    public void fetchesKeys() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlNode.class));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlNode.class));
        mappings.put(new PlainStringScalar("key2"), Mockito.mock(YamlNode.class));
        YamlMapping map = new RtYamlMapping(mappings);
        final Set<YamlNode> keys = map.keys();
        MatcherAssert.assertThat(
            keys, Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(3));
        final Iterator<YamlNode> iterator = keys.iterator();
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new PlainStringScalar("key3"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new PlainStringScalar("key1"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new PlainStringScalar("key2"))
        );

    }

    /**
     * RtYamlMapping can return a Scalar as a string.
     */
    @Test
    public void returnsYamlScalarAsString() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        String value = "value2";
        PlainStringScalar key = new PlainStringScalar("key2");
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(key, new PlainStringScalar(value));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(
            map.string("key2"), Matchers.equalTo(value)
        );
        MatcherAssert.assertThat(
            map.string(key), Matchers.equalTo(value)
        );
    }

    /**
     * RtYamlMapping can return null if the queried Scalar is missing or
     * the found value is not actually a Scalar.
     */
    @Test
    public void returnsNullOnMissingScalar() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        PlainStringScalar key = new PlainStringScalar("missing");
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new PlainStringScalar("key4"), Mockito.mock(YamlNode.class));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(map.string("missing"), Matchers.nullValue());
        MatcherAssert.assertThat(map.string(key), Matchers.nullValue());

        MatcherAssert.assertThat(
            map.string(new PlainStringScalar("keÿ3")), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can return a YamlMapping.
     */
    @Test
    public void returnsYamlMapping() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(
            map.yamlMapping("key1"), Matchers.notNullValue()
        );
    }

    /**
     * RtYamlMapping can return null if the queried YamlMapping is missing or
     * if the found value is not actually a YamlMapping.
     */
    @Test
    public void returnsNullOnMissingYamlMapping() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(
            map.yamlMapping("key4"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.yamlMapping("key3"), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can return a YamlSequence.
     */
    @Test
    public void returnsYamlSequence() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.yamlSequence("key3"), Matchers.notNullValue()
        );
    }

    /**
     * RtYamlMapping can return null if the queried YamlSequence is missing or
     * if the found value is not actually a YamlSequence.
     */
    @Test
    public void returnsNullOnMissingYamlSequence() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.yamlSequence("key4"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.yamlSequence("key1"), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can return a folded block Scalar as a string.
     */
    @Test
    public void returnsFoldedBlockScalarAsString() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(
            new PlainStringScalar("key3"),
            Mockito.mock(YamlSequence.class)
        );
        mappings.put(
            new PlainStringScalar("key1"),
            Mockito.mock(YamlMapping.class)
        );
        mappings.put(
            new PlainStringScalar("key2"),
            new RtYamlScalarBuilder.BuiltFoldedBlockScalar(
                Arrays.asList("first", "second")
            )
        );
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.foldedBlockScalar("key2"),
            Matchers.equalTo("first second")
        );
        MatcherAssert.assertThat(
            map.foldedBlockScalar("key3"), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can return a literal block Scalar as a
     * Collection of string lines.
     */
    @Test
    public void returnsLiteralBlockScalar() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(
            new PlainStringScalar("key3"),
            Mockito.mock(YamlSequence.class)
        );
        mappings.put(
            new PlainStringScalar("key1"),
            Mockito.mock(YamlMapping.class)
        );
        mappings.put(
            new PlainStringScalar("key2"),
            new RtYamlScalarBuilder.BuiltLiteralBlockScalar(
                Arrays.asList("first", "second")
            )
        );
        RtYamlMapping map = new RtYamlMapping(mappings);
        final Collection<String> literalLines = map.literalBlockScalar("key2");
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
            map.literalBlockScalar("key3"), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can return null if the specified key is missig.
     */
    @Test
    public void returnsNullOnMissingKey() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.yamlSequence("key4"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.yamlMapping("key4"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.string("key4"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.yamlSequence("key1"), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can compare itself to a Scalar.
     */
    @Test
    public void comparesToScalar() {
        RtYamlMapping map = new RtYamlMapping(
            new LinkedHashMap<YamlNode, YamlNode>()
        );
        PlainStringScalar scalar = new PlainStringScalar("java");
        MatcherAssert.assertThat(
            map.compareTo(scalar),
            Matchers.greaterThan(0)
        );
    }

    /**
     * RtYamlMapping can compare itself to a Sequence.
     */
    @Test
    public void comparesToSequence() {
        RtYamlMapping map = new RtYamlMapping(
            new LinkedHashMap<YamlNode, YamlNode>()
        );
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<YamlNode>());
        MatcherAssert.assertThat(
            map.compareTo(seq),
            Matchers.greaterThan(0)
        );
    }

    /**
     * RtYamlMapping can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key3"), new PlainStringScalar("value1"));
        mappings.put(new PlainStringScalar("key2"), new PlainStringScalar("value2"));
        mappings.put(new PlainStringScalar("key1"), new PlainStringScalar("value3"));
        YamlMapping firstmap = new RtYamlMapping(mappings);
        YamlMapping secondmap = new RtYamlMapping(mappings);
        mappings.put(new PlainStringScalar("key4"), new PlainStringScalar("value4"));
        YamlMapping thirdmap = new RtYamlMapping(mappings);

        Map<YamlNode, YamlNode> othermappings = new LinkedHashMap<>();
        othermappings.put(
            new PlainStringScalar("architect"),
            Mockito.mock(YamlSequence.class)
        );
        othermappings.put(new PlainStringScalar("dev"), firstmap);
        othermappings.put(new PlainStringScalar("tester"), secondmap);
        YamlMapping fourthmap = new RtYamlMapping(othermappings);

        MatcherAssert.assertThat(
            firstmap.compareTo(firstmap), Matchers.equalTo(0)
        );
        MatcherAssert.assertThat(
            firstmap.compareTo(secondmap), Matchers.equalTo(0)
        );
        MatcherAssert.assertThat(
            firstmap.compareTo(null), Matchers.greaterThan(0)
        );
        MatcherAssert.assertThat(
            firstmap.compareTo(thirdmap), Matchers.lessThan(0)
        );
        MatcherAssert.assertThat(
            thirdmap.compareTo(firstmap), Matchers.greaterThan(0)
        );
        MatcherAssert.assertThat(
            firstmap.compareTo(fourthmap), Matchers.greaterThan(0)
        );
        MatcherAssert.assertThat(
            fourthmap.compareTo(firstmap), Matchers.lessThan(0)
        );
    }

    /**
     * A simple YamlMapping can be pretty printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void prettyPrintsSimpleYaml() throws Exception {
        YamlMapping yaml = Yaml.createYamlMappingBuilder()
            .add("architect", "mihai")
            .add("developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add("name", "eo-yaml")
            .build();
        System.out.print(yaml.toString());
        String expected = this.readTestResource("simpleMapping.yml");
        MatcherAssert.assertThat(yaml.toString(), Matchers.equalTo(expected));
    }

    /**
     * A built YamlMapping with a single key-value pair is printed.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void prettyPrintsOnePair() throws Exception {
        YamlMapping yaml = Yaml.createYamlMappingBuilder()
            .add("spec", "1.2")
            .build();
        System.out.print(yaml);
        MatcherAssert.assertThat(
            yaml.toString(),
            Matchers.equalTo("spec: 1.2")
        );
    }


    /**
     * A complex YamlMapping can be pretty printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void prettyPrintsComplexYaml() throws Exception {
        YamlMapping yaml = Yaml.createYamlMappingBuilder()
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("Atlanta Braves")
                    .add("New York Yankees")
                    .build(),
                Yaml.createYamlSequenceBuilder()
                    .add("2001-07-02")
                    .add("2001-08-12")
                    .add("2001-08-14")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("Chicago cubs")
                    .add("Detroit Tigers")
                    .build(),
                Yaml.createYamlSequenceBuilder()
                    .add("2001-07-23")
                    .build()
            )
            .build();
        String expected = this.readTestResource("complexMapping.yml");
        MatcherAssert.assertThat(yaml.toString(), Matchers.equalTo(expected));
    }

    /**
     * An empty RtYamlMapping can be printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void printsEmptyYaml() throws Exception {
        YamlMapping yaml = Yaml.createYamlMappingBuilder().build();
        MatcherAssert.assertThat(yaml.toString(), Matchers.isEmptyString());
    }

    /**
     * RtYamlMapping.value(YamlNode) can return a found Scalar value.
     */
    @Test
    public void returnsScalarValue() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        PlainStringScalar value = new PlainStringScalar("value2");
        PlainStringScalar key = new PlainStringScalar("key2");
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(key, value);
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(map.value(key), Matchers.equalTo(value));
    }

    /**
     * RtYamlMapping.value(YamlNode) can return a found YamlMapping value.
     */
    @Test
    public void returnsYamlMappingValue() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        YamlMapping value = Mockito.mock(YamlMapping.class);
        PlainStringScalar key = new PlainStringScalar("key2");
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(key, value);
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(map.value(key), Matchers.equalTo(value));
    }

    /**
     * RtYamlMapping.value(YamlNode) can return a found YamlSequence value.
     */
    @Test
    public void returnsYamlSequenceValue() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        YamlSequence value = Mockito.mock(YamlSequence.class);
        PlainStringScalar key = new PlainStringScalar("key2");
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(key, value);
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(map.value(key), Matchers.equalTo(value));
    }

    /**
     * RtYamlMapping.value(YamlNode) can return null if the value is not there.
     */
    @Test
    public void returnsNullOnMissingValue() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        YamlSequence value = Mockito.mock(YamlSequence.class);
        PlainStringScalar key = new PlainStringScalar("key2");
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(key, value);
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(
            map.value(new PlainStringScalar("notthere")),
            Matchers.nullValue()
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
                new FileInputStream(
                    new File("src/test/resources/" + fileName)
                )
            )
        );
    }

}
