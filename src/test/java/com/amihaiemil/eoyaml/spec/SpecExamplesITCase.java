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
import com.amihaiemil.eoyaml.YamlSequence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * It tests the spec 1.2 examples.
 * @author Aislan Nadrowski (aislan.nadrowski@gmail.com)
 * @version $Id$
 * @sinve 4.3.6
 */
public final class SpecExamplesITCase {
    
    /**
     * It tests the example 2.1 of the Yaml Spec 1.2.
     * @throws FileNotFoundException If the file was not found.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void readFirstSpecExample() 
            throws FileNotFoundException, IOException {
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
}
