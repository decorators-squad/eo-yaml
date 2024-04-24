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
package com.amihaiemil.eoyaml.spec;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Run through the YAML Test suite.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 8.0.0
 */
public final class YamlTestSuiteITCase {

    @Test
    public void test() throws Exception {
        final Iterator<File> yamlTestSuite = FileUtils.iterateFiles(
            new File("src/test/resources/yaml_test_suite/"),
            new String[]{"yaml"},
            false
        );
        final List<String> failedTests = new ArrayList<>();
        int i=0;
        int j=0;
        while(yamlTestSuite.hasNext()) {
            i++;
            final File file = yamlTestSuite.next();
            try {
                final YamlMapping testCase = Yaml.createYamlInput(file)
                    .readYamlSequence().yamlMapping(0);
                if(Boolean.parseBoolean(testCase.string("fail"))) {
                    j++;
                    continue;
                }
                final YamlNode actual = Yaml.createYamlInput(
                    testCase.string("yaml")
                ).readYamlNode();
                final YamlNode expected = Yaml.createYamlInput(
                    testCase.string("dump")
                ).readYamlNode();
                MatcherAssert.assertThat(actual, Matchers.equalTo(expected));
            } catch (Exception | AssertionError ex) {
                failedTests.add(file.getName());
            }
        }
        System.out.println("FAILED TESTS READ COUNT " + failedTests.size() + " OUT OF TOTAL " + (i-j));
    }
}
