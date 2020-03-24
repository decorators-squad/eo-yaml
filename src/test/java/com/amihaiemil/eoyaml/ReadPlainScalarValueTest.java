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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@ling ReadPlainScalarValue}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.3
 */
public final class ReadPlainScalarValueTest {

    /**
     * ReadPlainScalarValue can return the scalar's value from a mapping line.
     */
    @Test
    public void returnsValueFromMappingLine() {
        final Scalar scalar = new ReadPlainScalarValue(
            new RtYamlLine("key: value", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("value"));
    }

    /**
     * ReadPlainScalarValue can return the scalar's value from a sequence line.
     */
    @Test
    public void returnsValueFromSequenceLine() {
        final Scalar scalar = new ReadPlainScalarValue(
            new RtYamlLine("- value", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("value"));
    }

    /**
     * ReadPlainScalarValue will return the plain scalar
     */
    @Test
    public void returnsPlainScalar() {
        final YamlLine line = new RtYamlLine("value", 0);
        final Scalar scalar = new ReadPlainScalarValue(line);
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("value"));
    }

    /**
     * ReadPlainScalarValue will return empty string if the line is empty.
     * Should never be the case in real practice, however, because empty
     * lines are omitted when read.
     */
    @Test
    public void returnsEmptyScalar() {
        final YamlLine line = new RtYamlLine("", 0);
        final Scalar scalar = new ReadPlainScalarValue(line);
        MatcherAssert.assertThat(scalar.value(), Matchers.isEmptyString());
    }
}
