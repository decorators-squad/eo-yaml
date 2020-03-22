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

import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.LinkedList;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link BuiltPlainScalar}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class BuiltPlainScalarTest {

    /**
     * Scalar can return its own value.
     */
    @Test
    public void returnsValue() {
        final String val = "test scalar value";
        final BuiltPlainScalar scl = new BuiltPlainScalar(val);
        MatcherAssert.assertThat(scl.value(), Matchers.equalTo(val));
    }

    /**
     * A Scalar shouldn't have any values nodes.
     */
    @Test
    public void hasNoValues() {
        final String val = "test scalar value";
        final BuiltPlainScalar scl = new BuiltPlainScalar(val);
        MatcherAssert.assertThat(
            scl.values(), Matchers.emptyIterable()
        );
    }

    /**
     * Make sure that equals and hash code are reflexive
     * and symmetric.
     */
    @Test
    public void equalsAndHashCode() {
        final String val = "test scalar value";
        final BuiltPlainScalar firstScalar = new BuiltPlainScalar(val);
        final BuiltPlainScalar secondScalar = new BuiltPlainScalar(val);

        MatcherAssert.assertThat(firstScalar, Matchers.equalTo(secondScalar));
        MatcherAssert.assertThat(secondScalar, Matchers.equalTo(firstScalar));

        MatcherAssert.assertThat(firstScalar, Matchers.equalTo(firstScalar));
        MatcherAssert.assertThat(firstScalar,
            Matchers.not(Matchers.equalTo(null)));

        MatcherAssert.assertThat(
            firstScalar.hashCode() == secondScalar.hashCode(), is(true)
        );
    }

    /**
     * Scalar can compare itself to another Scalar.
     */
    @Test
    public void comparesToScalar() {
        BuiltPlainScalar first = new BuiltPlainScalar("java");
        BuiltPlainScalar second = new BuiltPlainScalar("java");
        BuiltPlainScalar digits = new BuiltPlainScalar("123");
        BuiltPlainScalar otherDigits = new BuiltPlainScalar("124");
        MatcherAssert.assertThat(first.compareTo(first), Matchers.equalTo(0));
        MatcherAssert.assertThat(first.compareTo(second), Matchers.equalTo(0));
        MatcherAssert.assertThat(
            first.compareTo(digits), Matchers.greaterThan(0)
        );
        MatcherAssert.assertThat(
            first.compareTo(null), Matchers.greaterThan(0)
        );
        MatcherAssert.assertThat(
            digits.compareTo(otherDigits), Matchers.lessThan(0)
        );
    }

    /**
     * Scalar can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        BuiltPlainScalar first = new BuiltPlainScalar("java");
        RtYamlMapping map = new RtYamlMapping(
            new HashMap<YamlNode, YamlNode>()
        );
        MatcherAssert.assertThat(first.compareTo(map), Matchers.lessThan(0));
    }

    /**
     * Scalar can compare itself to a Sequence.
     */
    @Test
    public void comparesToSequence() {
        BuiltPlainScalar first = new BuiltPlainScalar("java");
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<YamlNode>());
        MatcherAssert.assertThat(first.compareTo(seq), Matchers.lessThan(0));
    }
}
