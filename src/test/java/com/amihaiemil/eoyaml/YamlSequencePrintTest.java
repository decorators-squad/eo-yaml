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
            .build();
        MatcherAssert.assertThat(
            built.toString(),
            Matchers.equalTo(
                this.readExpected("yamlSequenceAllNodes.txt")
            )
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
