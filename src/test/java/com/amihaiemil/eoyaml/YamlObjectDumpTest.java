/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
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

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link YamlObjectDump}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class YamlObjectDumpTest {

    /**
     * YamlObjectDump can represent a simple pojo.
     */
    @Test
    public void representsSimplePojo() {
        YamlMapping yaml = new YamlObjectDump(
            new StudentSimplePojo("John", "Doe", 21, 3.7)
        ).represent();
        MatcherAssert.assertThat(
            yaml.string("firstName"), Matchers.equalTo("John")
        );
        MatcherAssert.assertThat(
            yaml.string("lastName"), Matchers.equalTo("Doe")
        );
        MatcherAssert.assertThat(
            yaml.string("age"), Matchers.equalTo("21")
        );
        MatcherAssert.assertThat(
            yaml.string("gpa"), Matchers.equalTo("3.7")
        );
        YamlMapping grades = yaml.yamlMapping("grades");
        MatcherAssert.assertThat(
            grades.children().size(), Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            grades.string("Math"), Matchers.equalTo("9")
        );
        MatcherAssert.assertThat(
            grades.string("CS"), Matchers.equalTo("10")
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
    public static final class StudentSimplePojo {
        
        private String firstName;
        private String lastName;
        private int age;
        private double gpa;
        private Map<String, Integer> grades = new HashMap<>();
        
        public StudentSimplePojo(
            String firstName, String lastName, int age, double gpa
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
