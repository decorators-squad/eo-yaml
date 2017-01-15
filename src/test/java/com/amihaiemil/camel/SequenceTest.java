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
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link Sequence}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class SequenceTest {

    /**
     * Sequence throws ISE because the parent cannot be a Scalar.
     * Scalars cannot have children!
     */
    @Test (expected = IllegalStateException.class)
    public void scalarParent() {
        new Sequence(
            new Scalar(Mockito.mock(AbstractNode.class), "a"),
            new LinkedList<AbstractNode>()
        );
    }

    /**
     * Sequence can fetch its children.
     */
    @Test
    public void fetchesChildren() {
        List<AbstractNode> nodes = new LinkedList<>();
        nodes.add(Mockito.mock(AbstractNode.class));
        nodes.add(Mockito.mock(AbstractNode.class));
        nodes.add(Mockito.mock(AbstractNode.class));
        Sequence seq = new Sequence(Mockito.mock(AbstractNode.class), nodes);
        MatcherAssert.assertThat(
            seq.children(), Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(seq.children().size(), Matchers.equalTo(3));
    }
    
    /**
     * A Sequence is ordered.
     */
    @Test
    public void sequenceIsOrdered() {
        List<AbstractNode> nodes = new LinkedList<>();
        Scalar first = new Scalar(Mockito.mock(AbstractNode.class), "test");
        Scalar sec = new Scalar(Mockito.mock(AbstractNode.class), "mihai");
        Scalar third = new Scalar(Mockito.mock(AbstractNode.class), "amber");
        Scalar fourth = new Scalar(Mockito.mock(AbstractNode.class), "5433");
        nodes.add(first);
        nodes.add(sec);
        nodes.add(third);
        nodes.add(fourth);
        Sequence seq = new Sequence(Mockito.mock(AbstractNode.class), nodes);
        Iterator<AbstractNode> ordered = seq.children().iterator();
        MatcherAssert.assertThat(
            (Scalar) ordered.next(), Matchers.equalTo(fourth)
        );
        MatcherAssert.assertThat(
            (Scalar) ordered.next(), Matchers.equalTo(third)
        );
        MatcherAssert.assertThat(
            (Scalar) ordered.next(), Matchers.equalTo(sec)
        );
        MatcherAssert.assertThat(
            (Scalar) ordered.next(), Matchers.equalTo(first)
        );
    }
    
    /**
     * Scalar can compare itself to another Scalar. 
     */
    @Test
    public void comparesToScalar() {
        Sequence seq = new Sequence(
            Mockito.mock(AbstractNode.class),
            new LinkedList<AbstractNode>()
        );
        Scalar scalar = new Scalar(
            Mockito.mock(AbstractNode.class), "java"
        );
        MatcherAssert.assertThat(
            seq.compareTo(scalar),
            Matchers.greaterThan(0)
        );
    }

    /**
     * Scalar can compare itself to a Mapping.
     */
    @Test
    public void comparesToMapping() {
        Sequence seq = new Sequence(
            Mockito.mock(AbstractNode.class),
            new LinkedList<AbstractNode>()
        );
        Mapping map = new Mapping(
            Mockito.mock(AbstractNode.class),
            new HashMap<AbstractNode, AbstractNode>()
        );
        MatcherAssert.assertThat(seq.compareTo(map), Matchers.lessThan(0));
    }

    /**
     * Scalar can compare itself to a Sequence.
     * @checkstyle ExecutableStatementCount (100 lines)
     */
    @Test
    public void comparesToSequence() {
        List<AbstractNode> nodes = new LinkedList<>();
        Scalar first = new Scalar(Mockito.mock(AbstractNode.class), "test");
        Scalar sec = new Scalar(Mockito.mock(AbstractNode.class), "mihai");
        Scalar third = new Scalar(Mockito.mock(AbstractNode.class), "amber");
        Scalar fourth = new Scalar(Mockito.mock(AbstractNode.class), "5433");
        nodes.add(first);
        nodes.add(sec);
        nodes.add(third);
        nodes.add(fourth);
        Sequence seq = new Sequence(Mockito.mock(AbstractNode.class), nodes);
        Sequence other = new Sequence(Mockito.mock(AbstractNode.class), nodes);
        
        nodes.remove(0);
        Sequence another = new Sequence(
            Mockito.mock(AbstractNode.class), nodes
        );
        
        nodes.remove(0);
        nodes.add(0, new Scalar(Mockito.mock(AbstractNode.class), "yaml"));
        Sequence sameSize = new Sequence(
            Mockito.mock(AbstractNode.class), nodes
        );

        MatcherAssert.assertThat(seq.compareTo(seq), Matchers.equalTo(0));
        MatcherAssert.assertThat(seq.compareTo(null), Matchers.greaterThan(0));
        MatcherAssert.assertThat(seq.compareTo(other), Matchers.equalTo(0));
        MatcherAssert.assertThat(
            seq.compareTo(another), Matchers.greaterThan(0)
        );
        MatcherAssert.assertThat(
            another.compareTo(sameSize), Matchers.lessThan(0)
        );
    }

    
}
