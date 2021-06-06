/**
 * Copyright (c) 2016-2021, Mihai Emil Andronache
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
 * Unit tests for {@link ReadScalarComment}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 5.2.1
 */
public final class ReadScalarCommentTest {

    /**
     * ReadScalarComment can return the Comment above.
     */
    @Test
    public void returnsCommentAbove() {
        final Comment above = Mockito.mock(Comment.class);
        final Comment inline = Mockito.mock(Comment.class);
        MatcherAssert.assertThat(
            new ReadScalarComment(above, inline).above(),
            Matchers.is(above)
        );
    }

    /**
     * ReadScalarComment can return the inline Comment.
     */
    @Test
    public void returnsInlineComment() {
        final Comment above = Mockito.mock(Comment.class);
        final Comment inline = Mockito.mock(Comment.class);
        MatcherAssert.assertThat(
            new ReadScalarComment(above, inline).inline(),
            Matchers.is(inline)
        );
    }

    /**
     * ReadScalarComment can return the parent YamlNode.
     */
    @Test
    public void returnsNode() {
        final YamlNode parent = Mockito.mock(YamlNode.class);
        final Comment above = Mockito.mock(Comment.class);
        final Comment inline = Mockito.mock(Comment.class);
        Mockito.when(inline.yamlNode()).thenReturn(parent);

        MatcherAssert.assertThat(
            new ReadScalarComment(above, inline).yamlNode(),
            Matchers.is(parent)
        );
    }

    /**
     * ReadScalarComment returns the concatenated (with newline) value of
     * both comments.
     */
    @Test
    public void returnsConcatenatedValue(){
        final Comment above = Mockito.mock(Comment.class);
        Mockito.when(above.value()).thenReturn("Comment above on\nmore lines.");
        final Comment inline = Mockito.mock(Comment.class);
        Mockito.when(inline.value()).thenReturn("And inline comment.");

        MatcherAssert.assertThat(
            new ReadScalarComment(above, inline).value(),
            Matchers.equalTo(
                "Comment above on\n"
                + "more lines.\n"
                + "And inline comment."
            )
        );
    }

    /**
     * ReadScalarComment.value() is only the inline comment if the above
     * Comment is empty.
     */
    @Test
    public void returnsValueOfInlineCommentOnly() {
        final Comment above = Mockito.mock(Comment.class);
        Mockito.when(above.value()).thenReturn("");
        final Comment inline = Mockito.mock(Comment.class);
        Mockito.when(inline.value()).thenReturn("Inline comment.");

        MatcherAssert.assertThat(
            new ReadScalarComment(above, inline).value(),
            Matchers.equalTo("Inline comment.")
        );
    }

    /**
     * ReadScalarComment.value() is only the above comment if the inline
     * Comment is empty.
     */
    @Test
    public void returnsValueOfAboveCommentOnly() {
        final Comment above = Mockito.mock(Comment.class);
        Mockito.when(above.value()).thenReturn("Comment above on\nmore lines.");
        final Comment inline = Mockito.mock(Comment.class);
        Mockito.when(inline.value()).thenReturn("");

        MatcherAssert.assertThat(
            new ReadScalarComment(above, inline).value(),
            Matchers.equalTo("Comment above on\nmore lines.")
        );
    }

    /**
     * ReadScalarComment.value() returns empty string if both comments are
     * empty.
     */
    @Test
    public void returnsEmptyComment() {
        final Comment above = Mockito.mock(Comment.class);
        Mockito.when(above.value()).thenReturn("");
        final Comment inline = Mockito.mock(Comment.class);
        Mockito.when(inline.value()).thenReturn("");

        MatcherAssert.assertThat(
            new ReadScalarComment(above, inline).value(),
            Matchers.isEmptyString()
        );
    }
}
