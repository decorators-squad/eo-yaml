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
package com.amihaiemil.camel;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link NoCommentsYamlLine}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class NoCommentsYamlLineTest {
    /**
     * NoCommentsYamlLine knows its number.
     */
    @Test
    public void knowsNumber() {
        YamlLine line = new NoCommentsYamlLine(new RtYamlLine("line", 11));
        MatcherAssert.assertThat(line.number(), Matchers.is(11));
    }
    
    /**
     * NoCommentsYamlLine returns its, indentation if it's right.
     */
    @Test
    public void isWellIndented() {
        YamlLine byZero = new NoCommentsYamlLine(
            new RtYamlLine("this: line", 5)
        );
        MatcherAssert.assertThat(
            byZero.indentation(), Matchers.is(0)
        );
        YamlLine byFour = new NoCommentsYamlLine(
                new RtYamlLine("    this: line", 10)
        );
        MatcherAssert.assertThat(
            byFour.indentation(), Matchers.is(4)
        );
    }
    
    /**
     * NoCommentsYamlLine compares right with other lines.
     */
    @Test
    public void comparesRight() {
        YamlLine noComments = new NoCommentsYamlLine(
            new RtYamlLine("abc", 1)
        );
        YamlLine normalLine = new RtYamlLine("  no", 2);
        MatcherAssert.assertThat(
            noComments.compareTo(normalLine) < 0, Matchers.is(true)
        );
    }
    
    /**
     * NoCommentsYamlLine trims a comment line and return empty line.
     */
    @Test
    public void trimsCommentsLine() {
        YamlLine noComments = new NoCommentsYamlLine(
            new RtYamlLine("   #this is a comment line", 1)
        );
        MatcherAssert.assertThat(
            noComments.trimmed(), Matchers.is("")
        );
    }
    
    /**
     * NoCommentsYamlLine removes only comments and leaves original string.
     */
    @Test
    public void trimsCommentsAtEndOfLine() {
        YamlLine noComments = new NoCommentsYamlLine(
            new RtYamlLine("  this isn't comment   #here is the comment", 1)
        );
        MatcherAssert.assertThat(
            noComments.trimmed(), Matchers.is("this isn't comment")
        );
    }
    
    /**
     * NoCommentsYamlLine doesn't remove # escaped in a string.
     */
    @Test
    public void doesnotTrimsEscapedHash() {
        YamlLine noComments = new NoCommentsYamlLine(
            new RtYamlLine(" \"value = #5\" ", 2)
        );
        MatcherAssert.assertThat(
            noComments.trimmed(), Matchers.is("\"value = #5\"")
        );
    }
}





