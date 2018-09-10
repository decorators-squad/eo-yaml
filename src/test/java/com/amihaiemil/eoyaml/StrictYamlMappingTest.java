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

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link StrictYamlMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class StrictYamlMappingTest {

    /**
     * StrictYamlMapping can fetch its children.
     */
    @Test
    public void fetchesChildren() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key1"), Mockito.mock(YamlNode.class));
        mappings.put(new Scalar("key2"), Mockito.mock(YamlNode.class));
        mappings.put(new Scalar("key3"), Mockito.mock(YamlNode.class));
        YamlMapping map = new StrictYamlMapping(new RtYamlMapping(mappings));
        MatcherAssert.assertThat(
            map.children(), Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(map.children().size(), Matchers.equalTo(3));
    }
    
    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded YamlMapping is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullMapping() {
        AbstractYamlMapping origin = Mockito.mock(AbstractYamlMapping.class);
        Mockito.when(origin.yamlMapping("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.yamlMapping("key");
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded YamlSequence is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullSequence() {
        AbstractYamlMapping origin = Mockito.mock(AbstractYamlMapping.class);
        Mockito.when(origin.yamlSequence("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.yamlSequence("key");
    }

    /**
     * StringYamlMapping can throw YamlNodeNotFoundException
     * when the demanded String is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullString() {
        AbstractYamlMapping origin = Mockito.mock(AbstractYamlMapping.class);
        Mockito.when(origin.string("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.string("key");
    }

    /**
     * StringYamlMapping can fetch a YamlMapping based in its key.
     */
    @Test
    public void returnsMapping() {
        AbstractYamlMapping origin = Mockito.mock(AbstractYamlMapping.class);
        YamlMapping found = Mockito.mock(YamlMapping.class);
        YamlNode key = new Scalar("key");
        Mockito.when(origin.yamlMapping(key)).thenReturn(found);
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
        AbstractYamlMapping origin = Mockito.mock(AbstractYamlMapping.class);
        YamlSequence found = Mockito.mock(YamlSequence.class);
        YamlNode key = new Scalar("key");
        Mockito.when(origin.yamlSequence(key)).thenReturn(found);
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
        AbstractYamlMapping origin = Mockito.mock(AbstractYamlMapping.class);
        YamlNode key = new Scalar("key");
        Mockito.when(origin.string(key)).thenReturn("found");
        YamlMapping strict = new StrictYamlMapping(origin);
        MatcherAssert.assertThat(
            strict.string("key"), Matchers.equalTo("found")
        );
    }
}
