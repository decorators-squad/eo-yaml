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
 * Unit tests for {@link StartMarkers}.
 * @author Mihai Andronache(amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.4
 */
public final class StartMarkersTest {

    /**
     * StartMarkers can return the encapsulated lines.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void returnsLines() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final Collection<YamlLine> collection = Mockito.mock(Collection.class);
        Mockito.when(lines.original()).thenReturn(collection);
        MatcherAssert.assertThat(
            new StartMarkers(lines).original(),
            Matchers.is(collection)
        );
    }

    /**
     * StartMarkers can turn itself into YamlNode.
     */
    @Test
    public void turnsIntoYamlNode() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final YamlLine prev = Mockito.mock(YamlLine.class);
        final YamlNode result = Mockito.mock(YamlNode.class);
        Mockito.when(lines.toYamlNode(prev, false)).thenReturn(result);
        MatcherAssert.assertThat(
            new StartMarkers(lines).toYamlNode(prev, false),
            Matchers.is(result)
        );
    }

    /**
     * StartMarkers works when there are actually no lines.
     */
    @Test
    public void iteratesOverNoLines() {
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(new ArrayList<>())
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(0)
        );
    }

    /**
     * StartMarkers can iterate when there is only one document, with no
     * End Marker.
     */
    @Test
    public void iteratesOnlyOneDocument() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("  time: 12:00", 1));
        lines.add(new RtYamlLine("  temperature: 25C", 2));
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(1)
        );
        final Iterator<YamlLine> iterator = smLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
    }

    /**
     * StartMarkers can iterate when there is only one document, with an
     * End Marker.
     */
    @Test
    public void iteratesOnlyOneDocumentWithEndMarker() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("  time: 12:00", 1));
        lines.add(new RtYamlLine("  temperature: 25C", 2));
        lines.add(new RtYamlLine("...", 2));
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(1)
        );
        final Iterator<YamlLine> iterator = smLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
    }

    /**
     * The iterator returned by StartMarkers only covers the lines
     * representing a start marker (---).
     */
    @Test
    public void iteratesOnlyOverStartMarkers() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("  time: 12:00", 1));
        lines.add(new RtYamlLine("  temperature: 25C", 2));
        lines.add(new RtYamlLine("...", 3));
        lines.add(new RtYamlLine("---", 4));
        lines.add(new RtYamlLine("  time: 16:00", 5));
        lines.add(new RtYamlLine("  temperature: 20C", 6));
        lines.add(new RtYamlLine("...", 7));
        lines.add(new RtYamlLine("---", 8));
        lines.add(new RtYamlLine("  time: 22:00", 9));
        lines.add(new RtYamlLine("  temperature: 15C", 10));
        lines.add(new RtYamlLine("...", 11));
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(3)
        );
        final Iterator<YamlLine> iterator = smLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
    }

    /**
     * The iterator returned by StartMarkers only covers the lines
     * representing a start marker (---), with the exception of the first one,
     * which might not be a start marker (in a stream, the start marker of the
     * first document can be missing).
     */
    @Test
    public void iteratesOnlyOverStartMarkersMissingFirst() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("  time: 12:00", 0));
        lines.add(new RtYamlLine("  temperature: 25C", 1));
        lines.add(new RtYamlLine("...", 2));
        lines.add(new RtYamlLine("---", 3));
        lines.add(new RtYamlLine("  time: 16:00", 4));
        lines.add(new RtYamlLine("  temperature: 20C", 5));
        lines.add(new RtYamlLine("...", 6));
        lines.add(new RtYamlLine("---", 7));
        lines.add(new RtYamlLine("  time: 22:00", 8));
        lines.add(new RtYamlLine("  temperature: 15C", 9));
        lines.add(new RtYamlLine("...", 10));
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(3)
        );
        final Iterator<YamlLine> iterator = smLines.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.instanceOf(YamlLine.NullYamlLine.class)
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
    }

    /**
     * The iterator returned by StartMarkers only covers the lines
     * representing a start marker (---). In this test case,
     * there are no document End Markers (...);
     */
    @Test
    public void iteratesOnlyOverStartMarkersNoElipses() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("  time: 12:00", 1));
        lines.add(new RtYamlLine("  temperature: 25C", 2));
        lines.add(new RtYamlLine("---", 3));
        lines.add(new RtYamlLine("  time: 16:00", 4));
        lines.add(new RtYamlLine("  temperature: 20C", 5));
        lines.add(new RtYamlLine("---", 6));
        lines.add(new RtYamlLine("  time: 22:00", 7));
        lines.add(new RtYamlLine("  temperature: 15C", 8));
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(3)
        );
        final Iterator<YamlLine> iterator = smLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
    }

    /**
     * The iterator returned by StartMarkers only covers the lines
     * representing a start marker (---). Also, in this test case,
     * the YAML documents have the same indentation level as the
     * markers.
     */
    @Test
    public void iteratesOnlyOverStartMarkersSameIndentation() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("time: 12:00", 1));
        lines.add(new RtYamlLine("temperature: 25C", 2));
        lines.add(new RtYamlLine("...", 3));
        lines.add(new RtYamlLine("---", 4));
        lines.add(new RtYamlLine("time: 16:00", 5));
        lines.add(new RtYamlLine("temperature: 20C", 6));
        lines.add(new RtYamlLine("...", 7));
        lines.add(new RtYamlLine("---", 8));
        lines.add(new RtYamlLine("time: 22:00", 9));
        lines.add(new RtYamlLine("temperature: 15C", 10));
        lines.add(new RtYamlLine("...", 11));
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(3)
        );
        final Iterator<YamlLine> iterator = smLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
    }

    /**
     * The iterator returned by StartMarkers only covers the lines
     * representing a start marker (---). In this test case,
     * there are no document End Markers (...). Also, the YAML Documents
     * have the same indentation level as the markers.
     */
    @Test
    public void iteratesOnlyOverStartMarkersNoElipsesSameIndentation() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("time: 12:00", 1));
        lines.add(new RtYamlLine("temperature: 25C", 2));
        lines.add(new RtYamlLine("---", 3));
        lines.add(new RtYamlLine("time: 16:00", 4));
        lines.add(new RtYamlLine("temperature: 20C", 5));
        lines.add(new RtYamlLine("---", 6));
        lines.add(new RtYamlLine("time: 22:00", 7));
        lines.add(new RtYamlLine("temperature: 15C", 8));
        final YamlLines smLines = new StartMarkers(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            smLines,
            Matchers.iterableWithSize(3)
        );
        final Iterator<YamlLine> iterator = smLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
    }
}
