/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
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
package com.amihaiemil.camel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

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
     * RtYamlMapping can fetch its children.
     */
    @Test
    public void fetchesChildren() {
        Map<AbstractYamlNode, AbstractYamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key1"), Mockito.mock(AbstractYamlNode.class));
        mappings.put(new Scalar("key2"), Mockito.mock(AbstractYamlNode.class));
        mappings.put(new Scalar("key3"), Mockito.mock(AbstractYamlNode.class));
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.children(), Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(map.children().size(), Matchers.equalTo(3));
    }

    /**
     * RtYamlMapping is ordered by keys.
     */
    @Test
    public void orderedKeys() {
        Map<AbstractYamlNode, AbstractYamlNode> mappings = new HashMap<>();
        Scalar one = new Scalar("value1");
        Scalar two = new Scalar("value2");
        Scalar three = new Scalar("value3");

        mappings.put(new Scalar("key3"), one);
        mappings.put(new Scalar("key2"), two);
        mappings.put(new Scalar("key1"), three);
        RtYamlMapping map = new RtYamlMapping(mappings);
        Iterator<AbstractYamlNode> children = map.children().iterator();
        MatcherAssert.assertThat(
            (Scalar) children.next(), Matchers.equalTo(three)
        );
        MatcherAssert.assertThat(
            (Scalar) children.next(), Matchers.equalTo(two)
        );
        MatcherAssert.assertThat(
            (Scalar) children.next(), Matchers.equalTo(one)
        );
    }

    /**
     * RtYamlMapping can return a Scalar as a string.
     */
    @Test
    public void returnsYamlScalarAsString() {
        Map<AbstractYamlNode, AbstractYamlNode> mappings = new HashMap<>();
        String value = "value2";
        Scalar key = new Scalar("key2");
        mappings.put(
            new Scalar("key3"), Mockito.mock(AbstractYamlSequence.class)
        );
        mappings.put(key, new Scalar(value));
        mappings.put(
            new Scalar("key1"), Mockito.mock(AbstractYamlMapping.class)
        );
        RtYamlMapping map = new RtYamlMapping(mappings);
        
        MatcherAssert.assertThat(
            map.string("key2"), Matchers.equalTo(value)
        );
        MatcherAssert.assertThat(
            map.string(key), Matchers.equalTo(value)
        );
    }

    /**
     * RtYamlMapping can return a YamlMapping.
     */
    @Test
    public void returnsYamlMapping() {
        Map<AbstractYamlNode, AbstractYamlNode> mappings = new HashMap<>();
        mappings.put(
            new Scalar("key3"), Mockito.mock(AbstractYamlSequence.class)
        );
        mappings.put(
            new Scalar("key1"), Mockito.mock(AbstractYamlMapping.class)
        );
        RtYamlMapping map = new RtYamlMapping(mappings);
        
        MatcherAssert.assertThat(
            map.yamlMapping("key1"), Matchers.notNullValue()
        );
    }

    /**
     * RtYamlMapping can return a YamlSequence.
     */
    @Test
    public void returnsYamlSequence() {
        Map<AbstractYamlNode, AbstractYamlNode> mappings = new HashMap<>();
        mappings.put(
            new Scalar("key3"), Mockito.mock(AbstractYamlSequence.class)
        );
        mappings.put(
            new Scalar("key1"), Mockito.mock(AbstractYamlMapping.class)
        );
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.yamlSequence("key3"), Matchers.notNullValue()
        );
    }

    /**
     * RtYamlMapping can return null if the specified key is missig.
     */
    @Test
    public void returnsNullOnMissingKey() {
        Map<AbstractYamlNode, AbstractYamlNode> mappings = new HashMap<>();
        mappings.put(
            new Scalar("key3"), Mockito.mock(AbstractYamlSequence.class)
        );
        mappings.put(
            new Scalar("key1"), Mockito.mock(AbstractYamlMapping.class)
        );
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
            new HashMap<AbstractYamlNode, AbstractYamlNode>()
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
            new HashMap<AbstractYamlNode, AbstractYamlNode>()
        );
        RtYamlSequence seq = new RtYamlSequence(
            new LinkedList<AbstractYamlNode>()
        );
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
        Map<AbstractYamlNode, AbstractYamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key3"), new Scalar("value1"));
        mappings.put(new Scalar("key2"), new Scalar("value2"));
        mappings.put(new Scalar("key1"), new Scalar("value3"));
        AbstractYamlMapping firstmap = new RtYamlMapping(mappings);
        AbstractYamlMapping secondmap = new RtYamlMapping(mappings);
        mappings.put(new Scalar("key4"), new Scalar("value4"));
        AbstractYamlMapping thirdmap = new RtYamlMapping(mappings);

        Map<AbstractYamlNode, AbstractYamlNode> othermappings = new HashMap<>();
        othermappings.put(
            new Scalar("architect"),
            Mockito.mock(AbstractYamlSequence.class)
        );
        othermappings.put(new Scalar("dev"), firstmap);
        othermappings.put(new Scalar("tester"), secondmap);
        AbstractYamlMapping fourthmap = new RtYamlMapping(othermappings);
        
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
        AbstractYamlMapping yaml = Yaml.createYamlMappingBuilder()
            .add("name", "camel")
            .add("architect", "mihai")
            .add("developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("sherif")
                    .add("salikjan")
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
        AbstractYamlMapping yaml = Yaml.createYamlMappingBuilder()
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("Detroit Tigers")
                    .add("Chicago cubs")
                    .build(),
                Yaml.createYamlSequenceBuilder()
                    .add("2001-07-23")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("New York Yankees")
                    .add("Atlanta Braves")
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
