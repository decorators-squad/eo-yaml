/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
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

import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link EmptyYamlMapping}.
 *
 * @author Andrew Newman
 * @version $Id$
 * @since 5.1.17
 */
public final class EmptyYamlMappingTest {

    /**
     * Random comment.
     */
    public static final String ANY_COMMENT = UUID.randomUUID().toString();

    /**
     * Random key.
     */
    public static final String ANY_KEY = UUID.randomUUID().toString();

    /**
     * Able to get comment.
     */
    @Test
    public void returnsCommentFromDecoratedObject() {
        final YamlMapping mapping = Mockito.mock(YamlMapping.class);
        final YamlNode node = Mockito.mock(YamlNode.class);
        final Comment comment = new BuiltComment(node, ANY_COMMENT);
        Mockito.when(mapping.comment()).thenReturn(comment);
        YamlMapping emptyMapping = new EmptyYamlMapping(mapping);
        MatcherAssert.assertThat(
                emptyMapping.comment().value(),
                Matchers.equalTo(ANY_COMMENT)
        );
    }

    /**
     * Has no elements in the mapping - no keys.
     */
    @Test
    public void hasEmptyKeys() {
        final YamlMapping mapping = Mockito.mock(YamlMapping.class);
        YamlMapping emptyMapping = new EmptyYamlMapping(mapping);
        MatcherAssert.assertThat(
                emptyMapping.keys().size(),
                Matchers.is(0)
        );
    }

    /**
     * Has no elements in the mapping - no values.
     */
    @Test
    public void hasNoValues() {
        final YamlMapping mapping = Mockito.mock(YamlMapping.class);
        YamlMapping emptyMapping = new EmptyYamlMapping(mapping);
        MatcherAssert.assertThat(
                emptyMapping.value(ANY_KEY),
                Matchers.is(Matchers.nullValue())
        );
    }
}
