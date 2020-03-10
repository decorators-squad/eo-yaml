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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RtYamlMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @ince 1.0.0
 *
 */
public final class RtYamlMappingTest {

    /**
     * RtYamlMapping can fetch its key-ordered children.
     */
    @Test
    public void fetchesOrderedChildren() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key2"), new Scalar("value1"));
        mappings.put(new Scalar("key3"), new Scalar("value2"));
        mappings.put(new Scalar("key1"), new Scalar("value3"));
        YamlMapping map = new RtYamlMapping(mappings);
        final Collection<YamlNode> children = map.children();
        MatcherAssert.assertThat(
    		children, Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(children.size(), Matchers.equalTo(3));
        final Iterator<YamlNode> iterator = children.iterator();
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new Scalar("value3"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new Scalar("value1"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new Scalar("value2"))
        );
    }

    /**
     * RtYamlMapping can fetch its ordered keys.
     */
    @Test
    public void fetchesOrderedKeys() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), Mockito.mock(YamlNode.class));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlNode.class));
        mappings.put(new Scalar("key2"), Mockito.mock(YamlNode.class));
        YamlMapping map = new RtYamlMapping(mappings);
        final Set<YamlNode> keys = map.keys();
        MatcherAssert.assertThat(
            keys, Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(3));
        final Iterator<YamlNode> iterator = keys.iterator();
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new Scalar("key1"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new Scalar("key2"))
        );
        MatcherAssert.assertThat(
            iterator.next(), Matchers.equalTo(new Scalar("key3"))
        );

    }

    /**
     * RtYamlMapping can return a Scalar as a string.
     */
    @Test
    public void returnsYamlScalarAsString() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        String value = "value2";
        Scalar key = new Scalar("key2");
        mappings.put(new Scalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(key, new Scalar(value));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlMapping.class));
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
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        Scalar key = new Scalar("missing");
        mappings.put(new Scalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new Scalar("key4"), Mockito.mock(YamlNode.class));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);

        MatcherAssert.assertThat(map.string("missing"), Matchers.nullValue());
        MatcherAssert.assertThat(map.string(key), Matchers.nullValue());
        
        MatcherAssert.assertThat(
            map.string(new Scalar("keÿ3")), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can return a YamlMapping.
     */
    @Test
    public void returnsYamlMapping() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlMapping.class));
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
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlMapping.class));
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
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlMapping.class));
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
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlMapping.class));
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.yamlSequence("key4"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.yamlSequence("key1"), Matchers.nullValue()
        );
    }

    /**
     * RtYamlMapping can return null if the specified key is missig.
     */
    @Test
    public void returnsNullOnMissingKey() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), Mockito.mock(YamlSequence.class));
        mappings.put(new Scalar("key1"), Mockito.mock(YamlMapping.class));
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
            new HashMap<YamlNode, YamlNode>()
        );
        Scalar scalar = new Scalar("java");
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
            new HashMap<YamlNode, YamlNode>()
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
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), new Scalar("value1"));
        mappings.put(new Scalar("key2"), new Scalar("value2"));
        mappings.put(new Scalar("key1"), new Scalar("value3"));
        YamlMapping firstmap = new RtYamlMapping(mappings);
        YamlMapping secondmap = new RtYamlMapping(mappings);
        mappings.put(new Scalar("key4"), new Scalar("value4"));
        YamlMapping thirdmap = new RtYamlMapping(mappings);

        Map<YamlNode, YamlNode> othermappings = new HashMap<>();
        othermappings.put(
            new Scalar("architect"),
            Mockito.mock(YamlSequence.class)
        );
        othermappings.put(new Scalar("dev"), firstmap);
        othermappings.put(new Scalar("tester"), secondmap);
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
            .add("name", "eo-yaml")
            .add("architect", "mihai")
            .add("developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            ).build();
        String expected = this.readTestResource("simpleMapping.yml");
        MatcherAssert.assertThat(yaml.toString(), Matchers.equalTo(expected));
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
                    .add("Chicago cubs")
                    .add("Detroit Tigers")
                    .build(),
                Yaml.createYamlSequenceBuilder()
                    .add("2001-07-23")
                    .build()
            )
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
