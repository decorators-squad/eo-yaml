/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
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

import static com.amihaiemil.eoyaml.Comment.UNKNOWN_LINE_NUMBER;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link ReflectedYamlScalar}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.3.3
 */
public final class ReflectedYamlScalarTest {

    /**
     * A reflected scalar has no comment.
     */
    @Test
    public void returnsEmptyComment() {
        final Scalar scalar = new ReflectedYamlScalar("scalar");
        MatcherAssert.assertThat(
            scalar.comment().yamlNode(),
            Matchers.is(scalar)
        );
        MatcherAssert.assertThat(
            scalar.comment().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            scalar.comment().number(),
            Matchers.is(UNKNOWN_LINE_NUMBER)
        );
    }

    /**
     * It can represent different scalar objects.
     */
    @Test
    public void reflectsDifferentScalarObjects() {
        MatcherAssert.assertThat(
            new ReflectedYamlScalar("scalar").value(),
            Matchers.equalTo("scalar")
        );
        MatcherAssert.assertThat(
            new ReflectedYamlScalar(233).value(),
            Matchers.equalTo("233")
        );
        MatcherAssert.assertThat(
            new ReflectedYamlScalar(10.5).value(),
            Matchers.equalTo("10.5")
        );
        MatcherAssert.assertThat(
            new ReflectedYamlScalar(10.34F).value(),
            Matchers.equalTo("10.34")
        );
    }

    /**
     * The scalar's value is null if the passed object is null.
     */
    @Test
    public void reflectsNull() {
        MatcherAssert.assertThat(
            new ReflectedYamlScalar(null).value(),
            Matchers.nullValue()
        );
    }
}
