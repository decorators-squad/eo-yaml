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

import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Test cases for printing a YamlMapping together with its
 * added or read comments.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.2.0
 */
public final class YamlMappingCommentsPrintTest {

    /**
     * A built YamlMapping with added comments is being
     * printed properly.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void printsBuiltYamlMappingWithComments() throws Exception {
        final YamlMapping commented = Yaml.createYamlMappingBuilder()
            .add("architect", "mihai")
            .add(
                "developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("salikjan")
                    .add("sherif")
                    .build("all the contributors here")
            )
            .add(
                "name",
                Yaml.createYamlScalarBuilder()
                    .addLine("eo-yaml")
                    .buildPlainScalar("name of the project")
            ).build("Comment of the whole document");
        System.out.println(commented);
        System.out.println("***************");
        MatcherAssert.assertThat(
            commented.toString(),
            Matchers.equalTo(
                this.readExpected("commentedMapping.yml")
            )
        );
        System.out.println("-------------- VISITOR PRING ----------------");
        final YamlVisitor<String> visitor = new YamlPrintVisitor();
        String print = commented.accept(visitor);
        System.out.println(print);
        System.out.println("-------------- END VISITOR PRING -------------");
    }

    /**
     * A read YamlMapping should print itself together with the
     * read comments.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void printsReadYamlMappingWithComments() throws Exception {
        final YamlMapping read = Yaml.createYamlInput(
            new File("src/test/resources/commentedMapping.yml")
        ).readYamlMapping();
        System.out.println(read);
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(
                this.readExpected("commentedMapping.yml")
            )
        );
    }

    /**
     * A read YamlMapping can access its comments.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsComments() throws Exception {
        final YamlMapping read = Yaml.createYamlInput(
            new File("src/test/resources/commentedMapping.yml")
        ).readYamlMapping();
        System.out.println(read);
        MatcherAssert.assertThat(
            read.comment().value(),
            Matchers.equalTo("Comment of the whole document")
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").comment().value(),
            Matchers.equalTo("all the contributors here")
        );
        MatcherAssert.assertThat(
            read.value("name").comment().value(),
            Matchers.equalTo("name of the project")
        );
    }

    /**
     * A read YamlMapping can access its comments.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsMultilineComments() throws Exception {
        final YamlMapping read = Yaml.createYamlInput(
            new File(
                "src/test/resources/multilineCommentedMapping.yml"
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(
            read.comment().value(),
            Matchers.equalTo("Comment of the whole document")
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").comment().value(),
            Matchers.equalTo("all the contributors here")
        );
        MatcherAssert.assertThat(
            read.value("name").comment().value(),
            Matchers.equalTo("name of the project")
        );
        MatcherAssert.assertThat(
            read.value("devops").comment().value(),
            Matchers.equalTo(
            "Our DevOps contributors."
                + System.lineSeparator()
                + "They are chatbots."
            )
        );
        MatcherAssert.assertThat(
            read.value("platform").comment().value(),
            Matchers.equalTo(
            "The project is hosted at Github"
                + System.lineSeparator()
                + "under the \"decorators-squad\" Organization"
            )
        );
    }

    /**
     * A read YamlMapping can access its document comment, as
     * well as the comment of the very first node.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void distinguishDocumentCommentFromNodeComment() throws Exception {
        final YamlMapping read = Yaml.createYamlInput(
            new File(
                "src/test/resources/mappingWithDocumentComment.yml"
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(
            read.comment().value(),
            Matchers.equalTo("Comment of the whole document")
        );
        MatcherAssert.assertThat(
            read.yamlSequence("architects").comment().value(),
            Matchers.equalTo(
                "Architects of the project"
                + System.lineSeparator()
                + "Feel free to contribute"
            )
        );
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(
                this.readExpected("mappingWithDocumentComment.yml")
            )
        );
    }

    /**
     * Reads scalar comments from a mapping properly.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void readsScalarComments() throws IOException {
        final YamlMapping read = Yaml.createYamlInput(
            new File(
                "src/test/resources/scalarCommentsInMapping.yml"
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(
            read.value("architect").comment().value(),
            Matchers.equalTo(
                "Mihai is the architect,\nhe has 8 years Java XP"
            )
        );
        MatcherAssert.assertThat(
            read.value("name").comment().value(),
            Matchers.equalTo(
                "name of the project"
            )
        );
        MatcherAssert.assertThat(
            read.value("provider").comment().value(),
            Matchers.equalTo(
                "git web service"
            )
        );
        MatcherAssert.assertThat(
            read.value("devops").comment().value(),
            Matchers.equalTo("")
        );
        MatcherAssert.assertThat(
            read.value("tech").comment().value(),
            Matchers.equalTo(
                "eo-yaml is written in Java\n"
                + "and it has no dependencies\n"
                + "Java SE 8+"
            )
        );
    }

    /**
     * A read YamlMapping should print itself together with the
     * read scalar .
     * @throws Exception If something goes wrong.
     */
    @Test
    public void printsReadYamlMappingWithScalarComments() throws Exception {
        final YamlMapping read = Yaml.createYamlInput(
            new File("src/test/resources/scalarCommentsInMapping.yml")
        ).readYamlMapping();
        System.out.println(read);
        MatcherAssert.assertThat(
            read.toString(),
            Matchers.equalTo(
                this.readExpected("scalarCommentsInMapping.yml")
            )
        );
    }

    /**
     * We can print a built scalar with 2 comments.
     */
    @Test
    public void printsBuiltScalarWithTwoComments() {
        final Scalar scalar = Yaml.createYamlScalarBuilder()
            .addLine("simple")
            .buildPlainScalar("comment on top\nline2", "comment");
        System.out.println(scalar);
        MatcherAssert.assertThat(
            scalar.toString(),
            Matchers.equalTo(
                "---" + System.lineSeparator()
                + "# comment on top" + System.lineSeparator()
                + "# line2" + System.lineSeparator()
                + "simple # comment" + System.lineSeparator()
                + "..."
            )
        );
    }

    /**
     * We can print a built scalar an inline comment.
     */
    @Test
    public void printsBuiltScalarWithInline() {
        final Scalar scalar = Yaml.createYamlScalarBuilder()
            .addLine("simple")
            .buildPlainScalar("comment");
        System.out.println(scalar);
        MatcherAssert.assertThat(
            scalar.toString(),
            Matchers.equalTo(
                "---" + System.lineSeparator()
                    + "simple # comment" + System.lineSeparator()
                    + "..."
            )
        );
    }

    /**
     * We can print a built scalar with comment above.
     */
    @Test
    public void printsBuiltScalarWithAboveComment() {
        final Scalar scalar = Yaml.createYamlScalarBuilder()
            .addLine("simple")
            .buildPlainScalar("comment on top\nline2", "");
        System.out.println(scalar);
        MatcherAssert.assertThat(
            scalar.toString(),
            Matchers.equalTo(
                "---" + System.lineSeparator()
                    + "# comment on top" + System.lineSeparator()
                    + "# line2" + System.lineSeparator()
                    + "simple" + System.lineSeparator()
                    + "..."
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
                        "src/test/resources/" + fileName
                    )
                )
            )
        );
    }
}
