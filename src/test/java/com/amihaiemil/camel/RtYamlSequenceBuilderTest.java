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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for {@link RtYamlSequenceBuilder}.
 * @author Salavat.Yalalov (s.yalalov@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class RtYamlSequenceBuilderTest {
    /**
     * RtYamlSequence can add a String.
     */
    @Test
    public void addsString() {
        YamlSequenceBuilder sequenceBuilder = new RtYamlSequenceBuilder();
        YamlSequenceBuilder withAdded = sequenceBuilder.add("value");
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            sequenceBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlSequence can add a YamlNode.
     */
    @Test
    public void addsYamlNode() {
        YamlSequenceBuilder sequenceBuilder = new RtYamlSequenceBuilder();
        YamlSequenceBuilder withAdded =
            sequenceBuilder.add(new Scalar("value"));
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            sequenceBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlSequence can build a YamlSequence.
     */
    @Test
    public void buildsYamlSequence() {
        YamlSequenceBuilder sequenceBuilder = new RtYamlSequenceBuilder();
        List<AbstractYamlNode> devs = new LinkedList<>();
        devs.add(new Scalar("amihaiemil"));
        devs.add(new Scalar("salikjan"));
        AbstractYamlSequence sequence = sequenceBuilder
            .add("amihaiemil")
            .add(new RtYamlSequence(devs))
            .build();

        MatcherAssert.assertThat(sequence, Matchers.notNullValue());
        MatcherAssert.assertThat(
            sequence.string(0), Matchers.equalTo("amihaiemil")
        );

        MatcherAssert.assertThat(
            sequence.yamlSequence(1).children().size(),
            Matchers.equalTo(2)
        );
    }
}
