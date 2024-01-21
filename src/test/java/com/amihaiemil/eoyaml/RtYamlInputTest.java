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

import java.io.FileReader;
import java.io.StringReader;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Unit tests for {@link RtYamlInput}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.1
 * @checkstyle ExecutableStatementCount (2000 lines)
 */
public final class RtYamlInputTest {

    /**
     * YamlMapping can be read without its comments.
     * @throws Exception If something goes wrong
     */
    @Test
    public void readsMappingWithoutComments() throws Exception {
        YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("architect", "mihai")
            .add("developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build()
            )
            .add("name", "eo-yaml")
            .build();
        YamlMapping read = new RtYamlInput(
            new FileReader(
                "src/test/resources/commentedMapping.yml"
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(
            read.string("name"),
            Matchers.equalTo(expected.string("name"))
        );
        MatcherAssert.assertThat(
            read.string("architect"),
            Matchers.equalTo(expected.string("architect"))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").size(),
            Matchers.equalTo(expected.yamlSequence("developers").size())
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(0),
            Matchers.equalTo(expected.yamlSequence("developers").string(0))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(1),
            Matchers.equalTo(expected.yamlSequence("developers").string(1))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(2),
            Matchers.equalTo(expected.yamlSequence("developers").string(2))
        );
        MatcherAssert.assertThat(
            read, Matchers.equalTo(expected)
        );
    }

    /**
     * RtYamlnput can read and indent a complex mapping.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsAndIndentsComplexMapping() throws Exception {
        YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("first", "value1")
            .add("second", "value2")
            .add("third",
                Yaml.createYamlSequenceBuilder()
                    .add("singleElementSequence")
                    .build()
            ).add(
                Yaml.createYamlSequenceBuilder()
                    .add("sequence")
                    .add("key")
                    .build(),
                "scalar"
            ).add(
                Yaml.createYamlMappingBuilder()
                    .add("map", "asKey")
                    .add("map1", "asKey2")
                    .build(),
                Yaml.createYamlMappingBuilder()
                    .add("someMapping",  "value")
                    .build()
            )
            .build();
        System.out.println(expected);
        YamlMapping actual = new RtYamlInput(
            new FileReader(
                "src/test/resources/indentedComplexMapping.yml"
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * RtYamlnput can read and indent a complex sequence.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsAndIndentsComplexSequence() throws Exception {
        final YamlSequence expected = Yaml.createYamlSequenceBuilder()
            .add("amihaiemil")
            .add("salikjan")
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("element1")
                    .add("element2")
                    .add("element3")
                    .build()
            )
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("rule1", "test")
                    .add("rule2", "test2")
                    .add("rule3", "test3")
                    .build()
            )
            .build();
        final YamlSequence actual = new RtYamlInput(
            new FileReader(
                "src/test/resources/complexSequence.yml"
            )
        ).readYamlSequence();
        MatcherAssert.assertThat(
            actual.toString(), Matchers.equalTo(expected.toString())
        );
    }

    /**
     * At the moment, when reading a mapping, we should ignore any YAML
     * Directive or Marker line, such as "%YAML 1.2" or "---" or "...".
     * @throws Exception if something is wrong.
     */
    @Test
    public void readsMappingWithoutDirectivesAndMarkers() throws Exception {
        final YamlMapping mapping = new RtYamlInput(
            new FileReader(
                "src/test/resources/mapping_ignore_directives.yml"
            )
        ).readYamlMapping();
        final Set<YamlNode> keys = mapping.keys();
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(2));
        MatcherAssert.assertThat(
            keys.contains(new PlainStringScalar("palette")),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat(
            keys.contains(new PlainStringScalar("workspace")),
            Matchers.is(Boolean.TRUE)
        );

        final YamlMapping palette = mapping.yamlMapping("palette");
        MatcherAssert.assertThat(palette.keys().size(), Matchers.is(12));
        MatcherAssert.assertThat(palette.values().size(), Matchers.is(12));
        MatcherAssert.assertThat(
            palette.string("light_gray"),
            Matchers.is("#484848")
        );
        MatcherAssert.assertThat(
            palette.string("magenta"),
            Matchers.is("rgb(255, 102, 255)")
        );

        final YamlMapping workspace = mapping.yamlMapping("workspace");
        MatcherAssert.assertThat(workspace.keys().size(), Matchers.is(3));
        MatcherAssert.assertThat(workspace.values().size(), Matchers.is(3));
        MatcherAssert.assertThat(
            workspace.string("background"),
            Matchers.is("gray")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("elements")
                .yamlMapping("devices")
                .yamlMapping("pins")
                .yamlSequence("active")
                .string(1),
            Matchers.is("hsl(163, 45%, 100%)")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("guides").string("grid"),
            Matchers.is("stroke rgba(255, 255, 255, 0.3)")
        );
    }

    /**
     * At the moment, when reading a sequence, we should ignore any YAML
     * Directive or Marker line, such as "%YAML 1.2" or "---" or "...".
     * @throws Exception if something is wrong.
     */
    @Test
    public void readsSequenceWithoutDirectivesAndMarkers() throws Exception {
        final YamlSequence sequence = new RtYamlInput(
            new FileReader(
                "src/test/resources/sequence_ignore_directives.yml"
            )
        ).readYamlSequence();
        final YamlMapping first = sequence.yamlMapping(0);
        MatcherAssert.assertThat(
            first.keys().size(), Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            first.values().size(), Matchers.equalTo(1)
        );

        final YamlMapping palette = first.yamlMapping("palette");
        MatcherAssert.assertThat(palette.keys().size(), Matchers.is(12));
        MatcherAssert.assertThat(palette.values().size(), Matchers.is(12));
        MatcherAssert.assertThat(
            palette.string("gray"),
            Matchers.is("#404040")
        );
        MatcherAssert.assertThat(
            palette.string("magenta"),
            Matchers.is("rgb(255, 102, 255)")
        );

        final YamlMapping second = sequence.yamlMapping(1);
        MatcherAssert.assertThat(
            second.keys().size(), Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            second.values().size(), Matchers.equalTo(1)
        );

        final YamlMapping workspace = second.yamlMapping("workspace");
        MatcherAssert.assertThat(workspace.keys().size(), Matchers.is(3));
        MatcherAssert.assertThat(workspace.values().size(), Matchers.is(3));
        MatcherAssert.assertThat(
            workspace.string("background"),
            Matchers.is("gray")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("elements")
                .yamlMapping("devices")
                .yamlMapping("pins")
                .yamlSequence("active")
                .string(1),
            Matchers.is("hsl(163, 45%, 100%)")
        );
        MatcherAssert.assertThat(
            workspace.yamlMapping("guides").string("grid"),
            Matchers.is("stroke rgba(255, 255, 255, 0.3)")
        );
    }

    /**
     * A YamlSequence in block style can be read.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsBlockSequence() throws Exception {
        YamlSequence expected = Yaml.createYamlSequenceBuilder()
            .add("apples")
            .add("pears")
            .add("peaches")
            .build();
        YamlSequence read = new RtYamlInput(
            new StringReader(
                new StringBuilder()
                    .append("---").append(System.lineSeparator())
                    .append("- apples").append(System.lineSeparator())
                    .append("- pears").append(System.lineSeparator())
                    .append("- peaches").append(System.lineSeparator())
                    .toString()
            )
        ).readYamlSequence();

        MatcherAssert.assertThat(read, Matchers.equalTo(expected));
    }

    /**
     * A Yaml stream of mappings can be read.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsStreamOfMappings() throws Exception {
        final YamlInput input = Yaml.createYamlInput(
            new FileReader(
                "src/test/resources/streamOfMappings.yml"
            )
        );
        final YamlStream read = input.readYamlStream();
        System.out.print(read);
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(this.readTestResource("streamOfMappings.yml"))
        );
    }

    /**
     * A Yaml stream of sequences can be read.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsStreamOfSequences() throws Exception {
        final YamlInput input = Yaml.createYamlInput(
            new FileReader(
                "src/test/resources/streamOfSequences.yml"
            )
        );
        final YamlStream read = input.readYamlStream();
        System.out.print(read);
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(this.readTestResource("streamOfSequences.yml"))
        );
    }

    /**
     * A stream of mixed YAML documents can be read.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsMixedStream() throws Exception {
        final YamlInput input = Yaml.createYamlInput(
            new FileReader(
                "src/test/resources/streamMixed.yml"
            )
        );
        final YamlStream read = input.readYamlStream();
//        System.out.print(read);
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(this.readTestResource("streamMixed.yml"))
        );
        System.out.println("-------------- VISITOR PRING ----------------");
        final YamlVisitor<String> visitor = new YamlPrintVisitor();
        String print = read.accept(visitor);
        System.out.println(print);
        System.out.println("-------------- END VISITOR PRING -------------");
    }

    /**
     * A stream of YAML Documents, when the first Start Marker is missing,
     * can be read.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsStreamWithoutFirstStartMarker() throws Exception {
        final YamlInput input = Yaml.createYamlInput(
            new FileReader(
                "src/test/resources/streamWithoutFirstStartMarker.yml"
            )
        );
        final YamlStream read = input.readYamlStream();
        final Collection<YamlNode> values = read.values();
        final Iterator<YamlNode> iterator = values.iterator();
        MatcherAssert.assertThat(values, Matchers.iterableWithSize(3));
        MatcherAssert.assertThat(
            ((YamlMapping) iterator.next()).string("architect"),
            Matchers.equalTo("mihai")
        );
        MatcherAssert.assertThat(
            ((YamlMapping) iterator.next()).string("architect"),
            Matchers.equalTo("vlad")
        );
        MatcherAssert.assertThat(
            ((YamlMapping) iterator.next()).string("architect"),
            Matchers.equalTo("felicia")
        );
    }

    /**
     * A stream of YAML Documents, when the first Start Marker is missing,
     * can be read. The file also contains comments.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsStreamWithoutFirstStartMarkerAndComments()
        throws Exception {
        final YamlInput input = Yaml.createYamlInput(
            new FileReader(
                "src/test/resources/streamWithComments.yml"
            )
        );
        final YamlStream read = input.readYamlStream();
        System.out.print(read);
        final Collection<YamlNode> values = read.values();
        final Iterator<YamlNode> iterator = values.iterator();
        MatcherAssert.assertThat(values, Matchers.iterableWithSize(3));
        MatcherAssert.assertThat(
            ((YamlMapping) iterator.next()).string("architect"),
            Matchers.equalTo("mihai")
        );
        MatcherAssert.assertThat(
            ((YamlMapping) iterator.next()).string("architect"),
            Matchers.equalTo("vlad")
        );
        MatcherAssert.assertThat(
            ((YamlMapping) iterator.next()).string("architect"),
            Matchers.equalTo("felicia")
        );
    }


    /**
     * RtYamlInput can read an empty mapping.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsEmptyMapping() throws Exception {
        final String newLine = System.lineSeparator();
        YamlMapping read = Yaml.createYamlInput(
            "---" + newLine + "..."
        ).readYamlMapping();
        MatcherAssert.assertThat(read.values(), Matchers.emptyIterable());
        MatcherAssert.assertThat(read.keys(), Matchers.emptyIterable());
        MatcherAssert.assertThat(read.toString(), Matchers.isEmptyString());

        read = Yaml.createYamlInput("").readYamlMapping();
        MatcherAssert.assertThat(read.values(), Matchers.emptyIterable());
        MatcherAssert.assertThat(read.keys(), Matchers.emptyIterable());
        MatcherAssert.assertThat(read.toString(), Matchers.isEmptyString());
    }

    /**
     * RtYamlInput can read an empty sequence.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsEmptySequence() throws Exception {
        final String newLine = System.lineSeparator();
        YamlSequence read = Yaml.createYamlInput(
            "---" + newLine + "..."
        ).readYamlSequence();
        MatcherAssert.assertThat(read.values(), Matchers.emptyIterable());
        MatcherAssert.assertThat(read.toString(), Matchers.isEmptyString());

        read = Yaml.createYamlInput("").readYamlSequence();
        MatcherAssert.assertThat(read.values(), Matchers.emptyIterable());
        MatcherAssert.assertThat(read.toString(), Matchers.isEmptyString());
    }

    /**
     * RtYamlInput can read an empty stream.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsEmptyStream() throws Exception {
        final String newLine = System.lineSeparator();
        YamlStream read = Yaml.createYamlInput(
            "---" + newLine + "..."
        ).readYamlStream();
        MatcherAssert.assertThat(read.values(), Matchers.emptyIterable());
        MatcherAssert.assertThat(
            read.toString(), Matchers.isEmptyString()
        );

        read = Yaml.createYamlInput("").readYamlStream();
        MatcherAssert.assertThat(read.values(), Matchers.emptyIterable());
        MatcherAssert.assertThat(
            read.toString(), Matchers.isEmptyString()
        );
    }

    /**
     * RtYamlInput can read empty scalars.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsEmptyScalars() throws Exception {
        MatcherAssert.assertThat(
            Yaml.createYamlInput("").readPlainScalar().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            Yaml.createYamlInput("").readFoldedBlockScalar().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            Yaml.createYamlInput("").readLiteralBlockScalar().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            Yaml.createYamlInput("---" + System.lineSeparator() + "...")
                .readPlainScalar().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            Yaml.createYamlInput("---" + System.lineSeparator() + " ...")
                .readFoldedBlockScalar().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            Yaml.createYamlInput("---" + System.lineSeparator() + "...")
                .readLiteralBlockScalar().value(),
            Matchers.isEmptyString()
        );
    }

    /**
     * RtYamlInput can read a plain scalar.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsPlainScalar() throws Exception {
        MatcherAssert.assertThat(
            Yaml.createYamlInput("scalar").readPlainScalar().value(),
            Matchers.equalTo("scalar")
        );
        MatcherAssert.assertThat(
            Yaml.createYamlInput(
                "---"
              + System.lineSeparator()
              + "scalar"
              + System.lineSeparator()
              + "..."
            ).readPlainScalar().value(),
            Matchers.equalTo("scalar")
        );
    }

    /**
     * RtYamlInput can read a folded block scalar.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsFoldedBlockScalar() throws Exception {
        MatcherAssert.assertThat(
            Yaml.createYamlInput(
                ">" + System.lineSeparator()
              + "some folded" + System.lineSeparator()
              + "block scalar"
            ).readFoldedBlockScalar().value(),
            Matchers.equalTo("some folded block scalar")
        );
        MatcherAssert.assertThat(
            Yaml.createYamlInput(
                "---" + System.lineSeparator()
              + ">" + System.lineSeparator()
              + "some folded" + System.lineSeparator()
              + "block scalar" + System.lineSeparator()
              + "..."
            ).readFoldedBlockScalar().value(),
            Matchers.equalTo("some folded block scalar")
        );
    }

    /**
     * RtYamlInput can read a folded block scalar.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsLiteralBlockScalar() throws Exception {
        MatcherAssert.assertThat(
            Yaml.createYamlInput(
                "|" + System.lineSeparator()
              + "  line1" + System.lineSeparator()
              + "  line2"
            ).readLiteralBlockScalar().value(),
            Matchers.equalTo(
                    "line1" + System.lineSeparator()
                            + "line2" + System.lineSeparator())
        );
    }

    /**
     * RtYamlInput can read a folded block scalar with document
     * start/end markers.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsLiteralBlockScalarWithDocumentStartEnd()
            throws Exception {
        MatcherAssert.assertThat(
            Yaml.createYamlInput(
                "---" + System.lineSeparator()
              + "|" + System.lineSeparator()
              + "line1" + System.lineSeparator()
              + "line2" + System.lineSeparator()
              + "..."
            ).readLiteralBlockScalar().value(),
            Matchers.equalTo(
                    "line1" + System.lineSeparator()
                            + "line2" + System.lineSeparator())
        );
    }

    /**
     * A YamlSequence containing mappings which start on the
     * same line as the dash can be read.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsSequenceWithDashMappings() throws Exception {
        final YamlMapping map = Yaml.createYamlInput(
            this.readTestResource("dashMappings.yml")
        ).readYamlMapping();
        MatcherAssert.assertThat(map.type(), Matchers.is(Node.MAPPING));
        final YamlSequence permissions = map.yamlSequence("permissions");
        MatcherAssert.assertThat(
            permissions,
            Matchers.iterableWithSize(4)
        );
        MatcherAssert.assertThat(
            permissions
                .yamlMapping(0)
                .yamlSequence("john"),
            Matchers.iterableWithSize(2)
        );
        MatcherAssert.assertThat(
            permissions
                .yamlMapping(1)
                .yamlSequence("jane"),
            Matchers.iterableWithSize(2)
        );
        MatcherAssert.assertThat(
            permissions
                .yamlMapping(2)
                .string("andrew"),
            Matchers.equalTo("none")
        );
        MatcherAssert.assertThat(
            permissions
                .yamlMapping(3)
                .string("andreea"),
            Matchers.equalTo("none")
        );
    }

    /**
     * Some escaped scalars can be read from a sequence.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsEscapedScalarsFromSequence() throws Exception {
        final YamlMapping map = Yaml.createYamlInput(
            this.readTestResource("escapedScalarsInSequence.yml")
        ).readYamlMapping();
        System.out.println(map);
        final YamlSequence scalars = map.yamlSequence("scalars");
        MatcherAssert.assertThat(
            scalars.string(0),
            Matchers.equalTo(
                "escaped: scalar, looks like mapping"
            )
        );
        MatcherAssert.assertThat(
            scalars.string(1),
            Matchers.equalTo(
                "{escaped: scalar, like: flow}"
            )
        );
        MatcherAssert.assertThat(
            scalars.string(2),
            Matchers.equalTo(
                "- escapedScalar3"
            )
        );
        MatcherAssert.assertThat(
            scalars.string(3),
            Matchers.equalTo(
                "[scalar, like, flow, sequence]"
            )
        );
    }

    /**
     * Compare two RtYamlLines by trimmed value.
     */
    @Test
    public void comparesTo() {
        RtYamlLine java = new RtYamlLine("Java", 1);
        RtYamlLine scala = new RtYamlLine("Scala", 1);
        MatcherAssert.assertThat(java.compareTo(scala), Matchers.lessThan(0));
        MatcherAssert.assertThat(java.compareTo(java), Matchers.is(0));
    }

    /**
     * Do a round-trip test on the minimal quoted-key sample file.
     *
     * @throws IOException When there's a problem reading the sample files.
     */
    @Test
    public void quotedKeysMinTest() throws IOException {
        final String filename = "quotedKeysMin.yml";
        final String fileContents = readTestResource(filename).trim();

        final YamlMapping read = new RtYamlInput(
                new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        MatcherAssert.assertThat(read.type(), Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                read.asMapping().keys().size(),
                Matchers.equalTo(1));

        final YamlNode topLevelMapping = read.asMapping().value("a_mapping");
        MatcherAssert.assertThat(
                topLevelMapping.type(),
                Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                topLevelMapping.asMapping().keys().size(),
                Matchers.equalTo(1));

        final String pretty = read.toString().trim();

        MatcherAssert.assertThat(pretty, Matchers.equalTo(fileContents));
    }


    /**
     * Do a round-trip test on the maximal quoted-key sample file.
     *
     * @throws IOException When there's a problem reading the sample files.
     */
    @Test
    public void quotedKeysMaxTest() throws IOException {
        final String filename = "quotedKeysMax.yml";
        final String fileContents = readTestResource(filename).trim();

        final YamlMapping read = new RtYamlInput(
            new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        MatcherAssert.assertThat(read.type(), Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                read.asMapping().keys().size(),
                Matchers.equalTo(1));

        final YamlNode topLevelMapping = read.asMapping().value("a_mapping");
        MatcherAssert.assertThat(
                topLevelMapping.type(),
                Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                topLevelMapping.asMapping().keys().size(),
                Matchers.equalTo(6));

        final String pretty = read.toString().trim();

        MatcherAssert.assertThat(pretty, Matchers.equalTo(fileContents));
    }

    /**
     * Corner case when key:value is on the same with sequence marker.
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/447">#447</a>
     * @throws IOException If something is wrong.
     */
    @Test
    public void shouldReadSequenceWhenFirstKeyIsOnTheSameLineFirstCase()
        throws IOException {
        final String fileName = "src/test/resources/issue_447_bug_mapping"
            + "_case_1.yml";
        final YamlMapping root = Yaml.createYamlInput(new File(fileName))
            .readYamlMapping();
        final YamlSequence variables = root.yamlSequence("root");
        MatcherAssert.assertThat(variables, Matchers.iterableWithSize(3));
    }

    /**
     * Corner case when key:value is on the same with sequence marker.
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/447">#447</a>
     * @throws IOException If something is wrong.
     */
    @Test
    public void shouldReadSequenceWhenFirstKeyIsOnTheSameLineSecondCase()
        throws IOException {
        final String fileName = "src/test/resources/issue_447_bug_mapping"
            + "_case_2.yml";
        final YamlMapping root = Yaml.createYamlInput(new File(fileName))
            .readYamlMapping();
        final YamlSequence variables = root.yamlSequence("root");
        MatcherAssert.assertThat(variables, Matchers.iterableWithSize(3));
        MatcherAssert.assertThat(
            variables.yamlMapping(0).string("key"),
            Matchers.equalTo("key1")
        );
        MatcherAssert.assertThat(
            variables.yamlMapping(0).string("value"),
            Matchers.equalTo("value1")
        );
        MatcherAssert.assertThat(
            variables.yamlMapping(1).string("key"),
            Matchers.equalTo("key2")
        );
        MatcherAssert.assertThat(
            variables.yamlMapping(1).string("value"),
            Matchers.equalTo("value2")
        );
        MatcherAssert.assertThat(
            variables.yamlMapping(2).string("key"),
            Matchers.equalTo("key3")
        );
        MatcherAssert.assertThat(
            variables.yamlMapping(2).string("value"),
            Matchers.equalTo("value3")
        );
    }

    /**
     * Corner case when key:value is on the same with sequence marker.
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/447">#447</a>
     * @throws IOException If something is wrong.
     */
    @Test
    public void shouldReadSequenceWhenFirstKeyIsOnTheSameLineThirdCase()
        throws IOException {
        final String fileName = "src/test/resources/issue_447_bug_mapping"
            + "_case_3.yml";
        final YamlMapping root = Yaml.createYamlInput(new File(fileName))
            .readYamlMapping();
        final YamlSequence variables = root.yamlSequence("root");
        MatcherAssert.assertThat(variables, Matchers.iterableWithSize(3));
    }

    /**
     * Corner case when key:value is on the same with sequence marker.
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/447">#447</a>
     * and based on
     * <a href="https://github.com/decorators-squad/eo-yaml/pull/416">PR</a>
     * @throws IOException If something is wrong.
     */
    @Test
    public void shouldReadSequenceWhenFirstKeyIsOnTheSameLineFourthCase()
        throws IOException {
        final String fileName = "src/test/resources/issue_447_bug_mapping"
            + "_case_4.yml";
        final YamlMapping root = Yaml.createYamlInput(new File(fileName))
            .readYamlMapping();
        MatcherAssert.assertThat(
            root,
            Matchers.equalTo(
                Yaml.createYamlMappingBuilder()
                    .add("key", "value")
                    .add("seqMap", Yaml.createYamlSequenceBuilder()
                        .add(Yaml.createYamlMappingBuilder()
                            .add("alfa", "b")
                            .build())
                        .add(Yaml.createYamlMappingBuilder()
                            .add("this", "isSkiped")
                            .build())
                        .build())
                    .add("key2", "value")
                    .build()
            )
        );
    }

    /**
     * Corner case when key:value is on the same line as the sequence marker
     * and key contains dashes (which is the sequence marker itself).
     *  @throws IOException If something is wrong.
     */
    @Test
    public void shouldReadSequenceWhenFirstKeyWithDashesIsOnTheSameLine()
        throws IOException {
        final String fileName = "src/test/resources/"
            + "compactSequenceWithDashedKey.yml";
        final YamlMapping root = Yaml.createYamlInput(new File(fileName))
            .readYamlMapping();

        final YamlSequence sequence = root.yamlSequence("root");
        MatcherAssert.assertThat(sequence, Matchers.iterableWithSize(2));

        MatcherAssert.assertThat(
            sequence.yamlMapping(0).string("a-a-a"),
            Matchers.equalTo("1")
        );
    }

    /**
     * Do a round-trip test on the bracketed-key sample file.
     *
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/494">#494</a>
     * based on
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/495">PR</a>
     *
     * @throws IOException When there's a problem reading the sample files.
     */
    @Test
    public void supportsBracketedNotation() throws IOException {
        final String filename = "issue_494_bracketed_keys.yml";
        final String fileContents = readTestResource(filename).trim();

        final YamlMapping read = new RtYamlInput(
            new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        MatcherAssert.assertThat(read.type(), Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
            read.asMapping().keys().size(),
            Matchers.equalTo(1));

        final YamlNode topLevelMapping = read.asMapping().value("a_mapping");
        MatcherAssert.assertThat(
            topLevelMapping.type(),
            Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
            topLevelMapping.asMapping().keys().size(),
            Matchers.equalTo(1));

        final String pretty = read.toString().trim();

        MatcherAssert.assertThat(pretty, Matchers.equalTo(fileContents));
    }

    /**
     * Do a round-trip test on sample file containing a scalar using Spring
     * property reference syntax.
     *
     * Unit test for
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/518">#518</a>
     *
     * @throws IOException When there's a problem reading the sample files.
     */
    @Test
    public void supportsSpringPropertyRef() throws IOException {
        final String filename = "issue_518_spring_property_ref.yml";
        final String fileContents = readTestResource(filename).trim();

        final YamlMapping read = new RtYamlInput(
                new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        MatcherAssert.assertThat(read.type(), Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                read.asMapping().keys().size(),
                Matchers.equalTo(1));

        final YamlNode topLevelMapping = read.asMapping().value("a_mapping");
        MatcherAssert.assertThat(
                topLevelMapping.type(),
                Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                topLevelMapping.asMapping().keys().size(),
                Matchers.equalTo(1));

        final String pretty = read.toString().trim();

        MatcherAssert.assertThat(pretty, Matchers.equalTo(fileContents));
    }

    /**
     * Unit test for issue 517.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadKeysProperly() throws IOException {
        final String filename = "issue_517_values_with_colons.yml";

        final YamlMapping read = new RtYamlInput(
                new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        MatcherAssert.assertThat(read.type(), Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                read.asMapping().keys(),
                Matchers.hasSize(2));

        final YamlNode topLevelMapping = read.asMapping().value("a_mapping");
        MatcherAssert.assertThat(
                topLevelMapping.type(),
                Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                topLevelMapping.asMapping().value("a_scalar").type(),
                Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(
                topLevelMapping.asMapping().value("a_scalar")
                    .asScalar().value(),
                Matchers.equalTo("value:with-colon"));

        YamlNode topLevelSequence = read.asMapping().value("a_sequence");
        MatcherAssert.assertThat(
                topLevelSequence.type(),
                Matchers.equalTo(Node.SEQUENCE));
        MatcherAssert.assertThat(topLevelSequence.asSequence().values(),
                                 Matchers.hasSize(1));
        YamlNode sequenceItem = topLevelSequence.asSequence().values()
            .iterator().next();
        MatcherAssert.assertThat(sequenceItem.type(),
                                 Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(sequenceItem.asScalar().value(),
                                 Matchers.equalTo("value:with-colon"));
    }

    /**
     * Unit test to ensure empty block scalars are read properly.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadEmptyBlockScalarProperly() throws IOException {
        final String filename = "emptyLiteralScalar.yml";

        final YamlMapping read = new RtYamlInput(
                new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        MatcherAssert.assertThat(read.type(), Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
                read.asMapping().keys(),
                Matchers.hasSize(3));

        final YamlNode emptyLiteralScalar = read.asMapping()
            .value("empty_literal_scalar");
        MatcherAssert.assertThat(
                emptyLiteralScalar.type(),
                Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(
                emptyLiteralScalar.asScalar().value(),
                Matchers.equalTo(""));

        final YamlNode emptyFoldedScalar = read.asMapping()
            .value("empty_folded_scalar");
        MatcherAssert.assertThat(
                emptyFoldedScalar.type(),
                Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(
                emptyFoldedScalar.asScalar().value(),
                Matchers.equalTo(""));

        YamlNode topLevelSequence = read.asMapping().value("a_sequence");
        MatcherAssert.assertThat(
                topLevelSequence.type(),
                Matchers.equalTo(Node.SEQUENCE));
        MatcherAssert.assertThat(topLevelSequence.asSequence().values(),
                                 Matchers.hasSize(1));
        YamlNode sequenceItem = topLevelSequence.asSequence().values()
                                                .iterator().next();
        MatcherAssert.assertThat(sequenceItem.type(),
                                 Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(sequenceItem.asScalar().value(),
                                 Matchers.equalTo("test"));
    }

    /**
     * Unit test for issue 525 (empty scalars part).
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadEmptyScalarsProperly() throws IOException {
        final String filename = "issue_525_emptyEntries.yml";

        final YamlMapping read = new RtYamlInput(
            new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();
        MatcherAssert.assertThat(read.type(), Matchers.equalTo(Node.MAPPING));
        MatcherAssert.assertThat(
            read.keys(),
            Matchers.hasSize(1)
        );

        final YamlSequence aSequence = read.yamlSequence("a_sequence");
        MatcherAssert.assertThat(
            aSequence.type(),
            Matchers.equalTo(Node.SEQUENCE)
        );
        MatcherAssert.assertThat(
            aSequence.size(),
            Matchers.equalTo(3)
        );

        final Iterator<YamlNode> iterator = aSequence.iterator();

        final YamlNode firstItem = iterator.next();
        MatcherAssert.assertThat(
            firstItem.type(),
            Matchers.equalTo(Node.MAPPING)
        );
        YamlNode aMapping = firstItem.asMapping().value("a_mapping");
        MatcherAssert.assertThat(
                aMapping.type(),
                Matchers.equalTo(Node.MAPPING)
        );
        final YamlNode emptyBlockScalar = aMapping.asMapping()
            .value("empty_block_scalar");
        MatcherAssert.assertThat(
            emptyBlockScalar.type(),
            Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(
            emptyBlockScalar.asScalar().value(),
            Matchers.isEmptyString());
        final YamlNode emptyFoldedScalar = aMapping.asMapping()
            .value("empty_folded_scalar");
        MatcherAssert.assertThat(
            emptyFoldedScalar.type(),
            Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(
            emptyFoldedScalar.asScalar().value(),
            Matchers.isEmptyString());
        final YamlNode normalScalar = aMapping.asMapping()
            .value("normal_scalar");
        MatcherAssert.assertThat(
            normalScalar.type(),
            Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(
            normalScalar.asScalar().value(),
            Matchers.equalTo("example"));

        final YamlNode secondItem = iterator.next();
        MatcherAssert.assertThat(
                secondItem.type(),
                Matchers.equalTo(Node.MAPPING));
        final YamlNode value = secondItem.asMapping()
            .value("key");
        MatcherAssert.assertThat(value.type(), Matchers.equalTo(Node.SCALAR));
        MatcherAssert.assertThat(
            value.asScalar().value(),
            Matchers.equalTo("value"));

        final YamlNode thirdItem = iterator.next();
        MatcherAssert.assertThat(
                thirdItem.type(),
                Matchers.equalTo(Node.MAPPING));
        YamlNode anotherMapping = thirdItem.asMapping()
            .value("another_mapping");
        MatcherAssert.assertThat(
                anotherMapping.type(),
                Matchers.equalTo(Node.MAPPING));
        final YamlNode emptyScalarApp = anotherMapping.asMapping()
            .value("empty_scalar_app");
        MatcherAssert.assertThat(
            emptyScalarApp.type(),
            Matchers.equalTo(Node.SCALAR)
        );
        MatcherAssert.assertThat(
            emptyScalarApp.asScalar().value(),
            Matchers.isEmptyString());
        final YamlNode emptyScalarQuotes = anotherMapping.asMapping()
            .value("empty_scalar_quotes");
        MatcherAssert.assertThat(
            emptyScalarQuotes.type(),
            Matchers.equalTo(Node.SCALAR)
        );
        MatcherAssert.assertThat(
            emptyScalarQuotes.asScalar().value(),
            Matchers.isEmptyString());
        final YamlNode nullScalar = anotherMapping.asMapping()
            .value("null_scalar");
        MatcherAssert.assertThat(
            nullScalar.type(),
            Matchers.equalTo(Node.SCALAR)
        );
        MatcherAssert.assertThat(
            nullScalar.asScalar().value(),
            Matchers.nullValue());
    }

    /**
     * Unit test for issue 497 (ghost scalar comment).
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadScalarCommentsProperly() throws IOException {
        final String filename = "issue_497_ghost_comment.yml";

        final YamlMapping read = new RtYamlInput(
            new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(this.readTestResource(filename))
        );
    }

    /**
     * Unit test for issue 546.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldIterateKeysWhenMappingStartsAtDash()
        throws IOException {
        final String filename = "issue_546_mapping_starts_at_dash.yml";

        final YamlMapping read = new RtYamlInput(
            new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        final YamlMapping holders = read.yamlSequence("holders")
            .iterator().next().asMapping();
        MatcherAssert.assertThat(
            holders.yamlSequence("array").string(0),
            Matchers.equalTo("arr3")
        );
        MatcherAssert.assertThat(
            holders.string("value"),
            Matchers.equalTo("test1")
        );

        final YamlMapping holdersTwo = read.yamlSequence("holders2")
            .iterator().next().asMapping();
        MatcherAssert.assertThat(
            holdersTwo.string("value2"),
            Matchers.equalTo("test2")
        );
        MatcherAssert.assertThat(
            holdersTwo.yamlSequence("array2").string(0),
            Matchers.equalTo("arr4")
        );
    }

    /**
     * Unit test for issue 546, second example with accidentally interpolated
     * mapping in a sequence..
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldIterateKeysWhenMappingStartsAtDashNoInterpolation()
        throws IOException {
        final String filename = "issue_546_mapping_starts_at_dash_2.yml";

        final YamlMapping read = new RtYamlInput(
            new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        System.out.println(read);

        final YamlSequence holders = read.yamlSequence("holders");
        MatcherAssert.assertThat(holders.size(), Matchers.equalTo(2));

        final YamlMapping first = holders.yamlMapping(0);
        MatcherAssert.assertThat(first.keys().size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(
            first.yamlSequence("array").string(0),
            Matchers.equalTo("arr1")
        );

        final YamlMapping second = holders.yamlMapping(1);
        MatcherAssert.assertThat(second.keys().size(), Matchers.equalTo(2));
        MatcherAssert.assertThat(
            second.yamlSequence("array2").string(0),
            Matchers.equalTo("arr2")
        );
        MatcherAssert.assertThat(
            second.string("value"),
            Matchers.equalTo("test1")
        );

    }

    /**
     * Unit test for issue 547.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadEscapedScalarsProperly()
        throws IOException {
        final String filename = "issue_547_escaped_special_chars.yml";

        final YamlMapping read = new RtYamlInput(
            new FileReader("src/test/resources/" + filename)
        ).readYamlMapping();

        System.out.println(read);

        final YamlMapping filter = read.yamlMapping("filter");
        MatcherAssert.assertThat(
            filter.string("input"),
            Matchers.equalTo("${ {vegetables: .vegetables} }")
        );
        //@checkstyle LineLength (5 lines)
        MatcherAssert.assertThat(
            filter.string("output"),
            Matchers.equalTo(
                "${ {vegetables: [.vegetables[] | select(.veggieLike == true)]} }"
            )
        );
    }

    /**
     * Unit test for issue 542.
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/542">#542</a>
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadSequenceOfFoldedBlockScalars() throws IOException {
        final String filename = "issue_542_seq_folded_blocks.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(Paths.get("src/test/resources/" + filename))
        ).readYamlMapping();
        MatcherAssert.assertThat(
            mapping.yamlSequence("list").foldedBlockScalar(0),
            Matchers.equalTo(
                "    one" + System.lineSeparator()
                + "    two" + System.lineSeparator())
        );
        MatcherAssert.assertThat(
            mapping.yamlSequence("list").foldedBlockScalar(1),
            Matchers.equalTo(
                "    three" + System.lineSeparator()
                + "    four" + System.lineSeparator())
        );
        MatcherAssert.assertThat(
            mapping.yamlSequence("list").foldedBlockScalar(2),
            Matchers.equalTo(
                "    five" + System.lineSeparator()
                + "    six" + System.lineSeparator()
                + "    seven" + System.lineSeparator()
            )
        );
        MatcherAssert.assertThat(
            mapping.toString(),
            Matchers.equalTo(
                Yaml.createYamlMappingBuilder()
                    .add(
                        "list",
                        Yaml.createYamlSequenceBuilder()
                            .add(
                                Yaml.createYamlScalarBuilder()
                                    .addLine("one")
                                    .addLine("two")
                                    .buildFoldedBlockScalar()
                            )
                            .add(
                                Yaml.createYamlScalarBuilder()
                                    .addLine("three")
                                    .addLine("four")
                                    .buildFoldedBlockScalar()
                            )
                            .add(
                                Yaml.createYamlScalarBuilder()
                                    .addLine("five")
                                    .addLine("six")
                                    .addLine("seven")
                                    .buildFoldedBlockScalar()
                            )
                        .build()
                    ).build().toString()
            )
        );
    }

    /**
     * Unit test for issue 542.
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/542">#542</a>
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadMappingWithMixOfFoldedAndLiteralsScalars()
        throws IOException {
        final String filename = "issue_542_seq_folded_blocks_mix.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(Paths.get("src/test/resources/" + filename))
        ).readYamlMapping();
        MatcherAssert.assertThat(
            mapping.toString(),
            Matchers.equalTo(
                Yaml.createYamlMappingBuilder()
                    .add(
                        "list",
                        Yaml.createYamlSequenceBuilder()
                            .add(
                                Yaml.createYamlScalarBuilder()
                                    .addLine("one")
                                    .addLine("two")
                                    .buildFoldedBlockScalar()
                            )
                            .add(
                                Yaml.createYamlScalarBuilder()
                                    .addLine("three")
                                    .addLine("four")
                                    .buildFoldedBlockScalar()
                            )
                            .add(
                                Yaml.createYamlScalarBuilder()
                                    .addLine("five")
                                    .addLine("six")
                                    .addLine("seven")
                                    .buildFoldedBlockScalar()
                            )
                            .build()
                    ).add(
                        "other",
                        Yaml.createYamlScalarBuilder()
                            .addLine("one")
                            .addLine("two")
                            .buildFoldedBlockScalar()

                    ).add(
                        "another",
                        Yaml.createYamlScalarBuilder()
                            .addLine("three")
                            .addLine("four")
                            .buildFoldedBlockScalar()

                    ).add(
                        "anotherBlock",
                        Yaml.createYamlScalarBuilder()
                            .addLine("five")
                            .addLine("six")
                            .addLine("seven")
                            .buildLiteralBlockScalar()

                    ).build().toString()
            )
        );
    }

    /**
     * Unit test for issue 556.
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/556">#556</a>
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldIterateKeysOfFoldedMappingInSequence()
        throws IOException {
        final String filename = "issue_556_seq_folded_blocks.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(Paths.get("src/test/resources/" + filename))
        ).readYamlMapping();
//        System.out.println(mapping);
        System.out.println("-------------- VISITOR PRING ----------------");
        final YamlVisitor<String> visitor = new YamlPrintVisitor();
        String print = mapping.accept(visitor);
        System.out.println(print);
        System.out.println("-------------- END VISITOR PRING -------------");

        YamlSequence states = mapping.value("states").asSequence();
        MatcherAssert.assertThat(states.size(), Matchers.equalTo(1));
        YamlMapping state = states.iterator().next().asMapping();

        MatcherAssert.assertThat(
            state.string("name"), Matchers.equalTo("ConsumeReading")
        );
        MatcherAssert.assertThat(
            state.string("type"), Matchers.equalTo("event")
        );
        MatcherAssert.assertThat(
            state.string("end"), Matchers.equalTo("true")
        );
        YamlSequence onEvents = state.value("onEvents").asSequence();
        MatcherAssert.assertThat(onEvents.size(), Matchers.equalTo(1));
        YamlMapping event = onEvents.iterator().next().asMapping();

        MatcherAssert.assertThat(event.keys().size(), Matchers.equalTo(3));

        YamlMapping eventDataFilter = event.value("eventDataFilter")
            .asMapping();
        MatcherAssert.assertThat(
            eventDataFilter.string("toStateData"),
            Matchers.equalTo("${ .readings }")
        );
        YamlSequence actions = event.value("actions").asSequence();
        MatcherAssert.assertThat(actions.size(), Matchers.equalTo(1));
        YamlMapping action = actions.iterator().next().asMapping();
        YamlMapping functionRef = action.value("functionRef").asMapping();
        MatcherAssert.assertThat(
            functionRef.string("refName"),
            Matchers.equalTo("LogReading")
        );

        YamlSequence eventRefs = event.value("eventRefs").asSequence();
        MatcherAssert.assertThat(eventRefs.size(), Matchers.equalTo(2));

        String firstEventRef = eventRefs.string(0);
        MatcherAssert.assertThat(
            firstEventRef,
            Matchers.equalTo("TemperatureEvent")
        );

        String secondEventRef = eventRefs.string(1);
        MatcherAssert.assertThat(
            secondEventRef,
            Matchers.equalTo("HumidityEvent")
        );
    }

    /**
     * Unit test for example 2.3 https://yaml.org/spec/1.2.2/#21-collections.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void mappingScalarstoSequences() throws IOException {
        final String filename = "mappingScalarstoSequences.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(Paths.get("src/test/resources/" + filename))
        ).readYamlMapping();

        MatcherAssert.assertThat(mapping.keys().size(), Matchers.equalTo(2));
        YamlSequence american = mapping.value("american").asSequence();
        YamlSequence national = mapping.value("national").asSequence();
        MatcherAssert.assertThat(american.size(), Matchers.equalTo(3));
        MatcherAssert.assertThat(national.size(), Matchers.equalTo(3));
    }

    /**
     * More complex structure for example 2.3.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void mappingScalarstoSequencesComplexCase() throws IOException {
        final String filename = "mappingScalarstoSequencesComplexCase.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(
                Paths.get("src/test/resources/" + filename)
            )
        ).readYamlMapping();

        MatcherAssert.assertThat(mapping.keys().size(), Matchers.equalTo(6));
        YamlSequence states = mapping.value("states").asSequence();
        MatcherAssert.assertThat(states.size(), Matchers.equalTo(3));

        YamlMapping state = states.yamlMapping(0);
        MatcherAssert.assertThat(
            state.string("name"), Matchers.equalTo("Dispatch Courier")
        );
        MatcherAssert.assertThat(
            state.string("type"), Matchers.equalTo("operation")
        );
        YamlSequence actions = state.value("actions").asSequence();
        MatcherAssert.assertThat(actions.size(), Matchers.equalTo(1));
        YamlMapping action = actions.iterator().next().asMapping();
        MatcherAssert.assertThat(action.keys().size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(
            action.string("functionRef"),
            Matchers.equalTo("Dispatch Courrier Function")
        );
        MatcherAssert.assertThat(
            state.string("transition"),
            Matchers.equalTo("Wait for Order Pickup")
        );

        state = states.yamlMapping(1);
        MatcherAssert.assertThat(state.keys().size(), Matchers.equalTo(4));
        MatcherAssert.assertThat(
            state.string("name"),
            Matchers.equalTo("Wait for Order Pickup")
        );
        MatcherAssert.assertThat(
            state.string("type"),
            Matchers.equalTo("event")
        );
        YamlSequence onEvents = state.yamlSequence("onEvents");
        MatcherAssert.assertThat(onEvents.size(), Matchers.equalTo(1));
        YamlMapping onEvent = onEvents.iterator().next().asMapping();
        YamlSequence eventRefs = onEvent.value("eventRefs").asSequence();
        MatcherAssert.assertThat(eventRefs.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(
            eventRefs.iterator().next().asScalar().value(),
            Matchers.equalTo("Order Picked Up Event")
        );
        YamlMapping eventDataFilter = onEvent.value("eventDataFilter")
            .asMapping();
        MatcherAssert.assertThat(
            eventDataFilter.keys().size(), Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            eventDataFilter.string("data"),
            Matchers.equalTo("${ .data.status }")
        );
        MatcherAssert.assertThat(
            eventDataFilter.string("toStateData"),
            Matchers.equalTo("${ .status }")
        );
        YamlMapping actionsTwo = onEvent.value("actions").asSequence()
            .iterator().next().asMapping();
        MatcherAssert.assertThat(
            actionsTwo.string("functionRef"),
            Matchers.equalTo("Deliver Order Function")
        );
        MatcherAssert.assertThat(
            state.string("transition"),
            Matchers.equalTo("Wait for Delivery Confirmation")
        );

        state = states.yamlMapping(2);
        MatcherAssert.assertThat(
            state.string("name"),
            Matchers.equalTo("Wait for Delivery Confirmation")
        );
        MatcherAssert.assertThat(
            state.string("type"),
            Matchers.equalTo("event")
        );
        YamlSequence onEventsTwo = state.yamlSequence("onEvents");
        MatcherAssert.assertThat(onEventsTwo.size(), Matchers.equalTo(1));
        YamlMapping onEventTwo = onEventsTwo.iterator().next().asMapping();
        YamlSequence eventRefsTwo = onEventTwo.value("eventRefs").asSequence();
        MatcherAssert.assertThat(eventRefsTwo.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(
            eventRefsTwo.iterator().next().asScalar().value(),
            Matchers.equalTo("Order Delievered Event")
        );
        YamlMapping eventDataFilterTwo = onEventTwo.value("eventDataFilter")
            .asMapping();
        MatcherAssert.assertThat(
            eventDataFilterTwo.keys().size(),
            Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            eventDataFilterTwo.string("data"),
            Matchers.equalTo("${ .data.status }")
        );
        MatcherAssert.assertThat(
            eventDataFilterTwo.string("toStateData"),
            Matchers.equalTo("${ .status }")
        );
        MatcherAssert.assertThat(
            state.string("end"), Matchers.equalTo("true")
        );
    }

    /**
     * Mapping with unescaped values that have special chars '?' and '#'.
     * @throws IOException If something goes wrong.
     * @checkstyle LineLength (20 lines)
     */
    @Test
    public void questionMarkAtTheEndOfStatementAndHash() throws IOException {
        final String filename = "questionMarkAtTheEndOfStatementAndHashInString.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(
                Paths.get("src/test/resources/" + filename)
            )
        ).readYamlMapping();

        MatcherAssert.assertThat(mapping.keys().size(), Matchers.equalTo(4));
        YamlMapping start = mapping.value("start").asMapping();
        MatcherAssert.assertThat(
            start.keys().size(), Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            start.string("stateName"),
            Matchers.equalTo("CheckInbox")
        );
        MatcherAssert.assertThat(
            start.yamlMapping("schedule").keys().size(),
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            start.yamlMapping("schedule").string("cron"),
            Matchers.equalTo("0 0/15 * * * ?")
        );

        YamlSequence functions = mapping.value("functions").asSequence();
        MatcherAssert.assertThat(functions.size(), Matchers.equalTo(2));

        YamlMapping functionOne = functions.yamlMapping(0);
        MatcherAssert.assertThat(
            functionOne.keys().size(),
            Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            functionOne.string("name"),
            Matchers.equalTo("checkInboxFunction")
        );
        MatcherAssert.assertThat(
            functionOne.string("operation"),
            Matchers.equalTo(
                "http://myapis.org/inboxapi.json#checkNewMessages"
            )
        );


        YamlMapping functionTwo = functions.yamlMapping(1);
        MatcherAssert.assertThat(
            functionTwo.keys().size(), Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            functionTwo.string("name"),
            Matchers.equalTo("sendTextFunction")
        );
        MatcherAssert.assertThat(
            functionTwo.string("operation"),
            Matchers.equalTo("http://myapis.org/inboxapi.json#sendText")
        );

    }

    /**
     * Unit test for Issue 559. Original ticket indentation.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadListOfMapProperlyOriginalIndentation()
        throws IOException {
        final String filename = "issue_559_list_of_map_ticket_indentation.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(Paths.get("src/test/resources/" + filename))
        ).readYamlMapping();
        System.out.println(mapping);

        final YamlMapping chat = mapping.yamlMapping("Chat");
        MatcherAssert.assertThat(
            chat.keys(),
            Matchers.iterableWithSize(3)
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements").size(),
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements")
                .yamlMapping(0)
                .string("Sound"),
            Matchers.equalTo("CAT_HISS")
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements")
                .yamlMapping(0)
                .string("Permission"),
            Matchers.equalTo("none")
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements")
                .yamlMapping(0)
                .yamlSequence("list")
                .string(0),
            Matchers.equalTo("a. string")
        );
        MatcherAssert.assertThat(
            chat.string("list"),
            Matchers.equalTo("not actually a list")
        );
        MatcherAssert.assertThat(
            chat.yamlMapping("Broadcast")
                .string("enabled"),
            Matchers.equalTo("true")
        );
    }

    /**
     * Unit test for Issue 559. Properly Indented.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldReadListOfMapProperly()
        throws IOException {
        final String filename = "issue_559_list_of_map.yml";
        final YamlMapping mapping = new RtYamlInput(
            Files.newBufferedReader(Paths.get("src/test/resources/" + filename))
        ).readYamlMapping();
        System.out.println(mapping);

        final YamlMapping chat = mapping.yamlMapping("Chat");
        MatcherAssert.assertThat(
            chat.keys(),
            Matchers.iterableWithSize(3)
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements").size(),
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements")
                .yamlMapping(0)
                .string("Sound"),
            Matchers.equalTo("CAT_HISS")
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements")
                .yamlMapping(0)
                .string("Permission"),
            Matchers.equalTo("none")
        );
        MatcherAssert.assertThat(
            chat.yamlSequence("Announcements")
                .yamlMapping(0)
                .yamlSequence("list")
                .string(0),
            Matchers.equalTo("a. string")
        );
        MatcherAssert.assertThat(
            chat.string("list"),
            Matchers.equalTo("not actually a list")
        );
        MatcherAssert.assertThat(
            chat.yamlMapping("Broadcast")
                .string("enabled"),
            Matchers.equalTo("true")
        );
    }

    /**
     * Read a test resource file's contents.
     * @param fileName File to read.
     * @return File's contents as String.
     * @throws FileNotFoundException If something is wrong.
     * @throws IOException If something is wrong.
     */
    private String readTestResource(final String fileName)
        throws FileNotFoundException, IOException {
        return new String(
            IOUtils.toByteArray(
                new FileInputStream(
                    new File("src/test/resources/" + fileName)
                )
            )
        );
    }

}
