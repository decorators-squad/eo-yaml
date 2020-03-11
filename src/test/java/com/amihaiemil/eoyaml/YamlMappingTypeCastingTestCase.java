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
 * in {@link YamlMapping}.
 * @checkstyle JavadocMethod (300 lines)
 * @author Mihai Andronache
 * @version $Id$
 * @since 3.1.1
 */
public final class YamlMappingTypeCastingTestCase {

    @Test
    public void returnsInteger() {
        final YamlMapping mapping = this.mapping();
        MatcherAssert.assertThat(
            mapping.integer("integer"),
            Matchers.is(123)
        );
        MatcherAssert.assertThat(
            mapping.integer("missing_integer"),
            Matchers.is(-1)
        );
    }
    
    @Test
    public void returnsFloat() {
        final YamlMapping mapping = this.mapping();
        MatcherAssert.assertThat(
            mapping.floatNumber("float"),
            Matchers.is(3.54F)
        );
        MatcherAssert.assertThat(
            mapping.floatNumber("missing_float"),
            Matchers.is(-1F)
        );
    }
    
    @Test
    public void returnsDouble() {
        final YamlMapping mapping = this.mapping();
        MatcherAssert.assertThat(
            mapping.doubleNumber("double"),
            Matchers.is(2.05)
        );
        MatcherAssert.assertThat(
            mapping.integer("missing_double"),
            Matchers.is(-1)
        );
    }
    
    @Test
    public void returnsLong() {
        final YamlMapping mapping = this.mapping();
        MatcherAssert.assertThat(
            mapping.longNumber("long"),
            Matchers.is(32165498L)
        );
        MatcherAssert.assertThat(
            mapping.longNumber("missing_long"),
            Matchers.is(-1L)
        );
    }
    
    @Test
    public void returnsDate() {
        final YamlMapping mapping = this.mapping();
        MatcherAssert.assertThat(
            mapping.date("localDate"),
            Matchers.is(LocalDate.parse("2007-12-03"))
        );
        MatcherAssert.assertThat(
            mapping.date("missing_localDate"),
            Matchers.nullValue()
        );
    }
    
    @Test
    public void returnsDateTime() {
        final YamlMapping mapping = this.mapping();
        MatcherAssert.assertThat(
            mapping.dateTime("localDateTime"),
            Matchers.is(LocalDateTime.parse("2007-12-03T10:15:30"))
        );
        MatcherAssert.assertThat(
            mapping.dateTime("missing_localDateTime"),
            Matchers.nullValue()
        );
    }
    
    /**
     * Get a YamlMapping for test.
     * @return YamlMapping.
     */
    private YamlMapping mapping() {
        YamlMapping mapping = Yaml.createYamlMappingBuilder()
            .add("integer", "123")
            .add("float", "3.54")
            .add("double", "2.05")
            .add("long", "32165498")
            .add("localDate", "2007-12-03")
            .add("localDateTime", "2007-12-03T10:15:30")
            .build();
        return mapping;
    }
}
