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

import java.io.ByteArrayInputStream;
import java.io.File;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonValue;

/**
 * Unit tests for {@link Yaml}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class YamlTest {

    /**
     * Yaml can create an immutable YamlMappingBuilder.
     */
    @Test
    public void createsYamlMappingBuilder() {
        MatcherAssert.assertThat(
            Yaml.createYamlMappingBuilder(),
            Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.instanceOf(RtYamlMappingBuilder.class)
            )
        );
    }

    /**
     * Yaml can create a mutable YamlMappingBuilder.
     */
    @Test
    public void createsMutableYamlMappingBuilder() {
        MatcherAssert.assertThat(
            Yaml.createMutableYamlMappingBuilder(),
            Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.instanceOf(MutableYamlMappingBuilder.class)
            )
        );
    }

    /**
     * Yaml can create an immutable YamlSequenceBuilder.
     */
    @Test
    public void createsYamlSequenceBuilder() {
        MatcherAssert.assertThat(
            Yaml.createYamlSequenceBuilder(),
            Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.instanceOf(RtYamlSequenceBuilder.class)
            )
        );
    }

    /**
     * Yaml can create a mutable YamlSequenceBuilder.
     */
    @Test
    public void createsMutablYamlSequenceBuilder() {
        MatcherAssert.assertThat(
            Yaml.createMutableYamlSequenceBuilder(),
            Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.instanceOf(MutableYamlSequenceBuilder.class)
            )
        );
    }

    /**
     * Yaml can create a YamlScalarBuilder.
     */
    @Test
    public void createsYamlScalarBuilder() {
        MatcherAssert.assertThat(
            Yaml.createYamlScalarBuilder(), Matchers.notNullValue()
        );
    }

    /**
     * Yaml can create a YamlStreamBuilder.
     */
    @Test
    public void createsYamlStreamBuilder() {
        MatcherAssert.assertThat(
            Yaml.createYamlStreamBuilder(), Matchers.notNullValue()
        );
    }

    /**
     * Yaml can create a YamlInput from a File.
     * @throws Exception if something goes wrong
     */
    @Test
    public void createsYamlInputFromFile() throws Exception {
        MatcherAssert.assertThat(
            Yaml.createYamlInput(
                new File("src/test/resources/simpleMapping.yml")
            ),
            Matchers.notNullValue()
        );
    }

    /**
     * Yaml can create a YamlInput from an InputStream.
     */
    @Test
    public void createsYamlInputFromInputStream() {
        MatcherAssert.assertThat(
            Yaml.createYamlInput(
                new ByteArrayInputStream("yaml: test".getBytes())
            ),
            Matchers.notNullValue()
        );
    }

    /**
     * Yaml can create a YamlInput from a String.
     */
    @Test
    public void createsYamlInputFromString() {
        MatcherAssert.assertThat(
            Yaml.createYamlInput("yaml: test"), Matchers.notNullValue()
        );
    }

    /**
     * Yaml can create an YamlMapping from a json object.
     */
    @Test
    public void createsYamlMappingFromJsonObject() {
        MatcherAssert.assertThat(
            Yaml.fromJsonObject(Json.createObjectBuilder().build()),
            Matchers.notNullValue()
        );
    }

    /**
     * Yaml can create an YamlMapping from a json array.
     */
    @Test
    public void createsYamlMappingFromJsonArray() {
        MatcherAssert.assertThat(
            Yaml.fromJsonArray(JsonValue.EMPTY_JSON_ARRAY),
            Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.instanceOf(JsonYamlSequence.class)
            )
        );
    }
}
