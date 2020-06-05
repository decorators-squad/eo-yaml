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

import java.util.*;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link StrictYamlMapping}.
 * @checkstyle LineLength (1000 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class StrictYamlMappingTest {

    /**
     * StrictYamlMapping can fetch its values.
     */
    @Test
    public void fetchesValues() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlNode.class));
        mappings.put(new PlainStringScalar("key2"), Mockito.mock(YamlNode.class));
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlNode.class));
        YamlMapping map = new StrictYamlMapping(new RtYamlMapping(mappings));
        MatcherAssert.assertThat(
            map.values(), Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(map.values().size(), Matchers.equalTo(3));
    }

    /**
     * StrictYamlMapping can fetch its keys.
     */
    @Test
    public void fetchesKeys() {
        Map<YamlNode, YamlNode> mappings = new LinkedHashMap<>();
        mappings.put(new PlainStringScalar("key1"), Mockito.mock(YamlNode.class));
        mappings.put(new PlainStringScalar("key2"), Mockito.mock(YamlNode.class));
        mappings.put(new PlainStringScalar("key3"), Mockito.mock(YamlNode.class));
        YamlMapping map = new StrictYamlMapping(new RtYamlMapping(mappings));
        MatcherAssert.assertThat(
            map.keys(), Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(map.keys().size(), Matchers.equalTo(3));
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded YamlMapping is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullMapping() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.value("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.yamlMapping("key");
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded YamlSequence is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullSequence() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.value("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.yamlSequence("key");
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded String is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullString() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.value("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.string("key");
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded value is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullValue() {
        PlainStringScalar key = new PlainStringScalar("key");
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.value(key)).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.string(key);
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded folded block scalar is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullFoldedBlockScalarString() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        PlainStringScalar key = new PlainStringScalar("key");
        Mockito.when(origin.value(key)).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.foldedBlockScalar(key);
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded literal block scalar is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullLiteralBlockScalarString() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        PlainStringScalar key = new PlainStringScalar("key");
        Mockito.when(origin.value(key)).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.literalBlockScalar(key);
    }

    /**
     * StringYamlMapping can fetch a YamlMapping based in its key.
     */
    @Test
    public void returnsMapping() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        YamlMapping found = Mockito.mock(YamlMapping.class);
        YamlNode key = new PlainStringScalar("key");
        Mockito.when(origin.value(key)).thenReturn(found);
        YamlMapping strict = new StrictYamlMapping(origin);
        MatcherAssert.assertThat(
            strict.yamlMapping("key"), Matchers.equalTo(found)
        );
    }

    /**
     * StringYamlMapping can fetch a YamlSequence based in its key.
     */
    @Test
    public void returnsSequence() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        YamlSequence found = Mockito.mock(YamlSequence.class);
        YamlNode key = new PlainStringScalar("key");
        Mockito.when(origin.value(key)).thenReturn(found);
        YamlMapping strict = new StrictYamlMapping(origin);
        MatcherAssert.assertThat(
            strict.yamlSequence("key"), Matchers.equalTo(found)
        );
    }

    /**
     * StringYamlMapping can fetch a String based in its key.
     */
    @Test
    public void returnsString() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        YamlNode key = new PlainStringScalar("key");
        Mockito.when(origin.value(key)).thenReturn(
            new PlainStringScalar("found")

        );
        YamlMapping strict = new StrictYamlMapping(origin);
        MatcherAssert.assertThat(
            strict.string("key"), Matchers.equalTo("found")
        );
    }

    /**
     * StrictYamlSequence can fetch a folded block scalar
     * as String based in its key.
     */
    @Test
    public void returnsFoldedBlockScalarString() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        YamlNode key = new PlainStringScalar("key");
        final Scalar found = new RtYamlScalarBuilder.BuiltFoldedBlockScalar(
            Arrays.asList("found")
        );
        Mockito.when(origin.value(key)).thenReturn(found);
        YamlMapping strict = new StrictYamlMapping(origin);
        MatcherAssert.assertThat(
            strict.foldedBlockScalar(key),
            Matchers.equalTo("found")
        );
    }

    /**
     * StringYamlMapping can fetch a literal block scalar
     * as String based in its key.
     */
    @Test
    public void returnsLiteralBlockScalar() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        YamlNode key = new PlainStringScalar("key");
        List<String> lines = Arrays.asList("line1", "line2");
        Mockito.when(origin.value(key)).thenReturn(
            new RtYamlScalarBuilder.BuiltLiteralBlockScalar(lines)
        );
        YamlMapping strict = new StrictYamlMapping(origin);
        MatcherAssert.assertThat(
            strict.literalBlockScalar(key),
            Matchers.is(lines)
        );
    }

    /**
     * StringYamlMapping can fetch a value based in its key.
     */
    @Test
    public void returnsValue() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        YamlNode key = new PlainStringScalar("key");
        YamlNode value = new PlainStringScalar("value");
        Mockito.when(origin.value(key)).thenReturn(value);
        YamlMapping strict = new StrictYamlMapping(origin);
        MatcherAssert.assertThat(
            strict.value(key), Matchers.equalTo(value)
        );
    }

    /**
     * StrictYamlMapping can return the Comment.
     */
    @Test
    public void returnsComment() {
        final Comment com = Mockito.mock(Comment.class);
        final YamlMapping original = Mockito.mock(YamlMapping.class);
        Mockito.when(original.comment()).thenReturn(com);
        MatcherAssert.assertThat(
            new StrictYamlMapping(original).comment(),
            Matchers.is(com)
        );
    }
}
