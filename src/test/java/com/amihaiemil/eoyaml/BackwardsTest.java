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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Unit tests for {@link Backwards}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
public final class BackwardsTest {

    /**
     * Backwards delegates the call to originals().
     */
    @Test
    public void delegatesOriginals() {
        final YamlLines initial = Mockito.mock(YamlLines.class);
        final Collection<YamlLine> lines = Mockito.mock(Collection.class);
        Mockito.when(initial.original()).thenReturn(lines);
        MatcherAssert.assertThat(
            new Backwards(initial).original(),
            Matchers.is(lines)
        );
    }

    /**
     * Backwards delegates the call to toYamlNode().
     */
    @Test
    public void delegatesToYamlNode() {
        final YamlLines initial = Mockito.mock(YamlLines.class);
        final YamlLine prev = Mockito.mock(YamlLine.class);
        final YamlNode node = Mockito.mock(YamlNode.class);
        Mockito.when(initial.toYamlNode(prev, false)).thenReturn(node);
        MatcherAssert.assertThat(
            new Backwards(initial).toYamlNode(prev, false),
            Matchers.is(node)
        );
    }

    /**
     * Backwards can iterate over some empty lines.
     */
    @Test
    public void iteratesEmptyLines() {
        MatcherAssert.assertThat(
            new Backwards(
                new AllYamlLines(new ArrayList<>())
            ),
            Matchers.emptyIterable()
        );
    }

    /**
     * Backwards can iterate over one single line.
     */
    @Test
    public void iteratesOneLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("one line", 0));
        MatcherAssert.assertThat(
            new Backwards(
                new AllYamlLines(lines)
            ),
            Matchers.iterableWithSize(1)
        );
    }

    /**
     * Backwards can iterate over some lines.
     */
    @Test
    public void iteratesLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first line", 0));
        lines.add(new RtYamlLine("second line", 1));
        lines.add(new RtYamlLine("third line", 2));
        final YamlLines back = new Backwards(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            back,
            Matchers.iterableWithSize(3)
        );
        final Iterator<YamlLine> backIt = back.iterator();
        MatcherAssert.assertThat(
            backIt.next().trimmed(),
            Matchers.equalTo("third line")
        );
        MatcherAssert.assertThat(
            backIt.next().trimmed(),
            Matchers.equalTo("second line")
        );
        MatcherAssert.assertThat(
            backIt.next().trimmed(),
            Matchers.equalTo("first line")
        );
    }
}
