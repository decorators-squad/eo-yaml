/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
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

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * Visitor which converts the Yaml to JSON.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @checkstyle ExecutableStatementCount (1000 lines)
 * @since 8.0.2
 */
final class YamlToJsonVisitor implements YamlVisitor<JsonValue> {

    @Override
    public JsonValue visitYamlMapping(final YamlMapping node) {
        final JsonObjectBuilder object = Json.createObjectBuilder();
        node.keys().forEach(
            k -> {
                if(k instanceof Scalar) {
                    final String stringKey = ((Scalar) k).value();
                    object.add(
                        stringKey,
                        this.visitYamlNode(node.value(stringKey))
                    );
                } else {
                    throw new IllegalArgumentException(
                        "YamlMapping contains key which is not a Scalar. "
                        + "Cannot convert to JSON!"
                    );
                }
            }
        );
        return object.build();
    }

    @Override
    public JsonValue visitYamlSequence(final YamlSequence node) {
        final JsonArrayBuilder array = Json.createArrayBuilder();
        node.values().forEach(
            v -> array.add(this.visitYamlNode(v))
        );
        return array.build();
    }

    @Override
    public JsonValue visitYamlStream(final YamlStream node) {
        final JsonArrayBuilder array = Json.createArrayBuilder();
        node.values().forEach(
            v -> array.add(this.visitYamlNode(v))
        );
        return array.build();
    }

    @Override
    public JsonValue visitScalar(final Scalar node) {
        return Json.createValue(node.value());
    }

    @Override
    public JsonValue defaultResult() {
        return JsonValue.EMPTY_JSON_OBJECT;
    }

    @Override
    public JsonValue aggregateResult(
        final JsonValue aggregate,
        final JsonValue nextResult
    ) {
        return null;
    }
}
