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
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RtYamlMappingBuilder}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class RtYamlMappingBuilderTest {

    /**
     * RtYamlMappingBuilder can add a K:V pair of Strings.
     */
    @Test
    public void addsPairOfStrings() {
        YamlMappingBuilder mappingBuilder = new RtYamlMappingBuilder();
        YamlMappingBuilder withAdded = mappingBuilder.add("key", "value");
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            mappingBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlMappingBuilder can add a K:V pair of String and YamlNode.
     */
    @Test
    public void addsPairOfStringYamlNode() {
        YamlMappingBuilder mappingBuilder = new RtYamlMappingBuilder();
        YamlMappingBuilder withAdded = mappingBuilder.add(
            "key", new PlainStringScalar("value")
        );
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            mappingBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlMappingBuilder can add a K:V pair of String and YamlNode.
     */
    @Test
    public void addsPairOfYamlNodeString() {
        YamlMappingBuilder mappingBuilder = new RtYamlMappingBuilder();
        YamlMappingBuilder withAdded = mappingBuilder.add(
            new PlainStringScalar("key"), "value"
        );
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            mappingBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlMappingBuilder can add a K:V pair of YamlNode.
     */
    @Test
    public void addsPairOfYamlNodes() {
        YamlMappingBuilder mappingBuilder = new RtYamlMappingBuilder();
        YamlMappingBuilder withAdded = mappingBuilder.add(
            new PlainStringScalar("key"), new PlainStringScalar("value")
        );
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            mappingBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlMappingBuilder can build a YamlMapping.
     */
    @Test
    public void buildsYamlMapping() {
        YamlMappingBuilder mappingBuilder = new RtYamlMappingBuilder();
        List<YamlNode> devs = new ArrayList<>();
        devs.add(new PlainStringScalar("amihaiemil"));
        devs.add(new PlainStringScalar("salikjan"));
        YamlMapping mapping = mappingBuilder
            .add("architect", "amihaiemil")
            .add("developers", new RtYamlSequence(devs))
            .build();
        MatcherAssert.assertThat(mapping, Matchers.notNullValue());
        MatcherAssert.assertThat(
            mapping.string("architect"), Matchers.equalTo("amihaiemil")
        );
        MatcherAssert.assertThat(
            mapping.yamlSequence("developers").values().size(),
            Matchers.equalTo(2)
        );
    }

    /**
     * RtYamlMappingBuilder can build a YamlMapping with a comment
     * referring to it.
     */
    @Test
    public void buildsYamlMappingWithComment() {
        final YamlMapping mapping = new RtYamlMappingBuilder()
            .add("key", "value")
            .add("key1", "value1")
            .build("some test mapping");
        final Comment com = mapping.comment();
        MatcherAssert.assertThat(com.yamlNode(), Matchers.is(mapping));
        MatcherAssert.assertThat(
            com.value(),
            Matchers.equalTo("some test mapping")
        );
    }

    /**
     * RtYamlMappingBuilder should complain when a null key is provided.
     */
    @Test
    public void complainsOnNullKey() {
        final YamlNode key = null;
        try {
            final YamlMapping mapping = new RtYamlMappingBuilder()
                .add(key, "value")
                .build("some test mapping");
            Assert.fail("IAE was expected!");
        } catch (final IllegalArgumentException ex) {
            MatcherAssert.assertThat(
                ex.getMessage(),
                Matchers.equalTo(
                    "The key in YamlMapping cannot be null or empty!"
                )
            );
        }
    }

    /**
     * RtYamlMappingBuilder should complain when an empty key is provided.
     */
    @Test
    public void complainsOnEmptyKey() {
        final BaseYamlNode key = Mockito.mock(BaseYamlNode.class);
        Mockito.when(key.isEmpty()).thenReturn(true);
        try {
            final YamlMapping mapping = new RtYamlMappingBuilder()
                .add(key, "value")
                .build("some test mapping");
            Assert.fail("IAE was expected!");
        } catch (final IllegalArgumentException ex) {
            MatcherAssert.assertThat(
                ex.getMessage(),
                Matchers.equalTo(
                    "The key in YamlMapping cannot be null or empty!"
                )
            );
        }
    }

}
