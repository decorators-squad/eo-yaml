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
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RtYamlStreamBuilder.BuiltYamlStream}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.1
 *
 */
public final class BuiltYamlStreamTest {

    /**
     * BuiltYamlStreams can hold a stream of mappings.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void builtStreamOfMappings() throws Exception {
        final YamlStream mappings = Yaml.createYamlStreamBuilder()
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("architect", "mihai")
                    .add(
                        "developers",
                        Yaml.createYamlSequenceBuilder()
                            .add("rultor")
                            .add("salikjan")
                            .add("sherif")
                            .build()
                    )
                    .add("name", "eo-yaml")
                    .build()
            )
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("architect", "vlad")
                    .add(
                        "developers",
                        Yaml.createYamlSequenceBuilder()
                            .add("andrei")
                            .build()
                    )
                    .add("name", "eo-json-impl")
                    .build()
            )
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("architect", "felicia")
                    .add(
                        "developer", "sara"
                    )
                    .add("name", "docker-java-api")
                    .build()
            )
            .build();
        MatcherAssert.assertThat(mappings, Matchers.notNullValue());
        MatcherAssert.assertThat(mappings, Matchers.instanceOf(Stream.class));
        MatcherAssert.assertThat(
            mappings.values(), Matchers.iterableWithSize(3)
        );
        MatcherAssert.assertThat(
            mappings.toString(),
            Matchers.equalTo(this.readTestResource("streamOfMappings.yml"))
        );
    }

    /**
     * BuiltYamlStreams can hold a stream of sequences.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void builtStreamOfSequences() throws Exception {
        final YamlStream sequences = Yaml.createYamlStreamBuilder()
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("andrei")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("yegor")
                    .add("paolo")
                    .add("cesar")
                    .build()
            )
            .build();
        MatcherAssert.assertThat(sequences, Matchers.notNullValue());
        MatcherAssert.assertThat(sequences, Matchers.instanceOf(Stream.class));
        MatcherAssert.assertThat(
            sequences.values(), Matchers.iterableWithSize(3)
        );
        MatcherAssert.assertThat(
            sequences.toString(),
            Matchers.equalTo(this.readTestResource("streamOfSequences.yml"))
        );
    }

    /**
     * BuiltYamlStreams can hold a stream of mixed nodes.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void builtStreamOfMixed() throws Exception {
        final YamlStream mixed = Yaml.createYamlStreamBuilder()
                .add(
                    Yaml.createYamlMappingBuilder()
                        .add("architect", "mihai")
                        .add(
                            "developers",
                            Yaml.createYamlSequenceBuilder()
                                .add("rultor")
                                .add("salikjan")
                                .add("sherif")
                                .build()
                        )
                        .add("name", "eo-yaml")
                        .build()
                )
                .add(
                    Yaml.createYamlSequenceBuilder()
                        .add("yegor")
                        .add("paolo")
                        .add("cesar")
                        .build()
                )
                .add(
                    Yaml.createYamlMappingBuilder()
                        .add("architect", "felicia")
                        .add(
                            "developer", "sara"
                        )
                        .add("name", "docker-java-api")
                        .build()
                )
                .build();
        MatcherAssert.assertThat(mixed, Matchers.notNullValue());
        MatcherAssert.assertThat(mixed, Matchers.instanceOf(Stream.class));
        MatcherAssert.assertThat(
            mixed.values(), Matchers.iterableWithSize(3)
        );
        MatcherAssert.assertThat(
            mixed.toString(),
            Matchers.equalTo(this.readTestResource("streamMixed.yml"))
        );
    }

    /**
     * According to the comparison rules, any YamlStream is always considered
     * "greater than" a scalar.
     */
    @Test
    public void greaterThanAScalar() {
        final YamlStream stream = Yaml.createYamlStreamBuilder().build();
        MatcherAssert.assertThat(
            stream.compareTo(new PlainStringScalar("test")),
            Matchers.greaterThan(0)
        );
    }

    /**
     * According to the comparison rules, any YamlStream is always considered
     * "greater than" a mapping.
     */
    @Test
    public void greaterThanAMapping() {
        final YamlStream stream = Yaml.createYamlStreamBuilder().build();
        MatcherAssert.assertThat(
            stream.compareTo(Mockito.mock(YamlMapping.class)),
            Matchers.greaterThan(0)
        );
    }

    /**
     * According to the comparison rules, any YamlStream is always considered
     * "greater than" a sequence.
     */
    @Test
    public void greaterThanASequence() {
        final YamlStream stream = Yaml.createYamlStreamBuilder().build();
        MatcherAssert.assertThat(
            stream.compareTo(Mockito.mock(YamlSequence.class)),
            Matchers.greaterThan(0)
        );
    }

    /**
     * Two YamlStreams which are empty should be equal.
     */
    @Test
    public void emptyStreamsAreEqual() {
        final YamlStream first = Yaml.createYamlStreamBuilder().build();
        final YamlStream second = Yaml.createYamlStreamBuilder().build();
        MatcherAssert.assertThat(
            first.compareTo(second),
            Matchers.equalTo(0)
        );
        MatcherAssert.assertThat(
            second.compareTo(first),
            Matchers.equalTo(0)
        );
        MatcherAssert.assertThat(
            first.equals(second),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            second.equals(first),
            Matchers.is(Boolean.TRUE)
        );
    }

    /**
     * Two identical YamlStreams are equal.
     */
    @Test
    public void streamsAreEqual() {
        final YamlStream first = Yaml.createYamlStreamBuilder()
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("andrei")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("yegor")
                    .add("paolo")
                    .add("cesar")
                    .build()
            )
            .build();
        final YamlStream second = Yaml.createYamlStreamBuilder()
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("andrei")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("yegor")
                    .add("paolo")
                    .add("cesar")
                    .build()
            )
            .build();
        MatcherAssert.assertThat(
            second.equals(first),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            first.equals(second),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            first.equals(first),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            second.equals(second),
            Matchers.is(Boolean.TRUE)
        );
    }

    /**
     * Two different YamlStreams are equal.
     */
    @Test
    public void differentStreamsAreNotEqual() {
        final YamlStream first = Yaml.createYamlStreamBuilder()
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("andrei")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("yegor")
                    .add("paolo")
                    .add("cesar")
                    .build()
            )
            .build();
        final YamlStream second = Yaml.createYamlStreamBuilder()
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("architect", "felicia")
                    .add(
                        "developer", "sara"
                    )
                    .add("name", "docker-java-api")
                    .build()
            )
            .build();
        MatcherAssert.assertThat(
            second.equals(first),
            Matchers.is(Boolean.FALSE)
        );
        MatcherAssert.assertThat(
            first.equals(second),
            Matchers.is(Boolean.FALSE)
        );
    }

    /**
     * BuiltYamlStreams can hold an empty stream.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void builtEmptyStream() throws Exception {
        final YamlStream mappings = Yaml.createYamlStreamBuilder().build();
        MatcherAssert.assertThat(mappings, Matchers.notNullValue());
        MatcherAssert.assertThat(mappings, Matchers.instanceOf(Stream.class));
        MatcherAssert.assertThat(
            mappings.values(), Matchers.iterableWithSize(0)
        );
        MatcherAssert.assertThat(
            mappings.toString(),
            Matchers.isEmptyString()
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
