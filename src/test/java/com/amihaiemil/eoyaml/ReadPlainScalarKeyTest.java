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
 * Unit tests for {@link ReadPlainScalarKey}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.3
 */
public final class ReadPlainScalarKeyTest {

    /**
     * ReadPlainScalarKey can return the key from a YamlLine that
     * represents a simple key: value mapping.
     */
    @Test
    public void returnsKeyWithScalarValue() {
        final Scalar scalar = new ReadPlainScalarKey(
            new RtYamlLine("key: value", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("key"));
    }

    /**
     * ReadPlainScalarKey can return the key from a YamlLine that
     * represents a mapping between a simple key and a YamlNode value.
     */
    @Test
    public void returnsKeyWithYamlNodeValue() {
        final Scalar scalar = new ReadPlainScalarKey(
            new RtYamlLine("key: ", 0)
        );
        MatcherAssert.assertThat(scalar.value(), Matchers.equalTo("key"));
    }

    /**
     * ReadPlainScalarKey will complain if the given YamlLine contains
     * the start of a complex key rather than a Scalar key.
     */
    @Test(expected = IllegalStateException.class)
    public void complainsOnStartOfComplexKey() {
        final YamlLine line = new RtYamlLine("?", 0);
        final Scalar scalar = new ReadPlainScalarKey(line);
        scalar.value();
        Assert.fail("IllegalStateException was expected.");

    }

    /**
     * ReadPlainScalarKey will complain if the given YamlLine contains
     * the end of a complex key rather than a Scalar key.
     */
    @Test(expected = IllegalStateException.class)
    public void complainsOnEndOfComplexKey() {
        final YamlLine line = new RtYamlLine(":", 0);
        final Scalar scalar = new ReadPlainScalarKey(line);
        scalar.value();
        Assert.fail("IllegalStateException was expected.");
    }

    /**
     * ReadPlainScalarKey will complain if the given YamlLine is actually
     * part of a YamlSequence.
     */
    @Test(expected = IllegalStateException.class)
    public void complainsOnLineFromSequence() {
        final YamlLine line = new RtYamlLine("- someValue", 0);
        final Scalar scalar = new ReadPlainScalarKey(line);
        scalar.value();
        Assert.fail("IllegalStateException was expected.");
    }

    /**
     * ReadPlainScalarKey will complain if the given YamlLine is empty.
     */
    @Test(expected = IllegalStateException.class)
    public void complainsOnEmptyLine() {
        final YamlLine line = new RtYamlLine("", 0);
        final Scalar scalar = new ReadPlainScalarKey(line);
        scalar.value();
        Assert.fail("IllegalStateException was expected.");
    }

    /**
     * Unit test for toString.
     */
    @Test
    public void toStringWorks() {
        final Scalar scalar = new ReadPlainScalarKey(
            new RtYamlLine("key: value", 0)
        );
        MatcherAssert.assertThat(scalar.toString(), Matchers.equalTo("key"));
    }
}
