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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Unit tests for {@link ReadFlowMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 6.0.0
 */
public final class ReadFlowMappingTestCase {

    /**
     * ReadFlowMapping can return the document comment.
     */
    @Test
    public void hasDocumentComment() {
        final YamlMapping map = new ReadFlowMapping(
            new RtYamlLine("{a: b, c: d, e: f}", 2),
            new RtYamlLine("---", 1),
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("# this is a flow mapping document", 0),
                    new RtYamlLine("---", 1),
                    new RtYamlLine("{a: b, c: d, e: f}", 2)
                )
            )
        );
        MatcherAssert.assertThat(
            map.comment().value(),
            Matchers.equalTo("this is a flow mapping document")
        );
        MatcherAssert.assertThat(
            map.comment().yamlNode(),
            Matchers.is(map)
        );
    }

    /**
     * ReadFlowMapping can return the comment referring to it.
     */
    @Test
    public void hasOwnNodeComment() {
        final YamlMapping map = new ReadFlowMapping(
            new RtYamlLine("{a: {i: j}, c: d, e: f}", 2),
            new RtYamlLine("flow:", 1),
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("# this comment about the 'flow' map", 0),
                    new RtYamlLine("flow:", 1),
                    new RtYamlLine("  {a: {i: j}, c: d, e: f}", 2)
                )
            )
        );
        MatcherAssert.assertThat(
            map.comment().value(),
            Matchers.equalTo("this comment about the 'flow' map")
        );
        MatcherAssert.assertThat(
            map.comment().yamlNode(),
            Matchers.is(map)
        );
    }

    /**
     * ReadFlowMapping can return its keys.
     */
    @Test
    public void returnsKeys() {
        final YamlMapping map = new ReadFlowMapping(
            new YamlLine.NullYamlLine(),
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("{['a']: 'b',", 0),
                    new RtYamlLine(" [c,g]: d,", 1),
                    new RtYamlLine(" e: f,", 2),
                    new RtYamlLine("     {y:r}: {h: i},", 3),
                    new RtYamlLine("k: 4,"
                        + "o: [a, '0,3', \"2, 3, 4\", {ii: \"5,6,7\"}, b, c],",
                        4
                    ),
                    new RtYamlLine("t: \"0,3\"}", 5)
                )
            )
        );
        final Set<YamlNode> keys = map.keys();
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(7));
        final Iterator<YamlNode> iterator = keys.iterator();
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo("[a]")
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo("[c, g]")
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "e"
                + System.lineSeparator()
                + "..."
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo("{y: r}")
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "k"
                + System.lineSeparator()
                + "..."
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "o"
                + System.lineSeparator()
                + "..."
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "t"
                + System.lineSeparator()
                + "..."
            )
        );
    }

    /**
     * ReadFlowMapping can return all the values.
     */
    @Test
    public void returnsAllValues() {
        final YamlMapping map = new ReadFlowMapping(
            new YamlLine.NullYamlLine(),
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("{['a']: 'b',", 0),
                    new RtYamlLine(" [c,g]: d,", 1),
                    new RtYamlLine(" e: f,", 2),
                    new RtYamlLine("     {y:r}: {h: i},", 3),
                    new RtYamlLine("k: 4,"
                        + "o: [a, '0,3', \"2, 3, 4\", {ii: \"5,6,7\"}, b, c],",
                        4
                    ),
                    new RtYamlLine("t: \"0,3\"}", 5)
                )
            )
        );
        final Collection<YamlNode> values = map.values();
        MatcherAssert.assertThat(values.size(), Matchers.equalTo(7));
        final Iterator<YamlNode> iterator = values.iterator();
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "b"
                + System.lineSeparator()
                + "..."
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "d"
                + System.lineSeparator()
                + "..."
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "f"
                + System.lineSeparator()
                + "..."
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo("{h: i}")
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "4"
                + System.lineSeparator()
                + "..."
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "[a, \"0,3\", \"2, 3, 4\", {ii: \"5,6,7\"}, b, c]"
            )
        );
        MatcherAssert.assertThat(
            iterator.next().toString(),
            Matchers.equalTo(
                "---"
                + System.lineSeparator()
                + "\"0,3\""
                + System.lineSeparator()
                + "..."
            )
        );
    }

    /**
     * It can return values mapped to string keys.
     */
    @Test
    public void returnsValuesOfStringKeys() {
        final YamlMapping map = new ReadFlowMapping(
            new YamlLine.NullYamlLine(),
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("{a:b,c:d,e:f,g:h}", 0)
                )
            )
        );
        MatcherAssert.assertThat(
            map.string("a"),
            Matchers.equalTo("b")
        );
        MatcherAssert.assertThat(
            map.string("c"),
            Matchers.equalTo("d")
        );
        MatcherAssert.assertThat(
            map.string("e"),
            Matchers.equalTo("f")
        );
        MatcherAssert.assertThat(
            map.string("g"),
            Matchers.equalTo("h")
        );
    }

    /**
     * It can return values mapped to node keys.
     */
    @Test
    public void returnsValuesOfNodeKeys() {
        final YamlMapping map = new ReadFlowMapping(
            new YamlLine.NullYamlLine(),
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("{a:b,[c]:d,{o:i}:f,g:h}", 0)
                )
            )
        );
        MatcherAssert.assertThat(
            map.string("a"),
            Matchers.equalTo("b")
        );
        MatcherAssert.assertThat(
            map.value(
                Yaml.createYamlSequenceBuilder()
                    .add("c")
                    .build()
            ).asScalar().value(),
            Matchers.equalTo("d")
        );
        MatcherAssert.assertThat(
            map.value(
                Yaml.createYamlMappingBuilder()
                    .add("o", "i")
                    .build()
            ).asScalar().value(),
            Matchers.equalTo("f")
        );
        MatcherAssert.assertThat(
            map.string("g"),
            Matchers.equalTo("h")
        );
    }
}
