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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link RtYamlScalarBuilder}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
public final class RtYamlScalarBuilderTest {

    /**
     * RtYamlScalarBuilder can add line of text.
     */
    @Test
    public void addsLineOfText() {
        final YamlScalarBuilder scalarBuilder = new RtYamlScalarBuilder();
        final YamlScalarBuilder withAdded = scalarBuilder.addLine("a scalar");
        MatcherAssert.assertThat(withAdded, Matchers.notNullValue());
        MatcherAssert.assertThat(
            scalarBuilder, Matchers.not(Matchers.is(withAdded))
        );
    }

    /**
     * RtYamlScalarBuilder can build a plain scalar with no newlines.
     * @checkstyle LineLength (30 lines)
     */
    @Test
    public void buildsPlainScalarNoNewLines() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .addLine("a plain scalar")
            .addLine("that will be on the same line")
            .buildPlainScalar();
        MatcherAssert.assertThat(
            scalar.value(), Matchers.equalTo("a plain scalar that will be on the same line")
        );
    }

    /**
     * RtYamlScalarBuilder can build a plain scalar, while removing
     * the hardcoded newlines.
     * @checkstyle LineLength (30 lines)
     */
    @Test
    public void buildsPlainScalarRemovesAddedNewLines() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .addLine("a plain scalar")
            .addLine("that will be" + System.lineSeparator() + "on the same line.")
            .addLine("You cannot trick it.")
            .buildPlainScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo("a plain scalar that will be on the same line. You cannot trick it.")
        );
    }

    /**
     * RtYamlScalarBuilder can build on a single line of text.
     */
    @Test
    public void buildsSingleLinePlainScalar() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .addLine("value").buildPlainScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo("value")
        );
    }

    /**
     * RtYamlScalarBuilder can build an empty plain scalar.
     */
    @Test
    public void buildsEmptyPlainScalar() {
        final Scalar scalar = new RtYamlScalarBuilder().buildPlainScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.isEmptyString()
        );
    }

    /**
     * RtYamlScalarBuilder can build a folded block scalar
     * on a single line of text.
     */
    @Test
    public void buildsSingleLineFoldedBlockScalar() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .addLine("value").buildFoldedBlockScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo("value")
        );
    }

    /**
     * RtYamlScalarBuilder can build a literal block scalar
     * on a single line of text.
     */
    @Test
    public void buildsSingleLineLiteralBlockScalar() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .addLine("value").buildLiteralBlockScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo("value")
        );
    }

    /**
     * RtYamlScalarBuilder can build an empty folded block scalar.
     */
    @Test
    public void buildsEmptyFoldedBlockScalar() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .buildFoldedBlockScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.isEmptyString()
        );
    }

    /**
     * RtYamlScalarBuilder can build an empty literal block scalar.
     */
    @Test
    public void buildsEmptyLiteralBlockScalar() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .buildLiteralBlockScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.isEmptyString()
        );
    }

    /**
     * RtYamlScalarBuilder can build a folded block scalar with multiple lines.
     * @checkstyle LineLength (30 lines)
     */
    @Test
    public void buildsFoldedBlockScalarNoNewLines() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .addLine("a folded scalar")
            .addLine("that will be on the same line")
            .buildPlainScalar();
        MatcherAssert.assertThat(
            scalar.value(), Matchers.equalTo("a folded scalar that will be on the same line")
        );
    }

    /**
     * RtYamlScalarBuilder can build a folded block scalar with multiple lines.
     * @checkstyle LineLength (30 lines)
     */
    @Test
    public void buildsFoldedBlockScalarRemovesAddedNewLines() {
        final Scalar scalar = new RtYamlScalarBuilder()
            .addLine("a folded block scalar")
            .addLine("that will be" + System.lineSeparator() + "on the same line.")
            .addLine("Its value will be on a single line because NEWLINE is omitted.")
            .buildFoldedBlockScalar();
        MatcherAssert.assertThat(
            scalar.value(),
            Matchers.equalTo(
            "a folded block scalar that will be on the same line. Its value will be on a single line because NEWLINE is omitted."
            )
        );
    }
}
