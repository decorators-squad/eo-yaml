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
package com.amihaiemil.camel;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.amihaiemil.camel.YamlObjectDumpTest.StudentSimplePojo;

/**
 * Unit tests for {@link YamlCollectionDump}.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id: a29bf5e43c279d0f8c1cef6432605c53d23f673c $
 * @since 1.0.0
 * 
 */
public final class YamlCollectionDumpTest {
    
    /**
     * YamlCollectionDump can represent Collection of strings and simple pojos.
     */
    @Test
    public void representsCollectionOfStringsAndSimplePojos() {
        StudentSimplePojo studentA = 
            new StudentSimplePojo("John", "Doe", 25, 4);
        List<Object> collection = new ArrayList<Object>();
        collection.add(studentA);
        collection.add("objectA");
        
        YamlSequence yaml = new YamlCollectionDump(collection).represent();
        MatcherAssert.assertThat(yaml.size(), Matchers.equalTo(2));
        MatcherAssert.assertThat(yaml.string(0), Matchers.equalTo("objectA"));
        
        YamlMapping yamlMapping = yaml.yamlMapping(1);
        MatcherAssert.assertThat(
            yamlMapping.string("firstName"),
            Matchers.equalTo("John")
        );
        MatcherAssert.assertThat(
            yamlMapping.string("lastName"),
            Matchers.equalTo("Doe")
        );
        MatcherAssert.assertThat(
            yamlMapping.string("age"),
            Matchers.equalTo("25")
        );
        MatcherAssert.assertThat(
            yamlMapping.string("gpa"),
            Matchers.equalTo("4.0")
        );
    }
}
