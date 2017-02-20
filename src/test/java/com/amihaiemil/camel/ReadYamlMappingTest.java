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

/**
 * Unit tests for {@link ReadYamlMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class ReadYamlMappingTest {

    /**
     * ReadYamlMapping can return the YamlMapping mapped to a 
     * String key.
     */
    @Test
    public void returnsYamlMappingWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new RtYamlLines(lines));
        MatcherAssert.assertThat(
            map.yamlMapping("second"), Matchers.notNullValue()
        );
    }

    /**
     * ReadYamlMapping can throw an ISE when the mapping is not correct.
     */
    @Test (expected = IllegalStateException.class)
    public void throwsExceptionWhenBadMapping(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("missingcolon", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        final YamlMapping map = new ReadYamlMapping(new RtYamlLines(lines));
        map.yamlMapping("missingcolon");
    }
}
