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
import org.junit.Test;

/**
 * Unit tests for {@link RtYamlLine}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class RtYamlLineTest {

    /**
     * RtYamlLine knows its number.
     */
    @Test
    public void knowsNumber() {
        YamlLine line = new RtYamlLine("this line", 12);
        MatcherAssert.assertThat(line.number(), Matchers.is(12));
    }

    /**
     * RtYamlLine knows if it has a nested YamlNode after it.
     */
    @Test
    public void precedesYamlNode() {
        MatcherAssert.assertThat(
            new RtYamlLine("this:", 12).hasNestedNode(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            new RtYamlLine("this: |> ", 12).hasNestedNode(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            new RtYamlLine("this: |- ", 12).hasNestedNode(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            new RtYamlLine("this: value", 12).hasNestedNode(),
            Matchers.is(false)
        );
    }

    /**
     * RtYamlLine can trim itself.
     */
    @Test
    public void trimmsItself() {
        YamlLine line = new RtYamlLine("   this: line   ", 10);
        MatcherAssert.assertThat(
            line.trimmed(), Matchers.equalTo("this: line")
        );
    }

    /**
     * RtYamlLine returns indentation.
     */
    @Test
    public void knowsIndentation() {
        YamlLine line = new RtYamlLine("this: line", 5);
        MatcherAssert.assertThat(line.indentation(), Matchers.is(0));
    }
}
