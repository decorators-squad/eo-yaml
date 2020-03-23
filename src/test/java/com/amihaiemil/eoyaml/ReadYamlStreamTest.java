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

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link ReadYamlStream}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.4
 */
public final class ReadYamlStreamTest {

    /**
     * If no lines are provided, then ReadYamlStream should be empty.
     */
    @Test
    public void worksWithNoLines() {
        final YamlStream stream = new ReadYamlStream(
            new AllYamlLines(new ArrayList<>())
        );
        MatcherAssert.assertThat(stream.values(), Matchers.emptyIterable());
    }

    /**
     * If an empty Yaml stream is provided,
     * then ReadYamlStream should be empty.
     */
    @Test
    public void worksWithEmptyStream() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("...", 1));
        final YamlStream stream = new ReadYamlStream(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(stream.values(), Matchers.emptyIterable());
    }

    /**
     * ReadYamlStream can represent a stream of YAML docs which
     * are separated only by the Start Marker.
     */
    @Test
    public void worksWithOnlyStartMarkers() {
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
        final YamlStream stream = new ReadYamlStream(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(stream.values(), Matchers.iterableWithSize(3));
    }

    /**
     * ReadYamlStream can represent a stream of YAML docs which
     * are separated only by the Start Marker and have the same
     * indentation level as it.
     */
    @Test
    public void worksWithOnlyStartMarkersSameIndentation() {
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
        final YamlStream stream = new ReadYamlStream(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(stream.values(), Matchers.iterableWithSize(3));
    }

    /**
     * ReadYamlStream can represent a stream of YAML docs which
     * are separated by the Start, as well as End Markers.
     */
    @Test
    public void worksWithStartAndEndMarkers() {
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
        final YamlStream stream = new ReadYamlStream(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(stream.values(), Matchers.iterableWithSize(3));
    }

    /**
     * ReadYamlStream can represent a stream of YAML docs which
     * are separated by the Start, as well as End Markers, and have
     * the same indentation level.
     */
    @Test
    public void worksWithStartAndEndMarkersSameIndentation() {
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
        final YamlStream stream = new ReadYamlStream(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(stream.values(), Matchers.iterableWithSize(3));
    }
}
