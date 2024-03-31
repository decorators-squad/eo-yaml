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
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for {@link ReadYamlMapping}.
 * @checkstyle MethodName (1500 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class ReadYamlMappingTest {

    /**
     * ReadYamlMapping can return the YamlMapping mapped to a
     * String key.
     */
    @Test
    public void returnsYamlMappingWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("third: &something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.print(map);
        final YamlMapping second = map.yamlMapping("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            second.string("fifth"), Matchers.equalTo("values")
        );
        MatcherAssert.assertThat(
            map.string("third"), Matchers.equalTo("&something")
        );
    }

    /**
     * ReadYamlMapping can return its keys.
     */
    @Test
    public void returnsKeys(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("zkey: somethingElse", 0));
        lines.add(new RtYamlLine("bkey: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("akey: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Set<YamlNode> keys = map.keys();
        MatcherAssert.assertThat(
            keys, Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(3));
        final Iterator<YamlNode> iterator = keys.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("zkey"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("bkey"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("akey"))
        );
    }

    /**
     * ReadYamlMapping can return its children.
     */
    @Test
    public void returnsValuesOfStringKeys(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("zkey: somethingElse", 0));
        lines.add(new RtYamlLine("bkey: ", 1));
        lines.add(new RtYamlLine("  bchildkey1: some", 2));
        lines.add(new RtYamlLine("  bchildkey2: values", 3));
        lines.add(new RtYamlLine("akey: something", 4));
        lines.add(new RtYamlLine("ukey: ", 5));
        lines.add(new RtYamlLine("  - seq", 6));
        lines.add(new RtYamlLine("  - value", 7));

        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Collection<YamlNode> children = map.values();
        MatcherAssert.assertThat(
            children, Matchers.iterableWithSize(4)
        );
        System.out.println(children);
        final Iterator<YamlNode> iterator = children.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("somethingElse"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlMappingBuilder()
                    .add("bchildkey1", "some")
                    .add("bchildkey2", "values")
                    .build()
            )
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("something"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlSequenceBuilder()
                    .add("seq")
                    .add("value")
                    .build()
            )
        );
    }

    /**
     * ReadYamlMapping can return its values when there are both
     * String and YamlNode keys.
     * @checkstyle ExecutableStatementCount (100 lines)
     */
    @Test
    public void returnsValuesOfStringAndComplexKeys(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something ", 6));
        lines.add(new RtYamlLine("third: ", 7));
        lines.add(new RtYamlLine("  - singleSeq", 8));
        lines.add(new RtYamlLine("? ", 9));
        lines.add(new RtYamlLine("  - sequence", 10));
        lines.add(new RtYamlLine("  - key", 11));
        lines.add(new RtYamlLine(": simpleValue", 12));

        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.print(map);
        final Collection<YamlNode> children = map.values();
        MatcherAssert.assertThat(
            children, Matchers.iterableWithSize(5)
        );
        final Iterator<YamlNode> iterator = children.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("somethingElse"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlMappingBuilder()
                    .add("map", "value")
                    .build()
            )
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("something"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlSequenceBuilder()
                    .add("singleSeq")
                    .build()
            )
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new PlainStringScalar("simpleValue"))
        );
    }

    /**
     * ReadYamlMapping can return the YamlMapping mapped to a
     * YamlMapping key.
     */
    @Test
    public void returnsYamlMappingWithYamlMappingKey(){
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1").add("complex2", "mapping2").build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlMapping value = map.yamlMapping(key);
        MatcherAssert.assertThat(value, Matchers.notNullValue());
        MatcherAssert.assertThat(
            value, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            value.string("map"), Matchers.equalTo("value")
        );
    }

    /**
     * ReadYamlMapping can return the YamlSequence mapped to a
     * String key.
     */
    @Test
    public void returnsYamlSequenceWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlSequence second = map.yamlSequence("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(
            second.string(0), Matchers.equalTo("some")
        );
        MatcherAssert.assertThat(
            second.string(1), Matchers.equalTo("sequence")
        );
    }

    /**
     * ReadYamlMapping can return the flow inline YamlSequence mapped to a
     * String key after the colon.
     */
    @Test
    public void returnsAfterColonInlineFlowYamlSequenceWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  [some, sequence]", 2));
        lines.add(new RtYamlLine("third: something", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlSequence second = map.yamlSequence("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(
            second.string(0), Matchers.equalTo("some")
        );
        MatcherAssert.assertThat(
            second.string(1), Matchers.equalTo("sequence")
        );
    }

    /**
     * ReadYamlMapping can return the flow inline YamlSequence mapped to a
     * String key at the colon.
     */
    @Test
    public void returnsColonInlineFlowYamlSequenceWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: [some, sequence]", 1));
        lines.add(new RtYamlLine("third: something", 2));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final YamlSequence second = map.yamlSequence("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(
            second.string(0), Matchers.equalTo("some")
        );
        MatcherAssert.assertThat(
            second.string(1), Matchers.equalTo("sequence")
        );
    }

    /**
     * ReadYamlMapping can return the flow multiline YamlSequence mapped to a
     * String key at the colon.
     */
    @Test
    public void returnsColonMultilineFlowYamlSequenceWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: [some,", 1));
        lines.add(new RtYamlLine("other, {a: b},", 2));
        lines.add(new RtYamlLine("{c:", 3));
        lines.add(new RtYamlLine("d},[inner, sequence]],", 4));
        lines.add(new RtYamlLine("third: something", 5));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);

        MatcherAssert.assertThat(map.keys().size(), Matchers.is(3));
        MatcherAssert.assertThat(map.values().size(), Matchers.is(3));

        final YamlSequence second = map.yamlSequence("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(
            second.string(0), Matchers.equalTo("some")
        );
        MatcherAssert.assertThat(
            second.string(1), Matchers.equalTo("other")
        );
        MatcherAssert.assertThat(
            second.yamlMapping(2).string("a"), Matchers.equalTo("b")
        );
        MatcherAssert.assertThat(
            second.yamlMapping(3).string("c"), Matchers.equalTo("d")
        );
        MatcherAssert.assertThat(
            second.yamlSequence(4).string(0), Matchers.equalTo("inner")
        );
        MatcherAssert.assertThat(
            second.yamlSequence(4).string(1), Matchers.equalTo("sequence")
        );
    }

    /**
     * ReadYamlMapping can return the flow multiline YamlSequence mapped to a
     * String key at the colon.
     */
    @Test
    public void returnsColonMultilineFlowYamlMappingWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: {some: mapping,", 1));
        lines.add(new RtYamlLine("c: {a: [c,d]}}", 2));
        lines.add(new RtYamlLine("third: something", 5));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);

        MatcherAssert.assertThat(map.keys().size(), Matchers.is(3));
        MatcherAssert.assertThat(map.values().size(), Matchers.is(3));

        final YamlMapping second = map.yamlMapping("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            second.string("some"), Matchers.equalTo("mapping")
        );
        MatcherAssert.assertThat(
            second.yamlMapping("c").yamlSequence("a").string(0),
            Matchers.equalTo("c")
        );
        MatcherAssert.assertThat(
            second.yamlMapping("c").yamlSequence("a").string(1),
            Matchers.equalTo("d")
        );
    }

    /**
     * ReadYamlMapping can return the empty flow YamlSequence mapped to a
     * String key at the colon.
     */
    @Test
    public void returnsColonEmptyFlowYamlSequenceWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: []", 1));
        lines.add(new RtYamlLine("third: something", 2));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final YamlSequence second = map.yamlSequence("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(
            second.size(), Matchers.is(0)
        );
    }

    /**
     * ReadYamlMapping can return the empty flow YamlSequence mapped to a
     * String key after the colon.
     */
    @Test
    public void returnsAfterColonEmptyFlowYamlSequenceWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  []", 2));
        lines.add(new RtYamlLine("third: something", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final YamlSequence second = map.yamlSequence("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
        MatcherAssert.assertThat(
            second.size(), Matchers.is(0)
        );
    }

    /**
     * ReadYamlMapping can return the empty flow YamlMapping mapped to a
     * String key at the colon.
     */
    @Test
    public void returnsColonEmptyFlowYamlMappingWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: {}", 1));
        lines.add(new RtYamlLine("third: something", 2));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final YamlMapping second = map.yamlMapping("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            second.keys().size(), Matchers.is(0)
        );
        MatcherAssert.assertThat(
            second.values().size(), Matchers.is(0)
        );
    }

    /**
     * ReadYamlMapping can return the empty flow YamlMapping mapped to a
     * String key after the colon.
     */
    @Test
    public void returnsAfterColonEmptyFlowYamlMappingWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  {}", 2));
        lines.add(new RtYamlLine("third: something", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final YamlMapping second = map.yamlMapping("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            second.keys().size(), Matchers.is(0)
        );
        MatcherAssert.assertThat(
            second.values().size(), Matchers.is(0)
        );
    }

    /**
     * ReadYamlMapping can return the YamlMapping mapped to a
     * YamlMapping key.
     */
    @Test
    public void returnsStringWithYamlMappingKey(){
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": value", 4));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.print(map);
        System.out.println(">>>");
        final String value = map.string(key);
        MatcherAssert.assertThat(value, Matchers.notNullValue());
        MatcherAssert.assertThat(value, Matchers.equalTo("value"));
    }

    /**
     * ReadYamlMapping can return the folded block string mapped to a
     * YamlMapping key.
     */
    @Test
    @Ignore
    public void returnsFoldedStringWithYamlMappingKey(){
        final YamlMapping key = new RtYamlMappingBuilder()
                .add("complex1", "mapping1")
                .add("complex2", "mapping2")
                .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": >", 4));
        lines.add(new RtYamlLine("  folded block", 6));
        lines.add(new RtYamlLine("  scalar here", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final String value = map.foldedBlockScalar(key);
        MatcherAssert.assertThat(value, Matchers.notNullValue());
        MatcherAssert.assertThat(
            value,
            Matchers.equalTo("folded block scalar here")
        );
    }

    /**
     * ReadYamlMapping can return the folded block string mapped to a
     * String key.
     */
    @Test
    @Ignore
    public void returnsFoldedStringWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("key:> ", 1));
        lines.add(new RtYamlLine("  folded block", 2));
        lines.add(new RtYamlLine("  scalar here", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final String value = map.foldedBlockScalar("key");
        MatcherAssert.assertThat(value, Matchers.notNullValue());
        MatcherAssert.assertThat(
            value,
            Matchers.equalTo("folded block scalar here")
        );
    }

    /**
     * ReadYamlMapping can return the literal block string mapped to a
     * YamlMapping key.
     */
    @Test
    public void returnsLiteralStringWithYamlMappingKey(){
        final YamlMapping key = new RtYamlMappingBuilder()
                .add("complex1", "mapping1")
                .add("complex2", "mapping2")
                .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(":|", 4));
        lines.add(new RtYamlLine("  line1", 6));
        lines.add(new RtYamlLine("  line2", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Collection<String> literalLines = map.literalBlockScalar(key);
        MatcherAssert.assertThat(
            literalLines.size(),
            Matchers.is(2)
        );
        final Iterator<String> linesIt = literalLines.iterator();
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("line1")
        );
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("line2")
        );
        MatcherAssert.assertThat(
            map.literalBlockScalar("notthere"), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping can return the literal block string mapped to a
     * String key.
     */
    @Test
    public void returnsLiteralStringWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("key:| ", 1));
        lines.add(new RtYamlLine("  line1", 2));
        lines.add(new RtYamlLine("  line2", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Collection<String> literalLines = map.literalBlockScalar("key");
        MatcherAssert.assertThat(
            literalLines.size(),
            Matchers.is(2)
        );
        final Iterator<String> linesIt = literalLines.iterator();
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("line1")
        );
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("line2")
        );
        MatcherAssert.assertThat(
            map.literalBlockScalar("notthere"), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping can return the String mapped to a
     * String key.
     */
    @Test
    public void returnsStringWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final String third = map.string("third");
        MatcherAssert.assertThat(third, Matchers.notNullValue());
        MatcherAssert.assertThat(
            third, Matchers.equalTo("something")
        );
    }

    /**
     * ReadYamlMapping can return the Scalar mapped to a
     * Scalar key.
     */
    @Test
    public void returnsScalarValueWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlNode first = map.value(new PlainStringScalar("first"));
        MatcherAssert.assertThat(first, Matchers.notNullValue());
        MatcherAssert.assertThat(
            first, Matchers.equalTo(new PlainStringScalar("somethingElse"))
        );
    }

    /**
     * ReadYamlMapping can return the Sequence mapped to a
     * Scalar key.
     */
    @Test
    public void returnsSequenceValueWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlNode second = map.value(new PlainStringScalar("second"));
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
    }

    /**
     * ReadYamlMapping can return the Mapping mapped to a
     * Scalar key.
     */
    @Test
    public void returnsMappingValueWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  some: mapping", 2));
        lines.add(new RtYamlLine("third: something", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlNode second = map.value(new PlainStringScalar("second"));
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlMapping.class)
        );
    }

    /**
     * ReadYamlMapping can return null if value is missing.
     */
    @Test
    public void returnsNullIfValueIsMissing(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  some: mapping", 2));
        lines.add(new RtYamlLine("third: something", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            map.value(new PlainStringScalar("notthere")),
            Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.string("notthere"),
            Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.yamlMapping("notthere"),
            Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.yamlSequence("notthere"),
            Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.string(...) returns null if the queried
     * Scalar is not present.
     */
    @Test
    public void returnsNullOnMissingScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.string("notthere"), Matchers.nullValue());
        MatcherAssert.assertThat(map.string(
            new PlainStringScalar("notthere")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.string(YamlMapping) returns null if the queried
     * Scalar is not present.
     */
    @Test
    public void returnsNullOnMissingScalarWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.string(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.string(YamlSequence) returns null if the queried
     * Scalar is not present.
     */
    @Test
    public void returnsNullOnMissingScalarWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.string(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.string(...) returns null if the queried value
     * is present but it is not actually a Scalar.
     */
    @Test
    public void returnsNullOnMisreadScalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.string("second"), Matchers.nullValue());
        MatcherAssert.assertThat(map.string(
            new PlainStringScalar("second")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.string(YamlMapping) returns null if the queried value
     * is present but it is not actually a Scalar.
     */
    @Test
    public void returnsNullOnMisreadScalarWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(":", 4));
        lines.add(new RtYamlLine("  - some", 5));
        lines.add(new RtYamlLine("  - sequence", 6));
        lines.add(new RtYamlLine("second: something", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.print(map);
        System.out.println(">>>");
        MatcherAssert.assertThat(map.string(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.string(YamlSequence) returns null if the queried value
     * is present but it is not actually a Scalar.
     */
    @Test
    public void returnsNullOnMisreadScalarWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  - sequence", 2));
        lines.add(new RtYamlLine("  - key", 3));
        lines.add(new RtYamlLine(":", 4));
        lines.add(new RtYamlLine("  - some", 5));
        lines.add(new RtYamlLine("  - sequence", 6));
        lines.add(new RtYamlLine("second: something", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.string(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlSequence(...) returns null if the queried
     * YamlSequence is not present.
     */
    @Test
    public void returnsNullOnMissingYamlSequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(
            map.yamlSequence("notthere"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(map.yamlSequence(
            new PlainStringScalar("notthere")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.yamlSequence(...) returns null if the queried
     * YamlSequence is not present, based on a complex key.
     */
    @Test
    public void returnsNullOnMissingYamlSequenceWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  some: other", 2));
        lines.add(new RtYamlLine("  complex: key", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlSequence(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlMapping(...) returns null if the queried
     * YamlMapping is not present.
     */
    @Test
    public void returnsNullOnMissingYamlMappingWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  - someOther", 2));
        lines.add(new RtYamlLine("  - sequenceKey", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlMapping(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlMapping(String) returns null if the queried
     * key has a value that is a Scalar.
     */
    @Test
    public void returnsNullOnMisreadYamlMapping_Scalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(
            map.yamlMapping("first"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(map.yamlMapping(
            new PlainStringScalar("first")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.yamlMapping(String) returns null if the queried
     * key has a value that is a YamlSequence.
     */
    @Test
    public void returnsNullOnMisreadYamlMapping_Sequence() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(
            map.yamlMapping("second"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(map.yamlMapping(
            new PlainStringScalar("second")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.yamlMapping(YamlNode) returns null if the queried
     * complex key has a Scalar value.
     */
    @Test
    public void returnsNullOnMisreadYamlMapping_ScalarWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": scalarValue", 4));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlMapping(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlMapping(YamlNode) returns null if the queried
     * complex key has a YamlSequence value.
     */
    @Test
    public void returnsNullOnMisreadYamlMapping_SequenceWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(":", 4));
        lines.add(new RtYamlLine("  - some", 5));
        lines.add(new RtYamlLine("  - seq", 6));
        lines.add(new RtYamlLine("second: something", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlMapping(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlMapping(YamlNode) returns null if the queried
     * complex key has a Scalar value.
     */
    @Test
    public void returnsNullOnMisreadYamlMapping_ScalarWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  - sequence", 2));
        lines.add(new RtYamlLine("  - key", 3));
        lines.add(new RtYamlLine(": scalarValue", 4));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlMapping(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlMapping(YamlNode) returns null if the queried
     * complex key has a YamlSequence value.
     */
    @Test
    public void returnsNullOnMisreadYamlMapping_SequenceWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  - sequence", 2));
        lines.add(new RtYamlLine("  - key", 3));
        lines.add(new RtYamlLine(":", 4));
        lines.add(new RtYamlLine("  - some", 5));
        lines.add(new RtYamlLine("  - sequence", 6));
        lines.add(new RtYamlLine("second: something", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlMapping(key), Matchers.nullValue());
    }

    /**
     * An empty ReadYamlMapping can be printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void printsEmptyYaml() {
        final YamlMapping map = new ReadYamlMapping(
            new AllYamlLines(new ArrayList<>())
        );
        MatcherAssert.assertThat(map.toString(), Matchers.equalTo("{}"));
    }

    /**
     * ReadYamlMapping.yamlMapping(...) returns null if the queried
     * YamlMapping is not present.
     */
    @Test
    public void returnsNullOnMissingYamlMapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(
            map.yamlMapping("notthere"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(map.yamlMapping(
            new PlainStringScalar("notthere")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.yamlMapping(...) returns null if the queried
     * YamlMapping is not present.
     */
    @Test
    public void returnsNullOnMissingYamlMappingWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  some: other", 2));
        lines.add(new RtYamlLine("  complex: key", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlMapping(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlSequence(...) returns null if the queried
     * YamlSequence is not present.
     */
    @Test
    public void returnsNullOnMissingYamlSequenceWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  - someOther", 2));
        lines.add(new RtYamlLine("  - sequenceKey", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlSequence(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlSequence(String) returns null if the queried
     * key has a value that is a Scalar.
     */
    @Test
    public void returnsNullOnMisreadYamlSequence_Scalar() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(
            map.yamlSequence("first"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(map.yamlSequence(
            new PlainStringScalar("first")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.yamlSequence(String) returns null if the queried
     * key has a value that is a YamlMapping.
     */
    @Test
    public void returnsNullOnMisreadYamlSequence_Mapping() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second:", 1));
        lines.add(new RtYamlLine("  key1: map", 2));
        lines.add(new RtYamlLine("  key2: notSeq", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(
            map.yamlSequence("second"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(map.yamlSequence(
            new PlainStringScalar("second")), Matchers.nullValue()
        );
    }

    /**
     * ReadYamlMapping.yamlSequence(YamlNode) returns null if the queried
     * complex key has a Scalar value.
     */
    @Test
    public void returnsNullOnMisreadYamlSequence_ScalarWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": scalarValue", 4));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlSequence(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlSequence(YamlNode) returns null if the queried
     * complex key has a YamlMapping value.
     */
    @Test
    public void returnsNullOnMisreadYamlSequence_MappingWithMappingKey() {
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1")
            .add("complex2", "mapping2")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(":", 4));
        lines.add(new RtYamlLine("  some: map", 5));
        lines.add(new RtYamlLine("  not: seq", 6));
        lines.add(new RtYamlLine("second: something", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlSequence(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlSequence(YamlNode) returns null if the queried
     * complex key has a Scalar value.
     */
    @Test
    public void returnsNullOnMisreadYamlSequence_ScalarWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  - sequence", 2));
        lines.add(new RtYamlLine("  - key", 3));
        lines.add(new RtYamlLine(": scalarValue", 4));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlSequence(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping.yamlSequence(YamlNode) returns null if the queried
     * complex key has a YamlMapping value.
     */
    @Test
    public void returnsNullOnMisreadYamlSequence_MappingWithSequenceKey() {
        final YamlSequence key = new RtYamlSequenceBuilder()
            .add("sequence")
            .add("key")
            .build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  - sequence", 2));
        lines.add(new RtYamlLine("  - key", 3));
        lines.add(new RtYamlLine(":", 4));
        lines.add(new RtYamlLine("  some: map", 5));
        lines.add(new RtYamlLine("  not: seq", 6));
        lines.add(new RtYamlLine("second: something", 7));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(map.yamlSequence(key), Matchers.nullValue());
    }

    /**
     * ReadYamlMapping returns the correct value
     * when the key is wrapped in quotes.
     */
    @Test
    public void returnsValueOfStringKeyWithQuotes() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("\"a-key\": someValue", 0));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        MatcherAssert.assertThat(
            map.string("a-key"),
            Matchers.equalTo("someValue")
        );
    }

    /**
     * ReadYamlMapping returns the correct value for substring matching of keys.
     */
    @Test
    public void returnsValueOfStringKeys() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("aa: ", 0));
        lines.add(new RtYamlLine("  x: 1", 1));
        lines.add(new RtYamlLine("a: ", 2));
        lines.add(new RtYamlLine("  x: 2", 3));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        MatcherAssert.assertThat(
                map.value("a").asMapping().string("x"),
                Matchers.equalTo("2")
        );
    }

    /**
     * ReadYamlMapping returns the correct value for substring matching of keys.
     */
    @Test
    public void returnsValueOfStringKeysWithDashesAndSpaces() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("def:", 0));
        lines.add(new RtYamlLine("  -  aa:", 1));
        lines.add(new RtYamlLine("      x: 1", 2));
        lines.add(new RtYamlLine("  - a:", 3));
        lines.add(new RtYamlLine("      x: 2", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        Collection<YamlNode> values = map.value("def").asSequence().values();
        YamlNode firstValue = values.iterator().next();
        MatcherAssert.assertThat(
                firstValue.asMapping().value("aa").asMapping().string("x"),
                Matchers.equalTo("1")
        );
    }

    /**
     * ReadYamlMapping returns the correct value for empty maps and sequences.
     */
    @Test
    public void dontTurnEmptyMapsAndArraysIntoStrings() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("def: {}", 0));
        lines.add(new RtYamlLine("ghi: []", 1));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        YamlMapping actualMap = map.value("def").asMapping();
        YamlMapping expectedMap = Yaml.createYamlMappingBuilder().build();
        MatcherAssert.assertThat(
            actualMap,
            Matchers.equalTo(expectedMap)
        );
        MatcherAssert.assertThat(
            actualMap.comment().value(),
            Matchers.equalTo(expectedMap.comment().value())
        );
        YamlSequence actualSeq = map.value("ghi").asSequence();
        YamlSequence expectedSeq = Yaml.createYamlSequenceBuilder().build();
        MatcherAssert.assertThat(
            actualSeq,
            Matchers.equalTo(expectedSeq)
        );
        MatcherAssert.assertThat(
            actualSeq.comment().value(),
            Matchers.equalTo(expectedSeq.comment().value())
        );
    }

    /**
     * ReadYamlMapping can be created from another YamlMapping.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadFromAnotherFromYamlMapping()
        throws IOException {
        YamlMapping yaml = Yaml.createYamlMappingBuilder()
            .add("key1", "Some value?")
            .add("key2", "Some other value.")
            .build();
        YamlMapping copy = Yaml.createYamlInput(yaml.toString())
            .readYamlMapping();
        MatcherAssert.assertThat(copy.string("key1"), Matchers
            .equalTo("Some value?"));
        MatcherAssert.assertThat(copy.string("key2"), Matchers
            .equalTo("Some other value."));
    }

    /**
     * Reads mapping key properly if value has colon.
     */
    @Test
    public void shouldReadKeyProperlyIfValueContainsColon() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("key: value:with-colon", 0));
        ReadYamlMapping mapping = new ReadYamlMapping(new AllYamlLines(lines));

        MatcherAssert.assertThat(mapping.keys(), Matchers.hasSize(1));
        MatcherAssert.assertThat(
            mapping.keys().iterator().next().asScalar().value(),
            Matchers.equalTo("key")
        );
    }

    /**
     * ReadYamlMapping returns a literal string mapped to a string key,
     * by guessing the different lines' indentation (guessIndentation = true).
     */
    @Test
    public void returnsLiteralStringWithStringKeyGuessIndentation(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("key: | ", 0));
        lines.add(new RtYamlLine("  line1", 1));
        lines.add(new RtYamlLine("    line2", 2));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Collection<String> literalLines = map.literalBlockScalar("key");
        MatcherAssert.assertThat(
            literalLines.size(),
            Matchers.is(2)
        );
        final Iterator<String> linesIt = literalLines.iterator();
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("line1")
        );
        MatcherAssert.assertThat(
            linesIt.next(),
            Matchers.equalTo("  line2")
        );
    }

    /**
     * ReadYamlMapping can return the String mapped to a
     * String key even if the key contains quoted colons.
     */
    @Test
    public void returnsQuotedStringContainingColonsWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("'namespace:key': \"value:colon\"", 0));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final YamlNode key = map.keys().iterator().next();
        MatcherAssert.assertThat(key, Matchers.notNullValue());
        MatcherAssert.assertThat(
            key.asScalar().value(), Matchers.equalTo("namespace:key")
        );
        final YamlNode value = map.values().iterator().next();
        MatcherAssert.assertThat(value, Matchers.notNullValue());
        MatcherAssert.assertThat(
            value.asScalar().value(), Matchers.equalTo("value:colon")
        );
        MatcherAssert.assertThat(
            map.value("namespace:key").asScalar().value(),
            Matchers.equalTo("value:colon")
        );

    }
}
