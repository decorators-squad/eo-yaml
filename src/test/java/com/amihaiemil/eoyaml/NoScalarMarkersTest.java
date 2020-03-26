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
 * Unit tests for {@link NoScalarMarkers}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
public final class NoScalarMarkersTest {
    /**
     * {@link NoScalarMarkers} can return the encapsulated lines.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void returnsLines() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final Collection<YamlLine> collection = Mockito.mock(Collection.class);
        Mockito.when(lines.lines()).thenReturn(collection);
        MatcherAssert.assertThat(
            new NoScalarMarkers(lines).lines(),
            Matchers.is(collection)
        );
    }

    /**
     * {@link NoScalarMarkers} can turn itself into YamlNode.
     */
    @Test
    public void turnsIntoYamlNode() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final YamlLine prev = Mockito.mock(YamlLine.class);
        final YamlNode result = Mockito.mock(YamlNode.class);
        Mockito.when(lines.toYamlNode(prev)).thenReturn(result);
        MatcherAssert.assertThat(
            new NoScalarMarkers(lines).toYamlNode(prev),
            Matchers.is(result)
        );
    }

    /**
     * {@link NoScalarMarkers} should ignore the '|' marker.
     */
    @Test
    public void ignoresLiteralScalarMarker() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("|", 0));
        lines.add(new RtYamlLine("first", 1));
        lines.add(new RtYamlLine("second", 2));

        final YamlLines ndmLines = new NoScalarMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            ndmLines,
            Matchers.iterableWithSize(2)
        );
        final Iterator<YamlLine> iterator = ndmLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("first")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("second")
        );
    }


    /**
     * {@link NoScalarMarkers} should ignore the '>' marker.
     */
    @Test
    public void ignoresFoldedScalarMarker() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine(">", 0));
        lines.add(new RtYamlLine("first", 1));
        lines.add(new RtYamlLine("second", 2));

        final YamlLines ndmLines = new NoScalarMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            ndmLines,
            Matchers.iterableWithSize(2)
        );
        final Iterator<YamlLine> iterator = ndmLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("first")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("second")
        );
    }

    /**
     * {@link NoScalarMarkers} iterates over all the lines, since
     * there are actually no directives to ignore.
     */
    @Test
    public void noDirectivesAndMarkersToIgnore() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first", 0));
        lines.add(new RtYamlLine("second", 1));
        lines.add(new RtYamlLine("third", 2));
        final YamlLines ndmLines = new NoScalarMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            ndmLines,
            Matchers.iterableWithSize(3)
        );
        final Iterator<YamlLine> iterator = ndmLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("first")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("second")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("third")
        );
    }

    /**
     * {@link NoScalarMarkers} works with no lines.
     */
    @Test
    public void iteratesEmptyLines() {
        final YamlLines ndmLines = new NoScalarMarkers(
            new AllYamlLines(new ArrayList<>())
        );
        MatcherAssert.assertThat(
            ndmLines,
            Matchers.iterableWithSize(0)
        );
    }
}
