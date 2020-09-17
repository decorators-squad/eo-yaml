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

import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Full printing tests for built and read YamlSequences.
 * This will call toString() triggering the whole printing
 * process done by the internal indent(...) method.
 *
 * Assertions on printed YAML are done in other tests as well,
 * but it's better to also have a dedicated test class.
 * @checkstyle LineLength (300 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
public final class YamlSequencePrintTest {

    /**
     * We read a YamlSequence containing all types of
     * YamlNode we have so far and print it.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void printsReadYamlSequenceWithAllNodes() throws Exception {
        final YamlSequence read = Yaml.createYamlInput(
            new File("src/test/resources/printing_tests/yamlSequenceAllNodes.txt")
        ).readYamlSequence();
        System.out.print(read);
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(
                this.readExpected("yamlSequenceAllNodes.txt")
            )
        );
    }

    /**
     * We build a YamlSequence containing all types of
     * YamlNode we have so far and print it.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void printsBuiltYamlSequenceWithAllNodes() throws Exception {
        final YamlSequence built = Yaml.createYamlSequenceBuilder()
            .add("plain scalar")
            .add(
                Yaml.createYamlScalarBuilder()
                    .addLine("literal")
                    .addLine("block")
                    .addLine("scalar")
                    .buildLiteralBlockScalar()
            )
            .add(
                Yaml.createYamlScalarBuilder()
                    .addLine("a scalar folded")
                    .addLine("on more lines")
                    .addLine("for readability")
                    .buildFoldedBlockScalar()
            )
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("key", "value")
                    .build()
            )
            .add(
                Yaml.createYamlSequenceBuilder()
                    .add("a sequence")
                    .add("of plain scalars")
                    .add("as child")
                    .build()
            )
            .add(Yaml.createYamlSequenceBuilder().build())
            .add(Yaml.createYamlMappingBuilder().build())
            .build();
        MatcherAssert.assertThat(
            built.toString(),
            Matchers.equalTo(
                this.readExpected("yamlSequenceAllNodes.txt")
            )
        );
    }

    /**
     * An empty YamlSequence value is printed as empty sequence ([]).
     */
    @Test
    public void printsEmptySequenceAsNull() {
        final YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("value1")
            .add(Yaml.createYamlSequenceBuilder().build())
            .add("value2")
            .build();
        final StringBuilder expected = new StringBuilder();
        expected
            .append("- value1").append(System.lineSeparator())
            .append("- []").append(System.lineSeparator())
            .append("- value2");
        MatcherAssert.assertThat(
            sequence.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * An null YamlSequence value is printed as null.
     */
    @Test
    public void printsNullSequenceAsNull() {
        final YamlSequence seq = null;
        final YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("value1")
            .add(seq)
            .add("value2")
            .build();
        final StringBuilder expected = new StringBuilder();
        expected
            .append("- value1").append(System.lineSeparator())
            .append("- null").append(System.lineSeparator())
            .append("- value2");
        MatcherAssert.assertThat(
            sequence.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * An empty YamlMapping value is printed as empty mapping ({}).
     */
    @Test
    public void printsEmptyMappingAsNull() {
        final YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("value1")
            .add(Yaml.createYamlMappingBuilder().build())
            .add("value2")
            .build();
        final StringBuilder expected = new StringBuilder();
        expected
            .append("- value1").append(System.lineSeparator())
            .append("- {}").append(System.lineSeparator())
            .append("- value2");
        MatcherAssert.assertThat(
            sequence.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * An null YamlMapping value is printed as null.
     */
    @Test
    public void printsNullMappingAsNull() {
        final YamlMapping nullMap = null;
        final YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("value1")
            .add(nullMap)
            .add("value2")
            .build();
        final StringBuilder expected = new StringBuilder();
        expected
            .append("- value1").append(System.lineSeparator())
            .append("- null").append(System.lineSeparator())
            .append("- value2");
        MatcherAssert.assertThat(
            sequence.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * An empty Scalar value is printed as null.
     */
    @Test
    public void printsEmptyScalarAsNull() {
        final YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("scalar1")
            .add(Yaml.createYamlScalarBuilder().buildPlainScalar())
            .add("scalar2")
            .build();
        final StringBuilder expected = new StringBuilder();
        expected
            .append("- scalar1").append(System.lineSeparator())
            .append("- null").append(System.lineSeparator())
            .append("- scalar2");
        MatcherAssert.assertThat(
            sequence.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * An null Scalar value is printed as null.
     */
    @Test
    public void printsNullScalarAsNull() {
        final Scalar nullScalar = null;
        final YamlSequence sequence = Yaml.createYamlSequenceBuilder()
            .add("scalar1")
            .add(nullScalar)
            .add("scalar2")
            .build();
        final StringBuilder expected = new StringBuilder();
        expected
            .append("- scalar1").append(System.lineSeparator())
            .append("- null").append(System.lineSeparator())
            .append("- scalar2");
        MatcherAssert.assertThat(
            sequence.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * When a scalar value in a sequence contains a reserved symbol,
     * it should be escaped (wrapped between quotes or apostrophes).
     */
    @Test
    public void printsEscapedScalars() {
        final YamlMapping map = Yaml.createYamlMappingBuilder()
            .add(
                "sequence",
                Yaml.createYamlSequenceBuilder()
                    .add("15:00")
                    .add("#314132")
                    .add("&gt;")
                    .add("$15")
                    .add("a>b")
                    .add("a || b")
                    .add("-15C")
                    .add("3% reads \"3 per cent\"")
                    .build()
            ).build();
        final StringBuilder expected = new StringBuilder();
        expected
            .append("- \"15:00\"").append(System.lineSeparator())
            .append("- \"#314132\"").append(System.lineSeparator())
            .append("- \"&gt;\"").append(System.lineSeparator())
            .append("- \"$15\"").append(System.lineSeparator())
            .append("- \"a>b\"").append(System.lineSeparator())
            .append("- \"a || b\"").append(System.lineSeparator())
            .append("- \"-15C\"").append(System.lineSeparator())
            .append("- '3% reads \"3 per cent\"'");
        MatcherAssert.assertThat(
            map.yamlSequence("sequence").toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * Read a test resource file's contents.
     * @param fileName File to read.
     * @return File's contents as String.
     * @throws FileNotFoundException If something is wrong.
     * @throws IOException If something is wrong.
     */
    private String readExpected(final String fileName)
        throws FileNotFoundException, IOException {
        return new String(
            IOUtils.toByteArray(
                new FileInputStream(
                    new File(
                        "src/test/resources/printing_tests/" + fileName
                    )
                )
            )
        );
    }
}
