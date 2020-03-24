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

import java.util.stream.Stream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RtYamlStreamBuilder}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.1
 */
public final class RtYamlStreamBuilderTest {

    /**
     * RtYamlStreamBuilder can add a YamlSequence.
     */
    @Test
    public void addsYamlSequence() {
        YamlStreamBuilder streamBuilder = new RtYamlStreamBuilder();
        YamlStreamBuilder withAdded = streamBuilder.add(
            Mockito.mock(YamlSequence.class)
        );
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            streamBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlStreamBuilder can add a YamlMapping.
     */
    @Test
    public void addsYamlMapping() {
        YamlStreamBuilder streamBuilder = new RtYamlStreamBuilder();
        YamlStreamBuilder withAdded = streamBuilder.add(
            Mockito.mock(YamlMapping.class)
        );
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            streamBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlStreamBuilder can add a Scalar.
     */
    @Test
    public void addsScalar() {
        YamlStreamBuilder streamBuilder = new RtYamlStreamBuilder();
        YamlStreamBuilder withAdded = streamBuilder.add(
            new PlainStringScalar("test")
        );
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            streamBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlStreamBuilder can add a YamlNode.
     */
    @Test
    public void addsYamlNode() {
        YamlStreamBuilder streamBuilder = new RtYamlStreamBuilder();
        YamlStreamBuilder withAdded = streamBuilder.add(
            Mockito.mock(YamlNode.class)
        );
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            streamBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlStreamBuilder can build the YamlStream.
     */
    @Test
    public void buildsYamlStream() {
        final YamlStream stream = new RtYamlStreamBuilder()
            .add(Mockito.mock(YamlMapping.class))
            .add(Mockito.mock(YamlSequence.class))
            .add(Mockito.mock(YamlNode.class))
            .build();

        MatcherAssert.assertThat(stream, Matchers.notNullValue());
        MatcherAssert.assertThat(stream, Matchers.instanceOf(Stream.class));
        MatcherAssert.assertThat(
              stream.values(), Matchers.iterableWithSize(3)
        );
    }

}
