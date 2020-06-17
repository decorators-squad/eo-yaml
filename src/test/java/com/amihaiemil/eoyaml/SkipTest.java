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
 * Unit tests for {@link Skip}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
public final class SkipTest {

    /**
     * No line is skipped when there are no conditions.
     */
    @Test
    public void noConditionsSkipsNone() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlLines all = new AllYamlLines(lines);
        MatcherAssert.assertThat(all, Matchers.iterableWithSize(lines.size()));
        MatcherAssert.assertThat(
            new Skip(all), Matchers.iterableWithSize(lines.size())
        );
    }

    /**
     * It works when there are no lines given.
     */
    @Test
    public void worksWithNoLines() {
        final List<YamlLine> lines = new ArrayList<>();
        final YamlLines all = new AllYamlLines(lines);
        MatcherAssert.assertThat(all, Matchers.iterableWithSize(lines.size()));
        MatcherAssert.assertThat(
            new Skip(all), Matchers.iterableWithSize(lines.size())
        );
        MatcherAssert.assertThat(
            new Skip(
                all,
                line -> {
                    throw new IllegalStateException(
                        "Condition should not be checked, there are no lines."
                    );
                }
            ),
            Matchers.iterableWithSize(lines.size())
        );
    }
    /**
     * Lines which are satisfying one of the conditions are skipped.
     */
    @Test
    public void skipsLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("---", 0));
        lines.add(new RtYamlLine("temp: 10C", 1));
        lines.add(new RtYamlLine("...", 2));
        lines.add(new RtYamlLine("---", 3));
        lines.add(new RtYamlLine("temp: 15C", 4));
        lines.add(new RtYamlLine("...", 5));
        final YamlLines all = new AllYamlLines(lines);
        MatcherAssert.assertThat(all, Matchers.iterableWithSize(lines.size()));
        final YamlLines skip = new Skip(
            all,
            line -> line.trimmed().startsWith("---"),
            line -> line.trimmed().startsWith("...")
        );
        MatcherAssert.assertThat(skip, Matchers.iterableWithSize(2));
        final Iterator<YamlLine> notSkipped = skip.iterator();
        MatcherAssert.assertThat(
            notSkipped.next().trimmed(),
            Matchers.equalTo("temp: 10C")
        );
        MatcherAssert.assertThat(
            notSkipped.next().trimmed(),
            Matchers.equalTo("temp: 15C")
        );
    }

    /**
     * No line is skipped because none of them are satisfying any
     * conditions.
     */
    @Test
    public void skipsNoLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("temp: 10C", 1));
        lines.add(new RtYamlLine("nextTemp: 15C", 4));
        final YamlLines all = new AllYamlLines(lines);
        MatcherAssert.assertThat(all, Matchers.iterableWithSize(lines.size()));
        final YamlLines skip = new Skip(
            all,
            line -> line.trimmed().startsWith("---"),
            line -> line.trimmed().startsWith("...")
        );
        MatcherAssert.assertThat(skip, Matchers.iterableWithSize(lines.size()));
        final Iterator<YamlLine> notSkipped = skip.iterator();
        MatcherAssert.assertThat(
            notSkipped.next().trimmed(),
            Matchers.equalTo("temp: 10C")
        );
        MatcherAssert.assertThat(
            notSkipped.next().trimmed(),
            Matchers.equalTo("nextTemp: 15C")
        );
    }

    /**
     * Skip delegates the call to originals().
     */
    @Test
    public void delegatesOriginals() {
        final YamlLines initial = Mockito.mock(YamlLines.class);
        final Collection<YamlLine> lines = Mockito.mock(Collection.class);
        Mockito.when(initial.original()).thenReturn(lines);
        MatcherAssert.assertThat(
            new Skip(initial).original(),
            Matchers.is(lines)
        );
    }

    /**
     * Skip delegates the call to toYamlNode().
     */
    @Test
    public void delegatesToYamlNode() {
        final YamlLines initial = Mockito.mock(YamlLines.class);
        final YamlLine prev = Mockito.mock(YamlLine.class);
        final YamlNode node = Mockito.mock(YamlNode.class);
        Mockito.when(initial.toYamlNode(prev, false)).thenReturn(node);
        MatcherAssert.assertThat(
            new Skip(initial).toYamlNode(prev, false),
            Matchers.is(node)
        );
    }
}
