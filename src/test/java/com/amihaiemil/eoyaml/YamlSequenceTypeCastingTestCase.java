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

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for the convenience type-casting methods
 * in {@link YamlSequence}.
 * @checkstyle JavadocMethod (300 lines)
 * @author Mihai Andronache
 * @version $Id$
 * @since 3.1.1
 */
public final class YamlSequenceTypeCastingTestCase {

    @Test
    public void returnsInteger() {
        MatcherAssert.assertThat(
            this.sequence().integer(0),
            Matchers.is(123)
        );
    }
    
    @Test
    public void returnsFloat() {
        MatcherAssert.assertThat(
            this.sequence().floatNumber(1),
            Matchers.is(3.54F)
        );
    }
    
    @Test
    public void returnsDouble() {
        MatcherAssert.assertThat(
            this.sequence().doubleNumber(2),
            Matchers.is(2.05)
        );
    }
    
    @Test
    public void returnsLong() {
        MatcherAssert.assertThat(
            this.sequence().longNumber(3),
            Matchers.is(32165498L)
        );
    }
    
    @Test
    public void returnsDate() {
        MatcherAssert.assertThat(
            this.sequence().date(4),
            Matchers.is(LocalDate.parse("2007-12-03"))
        );
    }
    
    @Test
    public void returnsDateTime() {
        MatcherAssert.assertThat(
            this.sequence().dateTime(5),
            Matchers.is(LocalDateTime.parse("2007-12-03T10:15:30"))
        );
    }
    
    /**
     * Get a YamlSequence for test.
     * @return YamlSequence.
     */
    private YamlSequence sequence() {
        YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("123")
            .add("3.54")
            .add("2.05")
            .add("32165498")
            .add("2007-12-03")
            .add("2007-12-03T10:15:30")
            .build();
        return sequence;
    }
}
