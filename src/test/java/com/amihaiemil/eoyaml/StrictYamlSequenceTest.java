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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for {@link StrictYamlSequence}.
 * @author Salavat.Yalalov (s.yalalov@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class StrictYamlSequenceTest {
    /**
     * StrictYamlSequence can fetch its values.
     */
    @Test
    public void fetchesValues() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new PlainStringScalar("key1"));
        elements.add(new PlainStringScalar("key2"));
        elements.add(new PlainStringScalar("key3"));
        YamlSequence sequence = new StrictYamlSequence(
            new RtYamlSequence(elements)
        );

        MatcherAssert.assertThat(
            sequence.values().size(),
            Matchers.equalTo(3)
        );
    }

    /**
     * StrictYamlSequence can be iterated.
     */
    @Test
    public void strictSequenceIsIterable() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new PlainStringScalar("key1"));
        elements.add(new PlainStringScalar("key2"));
        elements.add(new PlainStringScalar("key3"));
        YamlSequence seq = new StrictYamlSequence(
            new RtYamlSequence(elements)
        );
        MatcherAssert.assertThat(seq, Matchers.not(Matchers.emptyIterable()));
        MatcherAssert.assertThat(seq, Matchers.iterableWithSize(3));
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded YamlMapping is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullMapping() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.yamlMapping(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.yamlMapping(1);
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded YamlSequence is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullSequence() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.yamlSequence(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.yamlSequence(1);
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded String is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.string(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.string(1);
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded folded block scalar is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullFoldedBlockScalarString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.foldedBlockScalar(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.foldedBlockScalar(1);
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded literal block scalar is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullLiteralBlockScalarString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.literalBlockScalar(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.literalBlockScalar(1);
    }

    /**
     * StrictYamlSequence can return the number of elements in a sequence.
     */
    @Test
    public void returnsSize() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new PlainStringScalar("key1"));
        elements.add(new PlainStringScalar("key2"));
        YamlSequence sequence = new StrictYamlSequence(
            new RtYamlSequence(elements)
        );

        MatcherAssert.assertThat(
            sequence.size(),
            Matchers.equalTo(2)
        );
    }

    /**
     * StringYamlSequence can fetch a YamlMapping based in its index.
     */
    @Test
    public void returnsMapping() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        YamlMapping found = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.yamlMapping(1)).thenReturn(found);
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
            strict.yamlMapping(1), Matchers.equalTo(found)
        );
    }

    /**
     * StringYamlSequence can fetch a YamlSequence based in its index.
     */
    @Test
    public void returnsSequence() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        YamlSequence found = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.yamlSequence(1)).thenReturn(found);
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
            strict.yamlSequence(1), Matchers.equalTo(found)
        );
    }

    /**
     * StringYamlSequence can fetch a String based in its index.
     */
    @Test
    public void returnsString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.string(1)).thenReturn("found");
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
                strict.string(1), Matchers.equalTo("found")
        );
    }

    /**
     * StrictYamlSequence can fetch a folded block scalar
     * as String based in its index.
     */
    @Test
    public void returnsFoldedBlockScalarString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.foldedBlockScalar(1)).thenReturn("found");
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
            strict.foldedBlockScalar(1), Matchers.equalTo("found")
        );
    }

    /**
     * StrictYamlSequence can fetch a literal block scalar
     * as String based in its index.
     */
    @Test
    public void returnsLiteralBlockScalar() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Collection<String> lines = Mockito.mock(Collection.class);
        Mockito.when(origin.literalBlockScalar(1)).thenReturn(lines);
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
            strict.literalBlockScalar(1),
            Matchers.is(lines)
        );
    }

    /**
     * StrictYamlSequence can return the Comment.
     */
    @Test
    public void returnsComment() {
        final Comment com = Mockito.mock(Comment.class);
        final YamlSequence original = Mockito.mock(YamlSequence.class);
        Mockito.when(original.comment()).thenReturn(com);
        MatcherAssert.assertThat(
            new StrictYamlSequence(original).comment(),
            Matchers.is(com)
        );
    }

}
