/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
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
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link ReadYamlSequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class ReadYamlSequenceTest {

    /**
     * ReadYamlSequence can return the YamlMapping from a given index.
     * Note that a YamlSequence is ordered, so the index might differ from
     * the original found at read time.
     */
    @Test
    public void returnsYamlMappingFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new RtYamlLines(lines)
        );
        final YamlMapping alfa = sequence.yamlMapping(2);
        MatcherAssert.assertThat(alfa, Matchers.notNullValue());
        MatcherAssert.assertThat(alfa, Matchers.instanceOf(YamlMapping.class));
        MatcherAssert.assertThat(
            alfa.yamlMapping("alfa").string("key"), Matchers.equalTo("value")
        );
    }
    
    /**
     * ReadYamlSequence can return the YamlSequence from a given index.
     * Note that a YamlSequence is ordered, so the index might differ from
     * the original found at read time.
     */
    @Test
    public void returnsYamlSequenceFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  - rultor", 1));
        lines.add(new RtYamlLine("  - 0pdd", 2));
        lines.add(new RtYamlLine("- scalar", 3));
        lines.add(new RtYamlLine("- otherScalar", 4));
        final YamlSequence sequence = new ReadYamlSequence(
            new RtYamlLines(lines)
        );
        final YamlSequence devops = sequence.yamlSequence(0);
        MatcherAssert.assertThat(devops, Matchers.notNullValue());
        MatcherAssert.assertThat(
            devops, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(devops.size(), Matchers.equalTo(2));
    }
    
    /**
     * ReadYamlSequence can return the YamlMapping from a given index.
     * Note that a YamlSequence is ordered, so the index might differ from
     * the original found at read time.
     */
    @Test
    public void returnsStringFromIndex(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- rultor", 0));
        lines.add(new RtYamlLine("- 0pdd", 1));
        final YamlSequence devops = new ReadYamlSequence(
            new RtYamlLines(lines)
        );
        MatcherAssert.assertThat(devops.string(0), Matchers.equalTo("rultor"));
        MatcherAssert.assertThat(devops.string(1), Matchers.equalTo("0pdd"));
    }
    
    /**
     * ReadYamlSequence can return its size.
     */
    @Test
    public void returnsSize(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- ", 0));
        lines.add(new RtYamlLine("  beta: somethingElse", 1));
        lines.add(new RtYamlLine("- scalar", 2));
        lines.add(new RtYamlLine("- ", 3));
        lines.add(new RtYamlLine("  alfa: ", 4));
        lines.add(new RtYamlLine("    fourth: some", 5));
        lines.add(new RtYamlLine("    key: value", 6));
        final YamlSequence sequence = new ReadYamlSequence(
            new RtYamlLines(lines)
        );
        MatcherAssert.assertThat(sequence.size(), Matchers.is(3));
    }
    
    /**
     * An empty ReadYamlSequence can be printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void printsEmptyYaml() throws Exception {
        final YamlSequence sequence = new ReadYamlSequence(
            new RtYamlLines(new ArrayList<YamlLine>())
        );
        MatcherAssert.assertThat(sequence.toString(), Matchers.isEmptyString());
    }
}
