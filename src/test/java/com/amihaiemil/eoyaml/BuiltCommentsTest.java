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

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link BuiltComments}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
public final class BuiltCommentsTest {

    /**
     * BuiltComments can iterate over the comments.
     */
    @Test
    public void iteratesOverComments() {
        final List<Comment> comments = new ArrayList<>();
        comments.add(Mockito.mock(Comment.class));
        comments.add(Mockito.mock(Comment.class));
        comments.add(Mockito.mock(Comment.class));
        final Comments iterable = new BuiltComments(comments);
        int count = 0;
        for(final Comment com : iterable) {
            count++;
        }
        MatcherAssert.assertThat(count, Matchers.equalTo(3));
    }

    /**
     * BuiltComments can return the Comment referring to a given node.
     */
    @Test
    public void returnsCommentReferringToNode() {
        final List<Comment> all = new ArrayList<>();
        all.add(
            new BuiltComment(
                new PlainStringScalar("node1"),
                "test comment1"
            )
        );
        all.add(
            new BuiltComment(
                new PlainStringScalar("node2"),
                "test comment2"
            )
        );
        all.add(
            new BuiltComment(
                new PlainStringScalar("node3"),
                "test comment3"
            )
        );
        final Comments comments = new BuiltComments(all);
        MatcherAssert.assertThat(
            comments.referringTo("node2").value(),
            Matchers.equalTo("test comment2")
        );
        MatcherAssert.assertThat(
            comments.referringTo("missing").value(),
            Matchers.isEmptyString()
        );
    }

}
