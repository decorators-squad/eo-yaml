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
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Set;

/**
 * Unit tests for {@link ReadFlowMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 6.0.0
 */
public final class ReadFlowMappingTestCase {

    /**
     * YamlMapping in flow format has no comment.
     */
    @Test
    public void hasNoComment() {
        final YamlMapping map = new ReadFlowMapping(
            Mockito.mock(YamlLine.class)
        );
        MatcherAssert.assertThat(
            map.comment().value(),
            Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            map.comment().yamlNode(),
            Matchers.is(map)
        );
    }

    /**
     * ReadFlowMapping can return its keys.
     */
    @Test
    public void returnsKeys() {
        final YamlMapping map = new ReadFlowMapping(
            new YamlLine.NullYamlLine(),
            new AllYamlLines(
                Arrays.asList(
                    new RtYamlLine("{['a']: 'b',", 0),
                    new RtYamlLine(" [c,g]: d,", 1),
                    new RtYamlLine(" e: f,", 2),
                    new RtYamlLine("     {y:r}: {h: i},", 3),
                    new RtYamlLine("k: 4,"
                        + "o: [a, '0,3', \"2, 3, 4\", {ii: \"5,6,7\"}, b, c],",
                        4
                    ),
                    new RtYamlLine("t: \"0,3\"}", 5)
                )
            )
        );
        final Set<YamlNode> keys = map.keys();
        keys.stream().forEach(
            k -> {
                System.out.println("KEY: " + k);
                System.out.println("VALUE: " + map.value(k));
            }
        );
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(7));
    }

}
