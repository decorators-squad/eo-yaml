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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link SameIndentationLevel}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.0.2
 */
public final class SameIndentationLevelTestCase {

    /**
     * SameIndentationLevel should return all the lines
     * that it encapsulates.
     */
    @Test
    public void fetchesTheLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlLines yaml = new SameIndentationLevel(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            yaml.original().size(),
            Matchers.equalTo(lines.size())
        );
    }
    
    /**
     * SameIndentationLevel should iterate only over the lines,
     * which have the same level of indentation.
     */
    @Test
    public void iteratesSameLevelOfIndentation() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlLines yaml = new SameIndentationLevel(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(yaml, Matchers.iterableWithSize(3));
        final Iterator<YamlLine> iterator = yaml.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(lines.get(0))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(lines.get(1))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(lines.get(4))
        );
    }
    
    /**
     * SameIndentationLevel should iterate only over the lines,
     * which have the same level of indentation. In this case,
     * all the lines are already at the same indentation level.
     */
    @Test
    public void iteratesSameLevelOfIndentationAll() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: test", 1));
        lines.add(new RtYamlLine("third: some", 2));
        lines.add(new RtYamlLine("fourth: values", 3));
        final YamlLines yaml = new SameIndentationLevel(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(yaml, Matchers.iterableWithSize(lines.size()));
        final Iterator<YamlLine> iterator = yaml.iterator();
        for(final YamlLine inputLine : lines) {
            MatcherAssert.assertThat(
                iterator.next(),
                Matchers.equalTo(inputLine)
            );
        }
    }

    /**
     * SameIndentationLevel should iterate only over the lines,
     * which have the same level of indentation. In this case, we
     * actually have no lines.
     */
    @Test
    public void iteratesSameLevelOfIndentationEmpty() {
        final List<YamlLine> lines = new ArrayList<>();
        final YamlLines yaml = new SameIndentationLevel(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(yaml, Matchers.iterableWithSize(lines.size()));
    }
    
}
