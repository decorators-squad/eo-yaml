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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RtYamlMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @ince 1.0.0
 *
 */
public final class RtYamlMappingTest {

    /**
     * Mapping can fetch its children.
     */
    @Test
    public void fetchesChildren() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        mappings.put(new Scalar("key1"), Mockito.mock(YamlNode.class));
        mappings.put(new Scalar("key2"), Mockito.mock(YamlNode.class));
        mappings.put(new Scalar("key3"), Mockito.mock(YamlNode.class));
        RtYamlMapping map = new RtYamlMapping(mappings);
        MatcherAssert.assertThat(
            map.children(), Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(map.children().size(), Matchers.equalTo(3));
    }

    /**
     * Mapping is ordered by keys.
     */
    @Test
    public void orderedKeys() {
        Map<YamlNode, YamlNode> mappings = new HashMap<>();
        Scalar one = new Scalar("value1");
        Scalar two = new Scalar("value1");
        Scalar three = new Scalar("value1");

        mappings.put(new Scalar("key3"), one);
        mappings.put(new Scalar("key2"), two);
        mappings.put(new Scalar("key1"), three);
        RtYamlMapping map = new RtYamlMapping(mappings);
        Iterator<YamlNode> children = map.children().iterator();
        MatcherAssert.assertThat(
            (Scalar) children.next(), Matchers.equalTo(three)
        );
        MatcherAssert.assertThat(
            (Scalar) children.next(), Matchers.equalTo(two)
        );
        MatcherAssert.assertThat(
            (Scalar) children.next(), Matchers.equalTo(one)
        );
    }
    
    /**
     * Mapping can compare itself to a Scalar. 
     */
    @Test
    public void comparesToScalar() {
        RtYamlMapping map = new RtYamlMapping(
            new HashMap<YamlNode, YamlNode>()
        );
        Scalar scalar = new Scalar("java");
        MatcherAssert.assertThat(
            map.compareTo(scalar),
            Matchers.greaterThan(0)
        );
    }

    /**
     * Mapping can compare itself to a Sequence.
     */
    @Test
    public void comparesToSequence() {
        RtYamlMapping map = new RtYamlMapping(
            new HashMap<YamlNode, YamlNode>()
        );
        RtYamlSequence seq = new RtYamlSequence(new LinkedList<YamlNode>());
        MatcherAssert.assertThat(
            map.compareTo(seq),
            Matchers.greaterThan(0)
        );
    }
    
    /**
     * Scalar can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        //....
    }

}
