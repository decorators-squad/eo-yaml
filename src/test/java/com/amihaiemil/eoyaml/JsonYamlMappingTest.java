/**
 * Copyright (c) 2016-2023, Mihai Emil Andronache
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

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Unit tests for {@link JsonYamlMapping}.
 * @author criske
 * @version $Id$
 * @since 5.1.7
 */
public final class JsonYamlMappingTest {

    /**
     * YamlMapping can map a json object to yaml object.
     */
    @Test
    public void canMapJsonObject(){
        final JsonObject json = Json.createObjectBuilder()
            .add("architect", "mihai")
            .add("developers",
                Json.createArrayBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add("lib",
                Json.createObjectBuilder()
                    .add("name", "eo-yaml")
                    .add("full-name", "")
                    .add("version", 5)
                    .build()
            )
            .add("notes", Json.createArrayBuilder().build())
            .add("latest", false)
            .build();
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("architect", "mihai")
            .add("developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add("lib",
                Yaml.createYamlMappingBuilder()
                    .add("name", "eo-yaml")
                    .add("full-name", "")
                    .add("version", "5")
                    .build()
            )
            .add("notes",  Yaml.createYamlSequenceBuilder().build())
            .add("latest", "false")
            .build();
        final YamlMapping jsonMapping = new JsonYamlMapping(json);
        MatcherAssert.assertThat(
            jsonMapping.string("architect"),
            Matchers.equalTo(expected.string("architect"))
        );
        MatcherAssert.assertThat(
            jsonMapping.value("lib").asMapping().string("name"),
            Matchers.equalTo(expected.value("lib").asMapping()
                .string("name"))
        );
        MatcherAssert.assertThat(
            jsonMapping.value("lib").asMapping().string("full-name"),
            Matchers.equalTo(expected.value("lib").asMapping()
                .string("full-name"))
        );
        MatcherAssert.assertThat(
            jsonMapping.value("lib").asMapping().integer("version"),
            Matchers.equalTo(expected.value("lib").asMapping()
                .integer("version"))
        );
        MatcherAssert.assertThat(
            jsonMapping.yamlSequence("notes").size(),
            Matchers.equalTo(expected.yamlSequence("notes").size())
        );
        MatcherAssert.assertThat(
            jsonMapping.yamlSequence("developers").size(),
            Matchers.equalTo(expected.yamlSequence("developers").size())
        );
        MatcherAssert.assertThat(
            jsonMapping.yamlSequence("developers").size(),
            Matchers.equalTo(expected.yamlSequence("developers").size())
        );
        MatcherAssert.assertThat(
            jsonMapping.yamlSequence("developers").string(0),
            Matchers.equalTo(expected.yamlSequence("developers").string(0))
        );
        MatcherAssert.assertThat(
            jsonMapping.yamlSequence("developers").string(1),
            Matchers.equalTo(expected.yamlSequence("developers").string(1))
        );
        MatcherAssert.assertThat(
            jsonMapping.yamlSequence("developers").string(2),
            Matchers.equalTo(expected.yamlSequence("developers").string(2))
        );
        MatcherAssert.assertThat(
            jsonMapping, Matchers.equalTo(expected)
        );
    }

    /**
     * YamlMapping can map an empty json object to yaml object.
     */
    @Test
    public void canMapEmptyJsonObject(){
        final JsonObject json = Json.createObjectBuilder().build();
        final YamlMapping expected = Yaml.createYamlMappingBuilder().build();
        MatcherAssert.assertThat(
            new JsonYamlMapping(json), Matchers.equalTo(expected)
        );
    }

    /**
     * YamlMapping has empty Comment.
     */
    @Test
    public void hasEmptyComment(){
        final Comment comment = new JsonYamlMapping(Json
            .createObjectBuilder()
            .build())
            .comment();
        MatcherAssert.assertThat(comment.yamlNode(), Matchers
            .instanceOf(JsonYamlMapping.class));
        MatcherAssert.assertThat(comment.value(), Matchers
            .isEmptyString());
    }
}