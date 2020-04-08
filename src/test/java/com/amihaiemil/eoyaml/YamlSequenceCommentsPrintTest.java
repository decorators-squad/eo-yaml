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
import java.util.Iterator;

/**
 * Test cases for printing a YamlSequence together with its
 * added or read comments.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
public final class YamlSequenceCommentsPrintTest {

    /**
     * A built YamlSequence with added comments is being
     * printed properly.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void printsBuiltYamlSequenceWithComments() throws Exception {
        final YamlSequence commented = Yaml.createYamlSequenceBuilder()
            .add("element1")
            .add(
                Yaml.createYamlScalarBuilder()
                    .addLine("element2")
                    .buildPlainScalar("a plain scalar string in a sequence"))
            .add("element3")
            .add(
                Yaml.createYamlMappingBuilder()
                    .add("key", "value")
                    .add("key2", "value2")
                    .build("a mapping as an element of a sequence")
            )
            .build("a sequence with comments");
        System.out.println(commented);
        MatcherAssert.assertThat(
            commented.toString(),
            Matchers.equalTo(
                this.readExpected("commentedSequence.yml")
            )
        );
    }

    /**
     * A read YamlSequence should print itself together with the
     * read comments.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void printsReadYamlSequenceWithComments() throws Exception {
        final YamlSequence read = Yaml.createYamlInput(
            new File("src/test/resources/commentedSequence.yml")
        ).readYamlSequence();
        System.out.println(read);
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(
                this.readExpected("commentedSequence.yml")
            )
        );
    }

    /**
     * A read YamlSequence can access its comments.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsComments() throws Exception {
        final YamlSequence read = Yaml.createYamlInput(
            new File("src/test/resources/commentedSequence.yml")
        ).readYamlSequence();
        System.out.println(read);
        MatcherAssert.assertThat(
            read.comment().value(),
            Matchers.equalTo("a sequence with comments")
        );
        final Iterator<YamlNode> values = read.values().iterator();
        MatcherAssert.assertThat(
            values.next().comment().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            values.next().comment().value(),
            Matchers.equalTo("a plain scalar string in a sequence")
        );
        MatcherAssert.assertThat(
            values.next().comment().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            values.next().comment().value(),
            Matchers.equalTo("a mapping as an element of a sequence")
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
                        "src/test/resources/" + fileName
                    )
                )
            )
        );
    }
}
