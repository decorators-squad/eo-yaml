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
package com.amihaiemil.eoyaml.spec;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlSequence;
import java.io.File;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * It tests the spec 1.2 examples.
 *
 * @author Aislan Nadrowski (aislan.nadrowski@gmail.com)
 * @version $Id$
 * @since 4.3.6
 *
 * @todo #336:30min Add more tests to cover all the examples from the 
 *  Yaml 1.2 Specification. You can have a look at PR 335 to see how 
 *  other examples have been tested. Don't forget that you do not have 
 *  to write all the tests in this task -- write 1 or 2 tests, leave 
 *  a todo (puzzle) and the work will continue in a future ticket.
 */
public final class SpecExamplesITCase {

    /**
     * It tests the example 2.1 of the Yaml Spec 1.2.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void readSpecExampleTwoDotOne() throws Exception {
        final YamlSequence expected = Yaml.createYamlSequenceBuilder()
            .add("Mark McGwire")
            .add("Sammy Sosa")
            .add("Ken Griffey")
            .build();
        System.out.println(expected);
        final YamlSequence actual = Yaml.createYamlInput(
            new File("src/test/resources/spec/spec_2_1.yml")
        ).readYamlSequence();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * It tests the example 2.2 of the Yaml Spec 1.2.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void readSpecExampleTwoDotTwo() throws Exception {

        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add(
                "hr",
                Yaml.createYamlScalarBuilder()
                    .addLine("65")
                    .buildPlainScalar("Home runs")
            )
            .add(
                "avg",
                Yaml.createYamlScalarBuilder()
                    .addLine("0.278")
                    .buildPlainScalar("Batting average")
            )
            .add(
                "rbi",
                Yaml.createYamlScalarBuilder()
                    .addLine("147")
                    .buildPlainScalar("Runs Batted In")
            )
            .build();
        System.out.println(expected);
        final YamlMapping actual = Yaml.createYamlInput(
            new File("src/test/resources/spec/spec_2_2.yml")
        ).readYamlMapping();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * It tests the example 2.3 of the Yaml Spec 1.2.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void readSpecExampleTwoDotThree() throws Exception {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("american",
                Yaml.createYamlSequenceBuilder()
                    .add("Boston Red Sox")
                    .add("Detroit Tigers")
                    .add("New York Yankees").build()
            )
            .add("national",
                Yaml.createYamlSequenceBuilder()
                    .add("New York Mets")
                    .add("Chicago Cubs")
                    .add("Atlanta Braves").build()
            ).build();

        System.out.println(expected);
        final YamlMapping actual = Yaml.createYamlInput(
            new File("src/test/resources/spec/spec_2_3.yml")
        ).readYamlMapping();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * It tests the example 2.4 of the Yaml Spec 1.2.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void readFourthSpecExample() throws Exception {
        final YamlSequence expected = Yaml.createYamlSequenceBuilder()
            .add(Yaml.createYamlMappingBuilder()
                .add("name", "Mark McGwire")
                .add("hr", "65")
                .add("avg", "0.278")
                .build()
            )
            .add(Yaml.createYamlMappingBuilder()
                .add("name", "Sammy Sosa")
                .add("hr", "63")
                .add("avg", "0.288")
                .build()
            ).build();

        System.out.println(expected);
        final YamlSequence actual = Yaml.createYamlInput(
            new File("src/test/resources/spec/spec_2_4.yml")
        ).readYamlSequence();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * It tests the example 2.19 of the Yaml Spec 1.2.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void readSpecExampleTwoDotNineteen() throws Exception {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("canonical", "12345")
            .add("decimal", "+12345")
            .add("octal", "0o14")
            .add("hexadecimal", "0xC")
            .build();
        System.out.println(expected);
        final YamlMapping actual = Yaml.createYamlInput(
            new File("src/test/resources/spec/spec_2_19.yml")
        ).readYamlMapping();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * It tests the example 2.20 of the Yaml Spec 1.2.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void readSpecExampleTwoDotTwenty() throws Exception {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("canonical", "1.23015e+3")
            .add("exponential", "12.3015e+02")
            .add("fixed", "1230.15")
            .add("negative infinity", "-.inf")
            .add("not a number", ".NaN")
            .build();
        System.out.println(expected);
        final YamlMapping actual = Yaml.createYamlInput(
            new File("src/test/resources/spec/spec_2_20.yml")
        ).readYamlMapping();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

    /**
     * It tests the example 2.22 of the Yaml Spec 1.2.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void readSpecExampleTwoDotTwentyTwo() throws Exception {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("canonical", "2001-12-15T02:59:43.1Z")
            .add("iso8601", "2001-12-14t21:59:43.10-05:00")
            .add("spaced", "2001-12-14 21:59:43.10 -5")
            .add("date", "2002-12-14")
            .build();
        System.out.println(expected);
        final YamlMapping actual = Yaml.createYamlInput(
            new File("src/test/resources/spec/spec_2_22.yml")
        ).readYamlMapping();
        MatcherAssert.assertThat(expected, Matchers.equalTo(actual));
    }

}
