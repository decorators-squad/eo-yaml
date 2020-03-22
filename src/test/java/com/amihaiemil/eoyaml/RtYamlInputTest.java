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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link RtYamlInput}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.1
 *
 */
public final class RtYamlInputTest {

    /**
     * YamlMapping can be read without its comments.
     * @throws Exception If something goes wrong
     */
    @Test
    public void readsMappingWithoutComments() throws Exception {
        YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("name", "eo-yaml")
            .add("architect", "mihai")
            .add("developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            ).build();
        YamlMapping read = new RtYamlInput(
            new FileInputStream(
                new File("src/test/resources/commentedMapping.yml")
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(
            read.string("name"),
            Matchers.equalTo(expected.string("name"))
        );
        MatcherAssert.assertThat(
            read.string("architect"),
            Matchers.equalTo(expected.string("architect"))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").size(),
            Matchers.equalTo(expected.yamlSequence("developers").size())
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(0),
            Matchers.equalTo(expected.yamlSequence("developers").string(0))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(1),
            Matchers.equalTo(expected.yamlSequence("developers").string(1))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(2),
            Matchers.equalTo(expected.yamlSequence("developers").string(2))
        );
        MatcherAssert.assertThat(
            read, Matchers.equalTo(expected)
        );
    }

    /**
     * RtYamlnput can read and indent a complex mapping.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsAndIndentsComplexMapping() throws Exception {
        YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("first", "value1")
            .add("second", "value2")
            .add("third",
                Yaml.createYamlSequenceBuilder()
                    .add("singleElementSequence")
                    .build()
            ).add(
                Yaml.createYamlSequenceBuilder()
                    .add("sequence")
                    .add("key")
                    .build(),
                "scalar"
            ).add(
                Yaml.createYamlMappingBuilder()
                    .add("map", "asKey")
                    .add("map1", "asKey2")
                    .build(),
                Yaml.createYamlMappingBuilder()
                    .add("someMapping",  "value")
                    .build()
            )
            .build();
        System.out.println(expected);
        YamlMapping actual = new RtYamlInput(
            new FileInputStream(
                new File("src/test/resources/indentedComplexMapping.yml")
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * RtYamlnput can read and indent a complex sequence.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsAndIndentsComplexSequence() throws Exception {
        final YamlSequence expected = Yaml.createYamlSequenceBuilder()
            .add("amihaiemil")
            .add("salikjan")
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("element1")
                    .add("element2")
                    .add("element3")
                    .build()
            )
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("rule1", "test")
                    .add("rule2", "test2")
                    .add("rule3", "test3")
                    .build()
            )
            .build();
        final YamlSequence actual = new RtYamlInput(
            new FileInputStream(
                new File("src/test/resources/complexSequence.yml")
            )
        ).readYamlSequence();
        MatcherAssert.assertThat(
            actual.indent(0), Matchers.equalTo(expected.indent(0))
        );
    }

    /**
     * At the moment, when reading a mapping, we should ignore any YAML
     * Directive or Marker line, such as "%YAML 1.2" or "---" or "...".
     * @throws Exception if something is wrong.
     */
    @Test
    public void readsMappingWithoutDirectivesAndMarkers() throws Exception {
        final YamlMapping mapping = new RtYamlInput(
            new FileInputStream(
                new File("src/test/resources/mapping_ignore_directives.yml")
            )
        ).readYamlMapping();
        final Set<YamlNode> keys = mapping.keys();
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(2));
        MatcherAssert.assertThat(
            keys.contains(new BuiltPlainScalar("palette")),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            keys.contains(new BuiltPlainScalar("workspace")),
            Matchers.is(Boolean.TRUE)
        );

        final YamlMapping palette = mapping.yamlMapping("palette");
        MatcherAssert.assertThat(palette.keys().size(), Matchers.is(12));
        MatcherAssert.assertThat(palette.values().size(), Matchers.is(12));
        MatcherAssert.assertThat(
            palette.string("light_gray"),
            Matchers.is("#484848")
        );
        MatcherAssert.assertThat(
            palette.string("magenta"),
            Matchers.is("rgb(255, 102, 255)")
        );

        final YamlMapping workspace = mapping.yamlMapping("workspace");
        MatcherAssert.assertThat(workspace.keys().size(), Matchers.is(3));
        MatcherAssert.assertThat(workspace.values().size(), Matchers.is(3));
        MatcherAssert.assertThat(
            workspace.string("background"),
            Matchers.is("gray")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("elements")
                .yamlMapping("devices")
                .yamlMapping("pins")
                .yamlSequence("active")
                .string(1),
            Matchers.is("hsl(163, 45%, 100%)")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("guides").string("grid"),
            Matchers.is("stroke rgba(255, 255, 255, 0.3)")
        );
    }

    /**
     * At the moment, when reading a sequence, we should ignore any YAML
     * Directive or Marker line, such as "%YAML 1.2" or "---" or "...".
     * @throws Exception if something is wrong.
     */
    @Test
    public void readsSequenceWithoutDirectivesAndMarkers() throws Exception {
        final YamlSequence sequence = new RtYamlInput(
            new FileInputStream(
                new File("src/test/resources/sequence_ignore_directives.yml")
            )
        ).readYamlSequence();
        final YamlMapping first = sequence.yamlMapping(0);
        MatcherAssert.assertThat(
            first.keys().size(), Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            first.values().size(), Matchers.equalTo(1)
        );

        final YamlMapping palette = first.yamlMapping("palette");
        MatcherAssert.assertThat(palette.keys().size(), Matchers.is(12));
        MatcherAssert.assertThat(palette.values().size(), Matchers.is(12));
        MatcherAssert.assertThat(
            palette.string("gray"),
            Matchers.is("#404040")
        );
        MatcherAssert.assertThat(
            palette.string("magenta"),
            Matchers.is("rgb(255, 102, 255)")
        );

        final YamlMapping second = sequence.yamlMapping(1);
        MatcherAssert.assertThat(
            second.keys().size(), Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            second.values().size(), Matchers.equalTo(1)
        );

        final YamlMapping workspace = second.yamlMapping("workspace");
        MatcherAssert.assertThat(workspace.keys().size(), Matchers.is(3));
        MatcherAssert.assertThat(workspace.values().size(), Matchers.is(3));
        MatcherAssert.assertThat(
            workspace.string("background"),
            Matchers.is("gray")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("elements")
                .yamlMapping("devices")
                .yamlMapping("pins")
                .yamlSequence("active")
                .string(1),
            Matchers.is("hsl(163, 45%, 100%)")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("guides").string("grid"),
            Matchers.is("stroke rgba(255, 255, 255, 0.3)")
        );
    }

    /**
     * A YamlSequence in block style can be read.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsBlockSequence() throws Exception {
        YamlSequence expected = Yaml.createYamlSequenceBuilder()
            .add("apples")
            .add("pears")
            .add("peaches")
            .build();
        YamlSequence read = new RtYamlInput(
            new ByteArrayInputStream(
                new StringBuilder()
                    .append("---").append(System.lineSeparator())
                    .append("- apples").append(System.lineSeparator())
                    .append("- pears").append(System.lineSeparator())
                    .append("- peaches").append(System.lineSeparator())
                    .toString()
                    .getBytes()
            )
        ).readYamlSequence();

        MatcherAssert.assertThat(read, Matchers.equalTo(expected));
    }

    /**
     * Reading of YamlStream is not yet implemented, an exception should
     * be thrown.
     * @throws Exception If something goes wrong.
     */
    @Test (expected = UnsupportedOperationException.class)
    public void readingYamlStreamNotImplemented() throws Exception {
        final YamlInput input = Yaml.createYamlInput("");
        input.readYamlStream();
    }

}
