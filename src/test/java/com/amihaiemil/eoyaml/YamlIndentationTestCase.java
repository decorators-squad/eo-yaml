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

import com.amihaiemil.eoyaml.exceptions.YamlIndentationException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Test cases regarding an input YAML's indentation, particularly
 * cases in which the input is not indented properly, we have keys without
 * values etc.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 5.1.0
 */
public final class YamlIndentationTestCase {

    /**
     * A Yaml mapping containing an unindented sequence can be read
     * if we guess the correct indentation.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsUnintendedSequenceException() throws Exception {
        final YamlMapping map = Yaml.createYamlInput(
            new File("src/test/resources/badSequenceIndentationInMapping.yml")
        ).readYamlMapping();
        final YamlSequence developers = map.yamlSequence("developers");
        MatcherAssert.assertThat(
            developers,
            Matchers.iterableWithSize(3)
        );
        MatcherAssert.assertThat(
            developers.string(0),
            Matchers.equalTo("amihaiemil")
        );
        MatcherAssert.assertThat(
            developers.string(1),
            Matchers.equalTo("sherif")
        );
        MatcherAssert.assertThat(
            developers.string(2),
            Matchers.equalTo("rultor")
        );
    }

    /**
     * A badly indented YAML mapping, which also has an empty key,
     * is read properly.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void readsBadlyIndentedMappingWithEmptyKey() throws Exception {
        final YamlMapping map = Yaml.createYamlInput(
            new File("src/test/resources/badMappingIndentation.yml")
        ).readYamlMapping();
        System.out.println(map);
        MatcherAssert.assertThat(
            map.string("name"), Matchers.equalTo("eo-yaml")
        );
        MatcherAssert.assertThat(
            map.string("contributors"), Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            map.value("contributors").comment().value(),
            Matchers.equalTo("empty key...")
        );
        final YamlSequence developers = map.yamlSequence("developers");
        MatcherAssert.assertThat(
            developers.size(),
            Matchers.equalTo(3)
        );
        MatcherAssert.assertThat(
            developers.string(0),
            Matchers.equalTo("amihaiemil")
        );
        MatcherAssert.assertThat(
            developers.string(1),
            Matchers.equalTo("sherif")
        );
        MatcherAssert.assertThat(
            developers.string(2),
            Matchers.equalTo("rultor")
        );
    }


    /**
     * A badly indented YAML sequence throws an exception.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void badlyIndentedSequenceThrowsException() throws Exception {
        try {
            final List<YamlLine> lines = new ArrayList<>();
            lines.add(new RtYamlLine("value1", 1));
            lines.add(new RtYamlLine(" lGvSz", 2));
            lines.add(new RtYamlLine("value3", 3));
            final YamlLines yamlLines = new AllYamlLines(lines);
            final YamlNode seq = yamlLines.nextYamlNode(
                    new RtYamlLine("foldedSequence:|-", 0));
            final Collection<YamlNode> values = ((YamlSequence) seq).values();
            Assert.fail("badlyIntendedSequenceThrowsException "
                    + "should have thrown YamlIndentationException");
        } catch (final YamlIndentationException expected) {
            MatcherAssert.assertThat(expected.getMessage(), Matchers.equalTo(
                    "Indentation of line 3 [lGvSz] is greater "
                    + "than the one of line 2 [value1]. "
                    + "It should be less or equal."));
        }
    }

}
