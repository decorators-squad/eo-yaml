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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.amihaiemil.eoyaml.YamlObjectDumpTest.StudentSimplePojo;

/**
 * Unit tests for {@link YamlMapDump}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class YamlMapDumpTest {

    /**
     * YamlMapDump can represent a Map of a simple pojo.
     */
    @Test
    public void representsMapOfSimplePojos() {
        StudentSimplePojo studentA =
            new StudentSimplePojo("John", "Doe", 21, 3.7);
        StudentSimplePojo studentB =
            new StudentSimplePojo("Albert", "Einestien", 25, 4);
        StudentSimplePojo studentC =
            new StudentSimplePojo("John", "Doe", 23, 3.7);
        StudentSimplePojo studentD =
            new StudentSimplePojo("Albert", "Einestien", 30, 4);

        Map<Object, Object> map = new HashMap<>();
        map.put(studentA, studentB);
        map.put(studentC, studentD);

        YamlMapping yaml =
            new YamlMapDump(map).represent();
        List<YamlNode> children = new ArrayList<YamlNode>(yaml.values());
        MatcherAssert.assertThat(children.size(), Matchers.equalTo(2));

        MatcherAssert.assertThat(
            this.yamlEqualsStudent((YamlMapping) children.get(0), studentB)
            && this.yamlEqualsStudent((YamlMapping) children.get(1), studentD),
            Matchers.is(true)
        );
    }

    /**
     * YamlMapDump can represent a Map of a simple pojo, with String keys.
     */
    @Test
    public void representsMapOfStringsAndSimplePojos() {
        StudentSimplePojo studentA =
            new StudentSimplePojo("John", "Doe", 25, 4);
        StudentSimplePojo studentB =
            new StudentSimplePojo("Albert", "Einestien", 30, 4);
        Map<Object, Object> map = new HashMap<>();
        map.put("key1", studentA);
        map.put("key2", studentB);

        YamlMapping yaml = new YamlMapDump(map).represent();
        MatcherAssert.assertThat(
            this.yamlEqualsStudent(yaml.yamlMapping("key1"), studentA),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            this.yamlEqualsStudent(yaml.yamlMapping("key2"), studentB),
            Matchers.is(true)
        );
        System.out.println(yaml);
    }

    /**
     * YamlMapDump can represent a Map of Strings and Integers..
     */
    @Test
    public void representsMapOfStringsAndInteers() {
        Map<Object, Object> map = new HashMap<>();
        map.put("key1", 12);
        map.put("key2", 13);

        YamlMapping yaml = new YamlMapDump(map).represent();
        MatcherAssert.assertThat(
            yaml.string("key1").equals("12"),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            yaml.string("key2").equals("13"),
            Matchers.is(true)
        );
    }

    /**
     * Method to check equality between a YamlMapping and a simple pojo.
     * @param yaml YamlMapping
     * @param student StudentSimplePojo
     * @return Returns true if parameters are equal, false otherwise
     */
    private boolean yamlEqualsStudent(final YamlMapping yaml,
        final StudentSimplePojo student) {
        return
            yaml.string("firstName").equals(student.getFirstName())
            && yaml.string("lastName").equals(student.getLastName())
            && yaml.string("age")
                .equals(((Integer) student.getAge()).toString())
            && yaml.string("gpa")
                .equals(((Double) student.getGpa()).toString());
    }
}
