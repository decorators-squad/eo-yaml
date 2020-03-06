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
import org.mockito.Mockito;

/**
 * Unit Tests for {@link CachedYamlLine}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class CachedYamlLineTest {

    /**
     * CachedIndentedLine knows its number.
     */
    @Test
    public void knowsNumber() {
        YamlLine line = new CachedYamlLine(
            new RtYamlLine("this line", 12)
        );
        MatcherAssert.assertThat(line.number(), Matchers.is(12));
    }

    /**
     * CachedIndentedLine caches trimmed String and returns same object.
     */
    @Test
    public void cachesTrimmedValue() {
        YamlLine mock = Mockito.mock(YamlLine.class);
        Mockito.when(mock.trimmed())
            .thenReturn("this line")
            .thenThrow(new RuntimeException());
        YamlLine line = new CachedYamlLine(mock);
        MatcherAssert.assertThat(line.trimmed(), Matchers.is("this line"));
        MatcherAssert.assertThat(line.trimmed(), Matchers.is("this line"));
    }

    /**
     * CachedIndentedLine caches indentation value and doesn't recalculate it.
     */
    @Test
    public void cachesIndentationValue() {
        YamlLine mock = Mockito.mock(YamlLine.class);
        Mockito.when(mock.indentation())
            .thenReturn(12)
            .thenThrow(new RuntimeException());
        YamlLine cachedLine = new CachedYamlLine(mock);
        MatcherAssert.assertThat(cachedLine.indentation(), Matchers.is(12));
        MatcherAssert.assertThat(cachedLine.indentation(), Matchers.is(12));
    }
}
