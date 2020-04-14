/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Unit tests for {@link ReflectedYamlMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.3.3
 */
public final class ReflectedYamlMappingTest {
    /**
     * A reflected mapping has no comment.
     */
    @Test
    public void returnsEmptyComment() {
        final YamlMapping mapping = new ReflectedYamlMapping("map: value");
        MatcherAssert.assertThat(
            mapping.comment().yamlNode(),
            Matchers.is(mapping)
        );
        MatcherAssert.assertThat(
            mapping.comment().value(),
            Matchers.isEmptyString()
        );
    }

    /**
     * A reflected mapping reflects the object's public and non-void methods
     * as Scalar keys.
     */
    @Test
    public void reflectsKeys() {
        final YamlMapping mapping = new ReflectedYamlMapping(
            new Student("Mihai", "Test", 20, 3.5)
        );
        final List<String> keys = mapping.keys().stream().map(
            key -> ((ReflectedYamlMapping.MethodKey) key).value()
        ).collect(Collectors.toList());
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(5));
        MatcherAssert.assertThat(keys, Matchers.hasItem("firstName"));
        MatcherAssert.assertThat(keys, Matchers.hasItem("lastName"));
        MatcherAssert.assertThat(keys, Matchers.hasItem("age"));
        MatcherAssert.assertThat(keys, Matchers.hasItem("gpa"));
        MatcherAssert.assertThat(keys, Matchers.hasItem("grades"));
    }

    /**
     * A reflected mapping reflects the object's values (method returns).
     */
    @Test
    public void reflectsValuess() {
        final YamlMapping mapping = new ReflectedYamlMapping(
            new Student("Mihai", "Test", 20, 3.5)
        );
        MatcherAssert.assertThat(
            mapping.string("firstName"),
            Matchers.equalTo("Mihai")
        );
        MatcherAssert.assertThat(
            mapping.string("lastName"),
            Matchers.equalTo("Test")
        );
        MatcherAssert.assertThat(
            mapping.integer("age"),
            Matchers.is(20)
        );
        MatcherAssert.assertThat(
            mapping.doubleNumber("gpa"),
            Matchers.is(3.5)
        );
        MatcherAssert.assertThat(
            mapping.yamlMapping("grades"),
            Matchers.instanceOf(ReflectedYamlMapping.class)
        );
    }

    /**
     * Simple student pojo for test.
     * @checkstyle JavadocVariable (100 lines)
     * @checkstyle JavadocMethod (100 lines)
     * @checkstyle HiddenField (100 lines)
     * @checkstyle ParameterNumber (100 lines)
     * @checkstyle FinalParameters (100 lines)
     */
    static final class Student {

        private String firstName;
        private String lastName;
        private int age;
        private double gpa;
        private Map<String, Integer> grades = new LinkedHashMap<>();

        Student(
            String firstName, String lastName,
            int age, double gpa
        ) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gpa = gpa;
            this.grades.put("Math", 9);
            this.grades.put("CS", 10);
        }

        public String getFirstName() {
            return this.firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return this.lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public int getAge() {
            return this.age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public double getGpa() {
            return this.gpa;
        }
        public void setGpa(double gpa) {
            this.gpa = gpa;
        }

        public Map<String, Integer> getGrades() {
            return this.grades;
        }

        public void setGrades(Map<String, Integer> grades) {
            this.grades = grades;
        }

    }
}
