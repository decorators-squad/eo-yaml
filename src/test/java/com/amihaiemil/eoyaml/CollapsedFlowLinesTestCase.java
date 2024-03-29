/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Unit tests for {@link CollapsedFlowLines}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @checkstyle ExecutableStatementCount (1000 lines)
 * @since 8.0.0
 */
public final class CollapsedFlowLinesTestCase {

    /**
     * CollapsedFlowLines can collapse the flow-style sequences within
     * a block document.
     */
    @Test
    public void collapsesArraysInBlockSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("- [o, d, p]", 0));
        lines.add(new RtYamlLine("-", 1));
        lines.add(new RtYamlLine("  [a,", 2));
        lines.add(new RtYamlLine("\'b]\',", 3));
        lines.add(new RtYamlLine("c]", 4));
        lines.add(new RtYamlLine("- otherScalar", 5));
        lines.add(new RtYamlLine("- other: value", 6));
        lines.add(new RtYamlLine("- {a: b}", 7));
        lines.add(new RtYamlLine("- [sss, sad]", 8));
        lines.add(new RtYamlLine("- [x, ", 9));
        lines.add(new RtYamlLine("\"y]\",", 10));
        lines.add(new RtYamlLine("z]", 11));

        final YamlLines collapsed = new CollapsedFlowLines(
            new AllYamlLines(lines), '[', ']'
        );
        for(final YamlLine line : collapsed) {
            System.out.println(line);
        }
        final Iterator<YamlLine> iterator = collapsed.iterator();
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("- [o, d, p]")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("-")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("  [a, 'b]', c]")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("- otherScalar")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("- other: value")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("- {a: b}")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("- [sss, sad]")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("- [x,  \"y]\", z]")
        );
    }

    /**
     * CollapsedFlowLines can collapse the flow-style mappings within
     * a block document.
     */
    @Test
    public void collapsesMappingsInBlockMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("a: {o: r, d: c, p: [a, b]}", 0));
        lines.add(new RtYamlLine("b:", 1));
        lines.add(new RtYamlLine("  {a: i,", 2));
        lines.add(new RtYamlLine("\'b}\': v,", 3));
        lines.add(new RtYamlLine("c: w}", 4));
        lines.add(new RtYamlLine("c: otherScalar", 5));
        lines.add(new RtYamlLine("d: [other, value]", 6));
        lines.add(new RtYamlLine("y: {a: b}", 7));
        lines.add(new RtYamlLine("x: [sss, sad]", 8));
        lines.add(new RtYamlLine("u: {x: c, ", 9));
        lines.add(new RtYamlLine("\"y:}\": u,", 10));
        lines.add(new RtYamlLine("z: [x]}", 11));

        final YamlLines collapsed = new CollapsedFlowLines(
            new AllYamlLines(lines), '{', '}'
        );
        for(final YamlLine line : collapsed) {
            System.out.println(line);
        }
        final Iterator<YamlLine> iterator = collapsed.iterator();
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("a: {o: r, d: c, p: [a, b]}")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("b:")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("  {a: i, 'b}': v, c: w}")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("c: otherScalar")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("d: [other, value]")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("y: {a: b}")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("x: [sss, sad]")
        );
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo("u: {x: c,  \"y:}\": u, z: [x]}")
        );
    }

    /**
     * CollapsedFlowLines can collapse a whole flow-style sequence document.
     */
    @Test
    public void collapsesWholeFlowArrayDocument() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("[a, b, c", 0));
        lines.add(new RtYamlLine(",d, e, f, {g: e},", 0));
        lines.add(new RtYamlLine("xyz, [y, u, u]]", 0));

        final YamlLines collapsed = new CollapsedFlowLines(
            new AllYamlLines(lines), '[', ']'
        );
        final Iterator<YamlLine> iterator = collapsed.iterator();
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo(
                "[a, b, c ,d, e, f, {g: e}, xyz, [y, u, u]]"
            )
        );
        MatcherAssert.assertThat(iterator.hasNext(), Matchers.is(false));
    }

    /**
     * CollapsedFlowLines can collapse a whole flow-style mapping document.
     */
    @Test
    public void collapsesWholeFlowMappingDocument() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("{a: b, c: d, e: f", 0));
        lines.add(new RtYamlLine(",g: h, i: {g: e},", 0));
        lines.add(new RtYamlLine("u: [a,v], w: [y, u, u]}", 0));

        final YamlLines collapsed = new CollapsedFlowLines(
            new AllYamlLines(lines), '{', '}'
        );
        final Iterator<YamlLine> iterator = collapsed.iterator();
        MatcherAssert.assertThat(
            iterator.next().value(),
            Matchers.equalTo(
                "{a: b, c: d, e: f ,g: h, i: {g: e}, u: [a,v], w: [y, u, u]}"
            )
        );
        MatcherAssert.assertThat(iterator.hasNext(), Matchers.is(false));
    }

}
