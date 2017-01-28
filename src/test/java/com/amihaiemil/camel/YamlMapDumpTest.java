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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.amihaiemil.camel.YamlObjectDumpTest.StudentSimplePojo;

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
     * @todo #38:30m/DEV Complete unit test for YamlMapDump class.
     */
    @Test
    public void representsSimpleDojo() {
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
        Collection<YamlNode> children = yaml.children();
        MatcherAssert.assertThat(children.size(), Matchers.equalTo(2));
    }
}
