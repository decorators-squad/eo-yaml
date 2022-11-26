/**
 * Copyright (c) 2016-2023, Mihai Emil Andronache
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
 * Unit tests for {@link Edited}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 7.0.0
 */
public final class EditedTestCase {

    /**
     * Edited returns the newContent as value().
     */
    @Test
    public void returnsNewContentAsValue() {
        final YamlLine original = Mockito.mock(YamlLine.class);
        MatcherAssert.assertThat(
            new Edited("my new content", original).value(),
            Matchers.equalTo("my new content")
        );
        Mockito.verify(original, Mockito.times(0)).value();
    }

    /**
     * Edited returns the comment from the newContent.
     */
    @Test
    public void returnsNewContentAsComment() {
        final YamlLine original = Mockito.mock(YamlLine.class);
        MatcherAssert.assertThat(
            new Edited("my new content # comment here", original)
                .comment(),
            Matchers.equalTo("comment here")
        );
        Mockito.verify(original, Mockito.times(0)).comment();
    }

    /**
     * Edited returns the original YamlLine's number.
     */
    @Test
    public void returnsOriginalLineNumber() {
        final YamlLine original = Mockito.mock(YamlLine.class);
        Mockito.when(original.number()).thenReturn(35);
        MatcherAssert.assertThat(
            new Edited("my new content", original).number(),
            Matchers.equalTo(35)
        );
        Mockito.verify(original, Mockito.times(1)).number();
    }

    /**
     * Edited returns the original YamlLine's indentation.
     */
    @Test
    public void returnsOriginalIndentation() {
        final YamlLine original = Mockito.mock(YamlLine.class);
        Mockito.when(original.indentation()).thenReturn(16);
        MatcherAssert.assertThat(
            new Edited("my new content", original).indentation(),
            Matchers.equalTo(16)
        );
        Mockito.verify(original, Mockito.times(1)).indentation();
    }

}
