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
package com.amihaiemil.eoyaml;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.json.Json;

/**
 * Unit tests for {@link JsonYamlDump}.
 * @author criske
 * @version $Id$
 * @since 5.1.7
 */
public final class JsonYamlDumpTest {

    /**
     * JsonYamlDump can dump null value.
     */
    @Test
    public void canDumpNullValue(){
        MatcherAssert.assertThat(new JsonYamlDump(null).dump(),
            Matchers.equalTo(new PlainStringScalar("null")));
    }

    /**
     * JsonYamlDump can dump JsonYamlMapping from json object.
     */
    @Test
    public void canDumpJsonObject(){
        MatcherAssert.assertThat(new JsonYamlDump(Json
                .createObjectBuilder().build()).dump(),
            Matchers.instanceOf(JsonYamlMapping.class));
    }

    /**
     * JsonYamlDump can dump JsonYamlSequence from json array.
     */
    @Test
    public void canDumpJsonArray(){
        MatcherAssert.assertThat(new JsonYamlDump(Json
                .createArrayBuilder().build()).dump(),
            Matchers.instanceOf(JsonYamlSequence.class));
    }
}