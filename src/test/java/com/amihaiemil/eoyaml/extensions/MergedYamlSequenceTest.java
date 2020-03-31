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
package com.amihaiemil.eoyaml.extensions;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlSequence;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link MergedYamlSequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.1.0
 */
public final class MergedYamlSequenceTest {

    /**
     * It should complain when both arguments are null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void complainsOnTwoNullSequences() {
        final YamlSequence original = null;
        final YamlSequence changed = null;
        final YamlSequence boom = new MergedYamlSequence(original, changed);
    }

    /**
     * Simply adds the elements in one YamlSequence.
     */
    @Test
    public void addsChangedToOriginal() {
        final YamlSequence expected = Yaml
            .createYamlSequenceBuilder()
            .add("first")
            .add("second")
            .add("third")
            .add("fourth")
            .add("fifth")
            .build();
        final YamlSequence original = Yaml
            .createYamlSequenceBuilder()
            .add("first")
            .add("second")
            .add("third")
            .build();
        final YamlSequence changed = Yaml
            .createYamlSequenceBuilder()
            .add("fourth")
            .add("fifth")
            .build();
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, changed),
            Matchers.equalTo(expected)
        );
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, ()->changed),
            Matchers.equalTo(expected)
        );
    }

    /**
     * It can override the values at the same indices.
     */
    @Test
    public void overridesIndicesInOriginal() {
        final YamlSequence expected = Yaml
            .createYamlSequenceBuilder()
            .add("fourth")
            .add("fifth")
            .add("third")
            .build();
        final YamlSequence original = Yaml
            .createYamlSequenceBuilder()
            .add("first")
            .add("second")
            .add("third")
            .build();
        final YamlSequence changed = Yaml
            .createYamlSequenceBuilder()
            .add("fourth")
            .add("fifth")
            .build();
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, changed, true),
            Matchers.equalTo(expected)
        );
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, ()->changed, true),
            Matchers.equalTo(expected)
        );
    }

    /**
     * It should represent the Original sequence if the changed one
     * is null.
     */
    @Test
    public void representsOriginalIfChangedIsNullOrEmpty() {
        final YamlSequence original = Yaml
            .createYamlSequenceBuilder().add("value").build();
        final YamlSequence changed = null;
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, changed),
            Matchers.equalTo(original)
        );
        MatcherAssert.assertThat(
            new MergedYamlSequence(
                original,
                Yaml.createYamlSequenceBuilder().build()
            ),
            Matchers.equalTo(original)
        );
    }

    /**
     * It should represent the Changed sequence if the original one
     * is null.
     */
    @Test
    public void representsChangeIfOriginalIsNullOrEmpty() {
        final YamlSequence original = null;
        final YamlSequence changed = Yaml
            .createYamlSequenceBuilder().add("value").build();
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, changed),
            Matchers.equalTo(changed)
        );
        MatcherAssert.assertThat(
            new MergedYamlSequence(
                Yaml.createYamlSequenceBuilder().build(),
                changed
            ),
            Matchers.equalTo(changed)
        );
    }

    /**
     * Null elements should also be merged.
     */
    @Test
    public void addsNullElementsToOriginal() {
        final String nullValue = null;
        final YamlSequence expected = Yaml
            .createYamlSequenceBuilder()
            .add("first")
            .add("second")
            .add("third")
            .add("fourth")
            .add(nullValue)
            .build();
        final YamlSequence original = Yaml
            .createYamlSequenceBuilder()
            .add("first")
            .add("second")
            .add("third")
            .build();
        final YamlSequence changed = Yaml
            .createYamlSequenceBuilder()
            .add("fourth")
            .add(nullValue)
            .build();
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, changed),
            Matchers.equalTo(expected)
        );
        MatcherAssert.assertThat(
            new MergedYamlSequence(original, ()->changed),
            Matchers.equalTo(expected)
        );
    }

}
