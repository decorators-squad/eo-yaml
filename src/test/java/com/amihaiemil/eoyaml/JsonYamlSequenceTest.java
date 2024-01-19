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
import javax.json.JsonArray;

/**
 * Unit tests for {@link JsonYamlSequence}.
 * @author criske
 * @version $Id$
 * @since 5.1.7
 */
public final class JsonYamlSequenceTest {

    /**
     * JsonYamlSequence can map a json array.
     */
    @Test
    public void canMapJsonArray(){
        final JsonArray array =  Json.createArrayBuilder()
            .add("rultor")
            .add("salikjan")
            .add("sherif")
            .build();
        final JsonYamlSequence sequence = new JsonYamlSequence(array);
        MatcherAssert.assertThat(sequence.values(),
            Matchers.iterableWithSize(3));
        MatcherAssert.assertThat(sequence.values().iterator().next(),
            Matchers.equalTo(new PlainStringScalar("rultor"))
        );
    }

    /**
     * JsonYamlSequence has empty Comment.
     */
    @Test
    public void hasEmptyComment(){
        final Comment comment = new JsonYamlSequence(Json
            .createArrayBuilder()
            .build())
            .comment();
        MatcherAssert.assertThat(comment.yamlNode(), Matchers
            .instanceOf(JsonYamlSequence.class));
        MatcherAssert.assertThat(comment.value(), Matchers
            .isEmptyString());
    }
}