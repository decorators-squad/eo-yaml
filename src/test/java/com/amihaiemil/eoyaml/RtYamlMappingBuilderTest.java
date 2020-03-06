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
import java.util.List;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

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
        Map<String, String> test = new HashMap<>();
        test.put("1", "2");
        test.put("2", "5");
        test.put("3", "6");
    }

    /**
     * RtYamlMappingBuilder can add a K:V pair of String and YamlNode.
     */
    @Test
    public void addsPairOfStringYamlNode() {
        YamlMappingBuilder mappingBuilder = new RtYamlMappingBuilder();
        YamlMappingBuilder withAdded = mappingBuilder.add(
            "key", new Scalar("value")
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
            new Scalar("key"), "value"
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
            new Scalar("key"), new Scalar("value")
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
        devs.add(new Scalar("amihaiemil"));
        devs.add(new Scalar("salikjan"));
        YamlMapping mapping = mappingBuilder
            .add("architect", "amihaiemil")
            .add("developers", new RtYamlSequence(devs))
            .build();
        MatcherAssert.assertThat(mapping, Matchers.notNullValue());
        MatcherAssert.assertThat(
            mapping.string("architect"), Matchers.equalTo("amihaiemil")
        );
        MatcherAssert.assertThat(
            mapping.yamlSequence("developers").children().size(),
            Matchers.equalTo(2)
        );
    }
}
