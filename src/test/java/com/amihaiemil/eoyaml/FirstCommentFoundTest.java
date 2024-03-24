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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Unit tests for {@link FirstCommentFound}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
public final class FirstCommentFoundTest {
    /**
     * FirstCommentFound delegates the call to originals().
     */
    @Test
    public void delegatesOriginals() {
        final YamlLines initial = Mockito.mock(YamlLines.class);
        final Collection<YamlLine> lines = Mockito.mock(Collection.class);
        Mockito.when(initial.original()).thenReturn(lines);
        MatcherAssert.assertThat(
            new FirstCommentFound(initial).original(),
            Matchers.is(lines)
        );
    }

    /**
     * FirstCommentFound delegates the call to toYamlNode().
     */
    @Test
    public void delegatesToYamlNode() {
        final YamlLines initial = Mockito.mock(YamlLines.class);
        final YamlLine prev = Mockito.mock(YamlLine.class);
        final YamlNode node = Mockito.mock(YamlNode.class);
        Mockito.when(initial.nextYamlNode(prev)).thenReturn(node);
        MatcherAssert.assertThat(
            new FirstCommentFound(initial).nextYamlNode(prev),
            Matchers.is(node)
        );
    }

    /**
     * {@link FirstCommentFound} can work with empty lines.
     */
    @Test
    public void worksWithEmptyLines() {
        MatcherAssert.assertThat(
            new FirstCommentFound(
                new AllYamlLines(new ArrayList<>())
            ),
            Matchers.emptyIterable()
        );
    }

    /**
     * {@link FirstCommentFound} returns the lines of the first
     * comment in the document.
     */
    @Test
    public void findsFirstComment() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("# YAML document for", 0));
        lines.add(new RtYamlLine("# test purposes: ", 1));
        lines.add(new RtYamlLine("first: somethingElse", 2));
        lines.add(new RtYamlLine("second: ", 3));
        lines.add(new RtYamlLine("  fourth: some", 4));
        lines.add(new RtYamlLine("  fifth: values", 5));
        lines.add(new RtYamlLine("third: something", 6));
        final YamlLines comment = new FirstCommentFound(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(comment, Matchers.iterableWithSize(2));
        final Iterator<YamlLine> commIt = comment.iterator();
        MatcherAssert.assertThat(
            commIt.next().comment(),
            Matchers.equalTo("YAML document for")
        );
        MatcherAssert.assertThat(
            commIt.next().comment(),
            Matchers.equalTo("test purposes:")
        );
    }

    /**
     * {@link FirstCommentFound} returns no lines since there is
     * no comment at the beginning.
     */
    @Test
    public void noFirstComment() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("# second map", 1));
        lines.add(new RtYamlLine("second: ", 2));
        lines.add(new RtYamlLine("  fourth: some", 3));
        lines.add(new RtYamlLine("  fifth: values", 4));
        lines.add(new RtYamlLine("third: something", 5));
        final YamlLines comment = new FirstCommentFound(
                new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(comment, Matchers.emptyIterable());
    }

    /**
     * {@link FirstCommentFound} returns the comment when called multiple
     *  times - check we re-parse correctly.
     */
    @Test
    public void findCommentsMultipleTimes() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("# two", 0));
        lines.add(new RtYamlLine("three: three", 1));
        final YamlLines comment = new FirstCommentFound(
                new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(comment, Matchers.iterableWithSize(1));
        MatcherAssert.assertThat(comment, Matchers.iterableWithSize(1));
    }
}
