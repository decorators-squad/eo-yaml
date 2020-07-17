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
 * Unit tests for {@link ReadFlowSequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 6.0.0
 */
public final class ReadFlowSequenceTestCase {

    /**
     * Sequences in flow/json style have no comment.
     */
    @Test
    public void hasNoComment() {
        final YamlSequence seq = new ReadFlowSequence(
            Mockito.mock(YamlLine.class)
        );
        MatcherAssert.assertThat(
            seq.comment().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            seq.comment().yamlNode(),
            Matchers.is(seq)
        );
    }

    /**
     * A ReadFlowSequence can be empty.
     */
    @Test
    public void hasNoValues() {
        final YamlSequence seq = new ReadFlowSequence(
            new RtYamlLine("[]", 0)
        );
        MatcherAssert.assertThat(
            seq.values(),
            Matchers.emptyIterable()
        );
    }

    /**
     * A ReadFlowSequence works if it has only scalars in it.
     */
    @Test
    public void hasOnlyScalars() {
        final YamlSequence seq = new ReadFlowSequence(
            new RtYamlLine("[a, b, c, d:e]", 0)
        );
        final Collection<YamlNode> values = seq.values();
        MatcherAssert.assertThat(values, Matchers.iterableWithSize(4));
        final Iterator<YamlNode> iterator = values.iterator();
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("a")
        );
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("b")
        );
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("c")
        );
        MatcherAssert.assertThat(
            ((Scalar) iterator.next()).value(),
            Matchers.equalTo("d:e")
        );
    }

    /**
     * A ReadFlowSequence works if it has only sequences in it.
     */
    @Test
    public void hasOnlySequences() {
        final YamlSequence seq = new ReadFlowSequence(
            new RtYamlLine("[[a, b], [c], [d, e, f]]", 0)
        );
        final Collection<YamlNode> values = seq.values();
        MatcherAssert.assertThat(values, Matchers.iterableWithSize(3));
    }

    List<String> found = new ArrayList<>();

    /**
     * A ReadFlowSequence works if it has only sequences in it.
     */
    @Test
    public void test() {
        String text = "vv, [a, x, c, {y:y}, 'c]', escaped], hh, [d, [e, [\"a\", \"]b\"]], f], uu, {ds:d}, ddd, ax, ['its'], as";
        int nested = 0;
        StringBuilder scalar = new StringBuilder();
        for(int i=0; i< text.length();i++) {
            if(text.charAt(i) == '[') {
                i = this.readNode(i, text, '[', ']');
            } else if(text.charAt(i) == '{') {
                i = this.readNode(i, text, '{', '}');
            } else if(text.charAt(i) != ',' && text.charAt(i) != ' ') {
                scalar.append(text.charAt(i));
                if(i==text.length()-1) {
                    found.add(scalar.toString().trim());
                }
            } else {
                if(!scalar.toString().trim().isEmpty()) {
                    found.add(scalar.toString().trim());
                    scalar = new StringBuilder();
                }
            }
        }
        for(String s : found) {
            System.out.println(s);
        }
    }

    public int readNode(final int start, final String text, final char opening, final char closing) {
        final StringBuilder node = new StringBuilder();
        node.append(text.charAt(start));
        int nested = 1;
        int i = start;
        while(nested != 0) {
            i++;
            if(i == text.length()) {
                throw new IllegalStateException(
                    "Could not find closing square bracket!"
                );
            }
            i = goOverEscapedValue(node, i, text, '\"');
            i = goOverEscapedValue(node, i, text, '\'');
            node.append(text.charAt(i));
            if(text.charAt(i) == opening){
                nested++;
            } else if(text.charAt(i) == closing) {
                nested--;
            }
        }
        found.add(node.toString());
        return i;
    }

    public int goOverEscapedValue(
        final StringBuilder node,
        final int start,
        final String text,
        final char escapeChar
    ) {
        int i = start;
        if(text.charAt(i) == escapeChar) {
            node.append(text.charAt(i));
            i++;
            while(text.charAt(i) != escapeChar) {
                node.append(text.charAt(i));
                i++;
                if(i == text.length()) {
                    throw new IllegalStateException(
                        "Could not find closing pair (" + escapeChar
                      + ") for escaped value starting at " + start
                    );
                }
            }
        }
        return i;
    }
}
