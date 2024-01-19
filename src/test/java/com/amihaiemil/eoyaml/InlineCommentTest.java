/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
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
 * Unit tests for {@link InlineComment}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 5.2.1
 */
public final class InlineCommentTest {

    /**
     * InlineComment can return the parent node from the original Comment.
     */
    @Test
    public void returnsNode() {
        final YamlNode parent = Mockito.mock(YamlNode.class);
        final Comment original = Mockito.mock(Comment.class);
        Mockito.when(original.yamlNode()).thenReturn(parent);

        MatcherAssert.assertThat(
            new InlineComment(original).yamlNode(),
            Matchers.is(parent)
        );
        Mockito.verify(original, Mockito.times(1)).yamlNode();
    }

    /**
     * InlineComment can return the original value if it has no new lines.
     */
    @Test
    public void returnsOriginalValueWithNoNewlines() {
        final Comment original = Mockito.mock(Comment.class);
        Mockito.when(original.value()).thenReturn("one line of comment");

        MatcherAssert.assertThat(
            new InlineComment(original).value(),
            Matchers.equalTo("one line of comment")
        );
        Mockito.verify(original, Mockito.times(1)).value();
    }

    /**
     * InlineComment concatenates the lines of the original Comment.
     */
    @Test
    public void returnsConcatenatedLines() {
        final Comment original = Mockito.mock(Comment.class);
        Mockito.when(original.value())
            .thenReturn(
                "first line" + System.lineSeparator()
                + "second line" + System.lineSeparator()
                + "third line"
            );

        MatcherAssert.assertThat(
            new InlineComment(original).value(),
            Matchers.equalTo(
                "first line second line third line"
            )
        );
        Mockito.verify(original, Mockito.times(1)).value();
    }

}
