/**
 * Copyright (c) 2016-2023, Mihai Emil Andronache
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

import com.amihaiemil.eoyaml.exceptions.YamlReadingException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link RtYamlLine}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class RtYamlLineTest {
    /**
     * Expect no exceptions thrown in tests.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * RtYamlLine knows its number.
     */
    @Test
    public void knowsNumber() {
        YamlLine line = new RtYamlLine("this line", 12);
        MatcherAssert.assertThat(line.number(), Matchers.is(12));
    }

    /**
     * RtYamlLine knows if the following line(s) require a deeped indentation
     * or not.
     */
    @Test
    public void requiresNestedIndentation() {
        MatcherAssert.assertThat(
            new RtYamlLine("this: |- ", 12).requireNestedIndentation(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            new RtYamlLine("this: value", 12).requireNestedIndentation(),
            Matchers.is(false)
        );
    }

    /**
     * RtYamlLine can its untrimmed, original value.
     */
    @Test
    public void returnsValue() {
        YamlLine line = new RtYamlLine(
            "   this: line # with comment  ", 10
        );
        MatcherAssert.assertThat(
            line.value(), Matchers.equalTo(
                "   this: line # with comment  "
            )
        );
    }

    /**
     * RtYamlLine can trim itself.
     */
    @Test
    public void trimsItself() {
        YamlLine line = new RtYamlLine("   this: line   ", 10);
        MatcherAssert.assertThat(
            line.trimmed(), Matchers.equalTo("this: line")
        );
    }

    /**
     * RtYamlLine can trim off the comments.
     */
    @Test
    public void trimsCommentsOff() {
        YamlLine line = new RtYamlLine("this: line  #has a comment ", 0);
        MatcherAssert.assertThat(
            line.trimmed(), Matchers.equalTo("this: line")
        );
    }

    /**
     * RtYamlLine will not trim the '#' because it is escaped and part
     * of a value.
     */
    @Test
    public void doesNotTrimEscapedHash() {
        YamlLine line = new RtYamlLine("color: '#404040' # comment here", 0);
        MatcherAssert.assertThat(
                line.trimmed(), Matchers.equalTo("color: '#404040'")
        );
    }

    /**
     * RtYamlLine returns the contained comment.
     */
    @Test
    public void returnsComment() {
        YamlLine line = new RtYamlLine("color: '#404040' # comment here", 0);
        MatcherAssert.assertThat(
            line.comment(), Matchers.equalTo("comment here")
        );
    }

    /**
     * RtYamlLine returns the comment which is the whole line.
     */
    @Test
    public void returnsCommentWholeLine() {
        YamlLine line = new RtYamlLine(
            "# line containing only a comment", 0
        );
        MatcherAssert.assertThat(
            line.comment(),
            Matchers.equalTo("line containing only a comment")
        );
    }

    /**
     * RtYamlLine returns an empty comment as the line is only a hash.
     */
    @Test
    public void returnsEmptyLineComment() {
        YamlLine line = new RtYamlLine("#", 0);
        MatcherAssert.assertThat(
            line.comment(),
            Matchers.isEmptyString()
        );
    }

    /**
     * RtYamlLine returns an empty comment.
     */
    @Test
    public void returnsEmptyComment() {
        YamlLine line = new RtYamlLine("test: value #", 0);
        MatcherAssert.assertThat(
            line.comment(),
            Matchers.isEmptyString()
        );
    }

    /**
     * RtYamlLine returns an empty comment from an empty line.
     */
    @Test
    public void returnsEmptyCommentFromEmptyLine() {
        YamlLine line = new RtYamlLine("", 0);
        MatcherAssert.assertThat(
            line.comment(),
            Matchers.isEmptyString()
        );
    }


    /**
     * RtYamlLine can trim off the anchors.
     * This test is ignored until Issue 374 gets fixed
     * properly. At the moment, we do small workaround.
     */
    @Test
    @Ignore
    public void trimsAnchorOff() {
        YamlLine line = new RtYamlLine("this: line  &anc ", 0);
        MatcherAssert.assertThat(
                line.trimmed(), Matchers.equalTo("this: line")
        );
    }

    /**
     * RtYamlLine returns indentation.
     */
    @Test
    public void knowsIndentation() {
        YamlLine line = new RtYamlLine("this: line", 5);
        MatcherAssert.assertThat(line.indentation(), Matchers.is(0));
    }

    /**
     * RtYamlLine ignores previous indentation if line isn't indented.
     */
    @Test
    public void contentsInline() {
        YamlLine line = new RtYamlLine("this: line ", 5);
        MatcherAssert.assertThat(
                line.contents(-1),
                Matchers.equalTo("this: line "));
    }

    /**
     * RtYamlLine throws exception if indented but previous doesn't match.
     */
    @Test
    public void badIndentationForContent() {
        thrown.expect(YamlReadingException.class);
        thrown.expectMessage("Literal must be indented at least 2 spaces"
                + " from previous element.");
        YamlLine line = new RtYamlLine("  this: line", 5);
        MatcherAssert.assertThat(
                line.contents(4),
                Matchers.is(0));
    }

    /**
     * RtYamlLine if literal is indented more than the previous extract the
     * string.
     */
    @Test
    public void correctIndentationForContent() {
        YamlLine line = new RtYamlLine("      this: line  ", 5);
        MatcherAssert.assertThat(
                line.contents(4),
                Matchers.equalTo("this: line  "));
    }
}
