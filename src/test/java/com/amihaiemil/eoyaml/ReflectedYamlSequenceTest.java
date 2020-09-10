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

import static com.amihaiemil.eoyaml.Comment.UNKNOWN_LINE_NUMBER;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

/**
 * Unit tests for {@link ReflectedYamlSequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.3.3
 */
public final class ReflectedYamlSequenceTest {

    /**
     * A reflected sequence has no comment.
     */
    @Test
    public void returnsEmptyComment() {
        final YamlSequence sequence = new ReflectedYamlSequence(
            new ArrayList<String>()
        );
        MatcherAssert.assertThat(
            sequence.comment().yamlNode(),
            Matchers.is(sequence)
        );
        MatcherAssert.assertThat(
            sequence.comment().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            sequence.comment().number(),
            Matchers.is(UNKNOWN_LINE_NUMBER)
        );
    }

    /**
     * A reflected YamlSequence can be created from a Collection.
     */
    @Test
    public void instantiatesWithCollection() {
        new ReflectedYamlSequence(new ArrayList<>());
        new ReflectedYamlSequence(new LinkedList<>());
        new ReflectedYamlSequence(new HashSet<>());
        new ReflectedYamlSequence(new LinkedHashSet<>());
        new ReflectedYamlSequence(Mockito.mock(Collection.class));
    }

    /**
     * A reflected YamlSequence can be created from an Array.
     */
    @Test
    public void instantiatesWithArray() {
        new ReflectedYamlSequence(new String[] {"one", "two", "three"});
        new ReflectedYamlSequence(new Object[] {});
        new ReflectedYamlSequence(new Integer[] {});
        new ReflectedYamlSequence(new Double[] {});
    }

    /**
     * ReflectedYamlSequence can reflect String scalars from an array.
     */
    @Test
    public void reflectsStringArray() {
        final YamlSequence sequence = new ReflectedYamlSequence(
            new String[] {"one", "two", "three"}
        );
        MatcherAssert.assertThat(sequence, Matchers.iterableWithSize(3));
        final Iterator<YamlNode> valuesIt = sequence.values().iterator();
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("one")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("two")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("three")
        );
    }

    /**
     * ReflectedYamlSequence can reflect String scalars from a collection.
     */
    @Test
    public void reflectsStringCollection() {
        final YamlSequence sequence = new ReflectedYamlSequence(
            Arrays.asList("one", "two", "three")
        );
        MatcherAssert.assertThat(sequence, Matchers.iterableWithSize(3));
        final Iterator<YamlNode> valuesIt = sequence.values().iterator();
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("one")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("two")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("three")
        );
    }

    /**
     * ReflectedYamlSequence can reflect Integer scalars from an array.
     */
    @Test
    public void reflectsIntegerArray() {
        final YamlSequence sequence = new ReflectedYamlSequence(
            new Integer[] {1, 2, 3}
        );
        MatcherAssert.assertThat(sequence, Matchers.iterableWithSize(3));
        final Iterator<YamlNode> valuesIt = sequence.values().iterator();
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("1")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("2")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("3")
        );
    }

    /**
     * ReflectedYamlSequence can reflect Integer scalars from a collection.
     */
    @Test
    public void reflectsIntegerCollection() {
        final YamlSequence sequence = new ReflectedYamlSequence(
            Arrays.asList(1, 2, 3)
        );
        MatcherAssert.assertThat(sequence, Matchers.iterableWithSize(3));
        final Iterator<YamlNode> valuesIt = sequence.values().iterator();
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("1")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("2")
        );
        MatcherAssert.assertThat(
            ((ReflectedYamlScalar) valuesIt.next()).value(),
            Matchers.equalTo("3")
        );
    }

    /**
     * Use the wrong object to construct a sequence - non-collection or array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenWrongObject() {
        new ReflectedYamlSequence("wrong");
    }
}
