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

import com.amihaiemil.eoyaml.exceptions.YamlIndentationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import org.junit.Assert;

/**
 * Unit tests for {@link WellIndented}.
 * @author Mihai Andronache
 * @version $Id$
 * @since 3.1.2
 */
public final class WellIndentedTest {

    /**
     * WellIndented can return the encapsulated lines.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void returnsLines() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final Collection<YamlLine> collection = Mockito.mock(Collection.class);
        Mockito.when(lines.original()).thenReturn(collection);
        MatcherAssert.assertThat(
            new WellIndented(lines).original(),
            Matchers.is(collection)
        );
    }
    
    /**
     * WellIndented can turn itself into YamlNode.
     */
    @Test
    public void turnsIntoYamlNode() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final YamlLine prev = Mockito.mock(YamlLine.class);
        final YamlNode result = Mockito.mock(YamlNode.class);
        Mockito.when(lines.toYamlNode(prev, false)).thenReturn(result);
        MatcherAssert.assertThat(
            new WellIndented(lines).toYamlNode(prev, false),
            Matchers.is(result)
        );
    }
    
    /**
     * WellIndented validates correctly-indented lines.
     */
    @Test
    public void validatesProperLines() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("third:", 4));
        lines.add(new RtYamlLine("  - seq", 5));
        final YamlLines wiLines = new WellIndented(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            wiLines,
            Matchers.iterableWithSize(6)
        );
    }
    
    /**
     * WellIndented complains on badly indented nested mapping.
     */
    @Test(expected = YamlIndentationException.class)
    public void complainsOnBadlyIndentedNestedMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine(" fourth: some", 2));
        lines.add(new RtYamlLine("  notok: values", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlLines wiLines = new WellIndented(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            wiLines,
            Matchers.iterableWithSize(5)
        );
        Assert.fail("YamlIndentationException was expected!");
    }
    
    /**
     * WellIndented complains on badly indented nested sequence.
     */
    @Test(expected = YamlIndentationException.class)
    public void complainsOnBadlyIndentedNestedSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  - ok", 2));
        lines.add(new RtYamlLine("   - notok", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlLines wiLines = new WellIndented(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            wiLines,
            Matchers.iterableWithSize(5)
        );
        Assert.fail("YamlIndentationException was expected!");
    }
    
    /**
     * WellIndented complains on badly indented sequence.
     */
    @Test(expected = YamlIndentationException.class)
    public void complainsOnBadlyIndentedSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- first", 0));
        lines.add(new RtYamlLine("- second", 1));
        lines.add(new RtYamlLine(" - third_not_ok", 2));
        final YamlLines wiLines = new WellIndented(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            wiLines,
            Matchers.iterableWithSize(3)
        );
        Assert.fail("YamlIndentationException was expected!");
    }
    
    /**
     * WellIndented complains on badly indented mapping.
     */
    @Test(expected = YamlIndentationException.class)
    public void complainsOnBadlyIndentedMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("  second_not_ok: value ", 1));
        lines.add(new RtYamlLine("third: something", 2));
        final YamlLines wiLines = new WellIndented(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            wiLines,
            Matchers.iterableWithSize(3)
        );
        Assert.fail("YamlIndentationException was expected!");
    }
}
