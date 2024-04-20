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

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import java.util.Collection;

/**
 * Builder of YamlMapping.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlMappingBuilder {
    /**
     * Add a pair to the mapping.
     * @param key String
     * @param value String
     * @return Builder
     */
    YamlMappingBuilder add(final String key, final String value);

    /**
     * Add a pair to the mapping.
     * @param key YamlNode (sequence or mapping)
     * @param value String
     * @return Builder
     */
    YamlMappingBuilder add(final YamlNode key, final String value);

    /**
     * Add a pair to the mapping.
     * @param key YamlNode (sequence or mapping)
     * @param value YamlNode (sequence or mapping)
     * @return Builder
     */
    YamlMappingBuilder add(final YamlNode key, final YamlNode value);

    /**
     * Add a pair to the mapping.
     * @param key String
     * @param value YamlNode (sequence or mapping)
     * @return Builder
     */
    YamlMappingBuilder add(final String key, final YamlNode value);

    /**
     * Add a pair to the mapping.
     * @param key YamlNode (sequence or mapping)
     * @param value JsonStructure ({@link javax.json.JsonObject}
     *              or {@link javax.json.JsonArray})
     * @return Builder
     */
    default YamlMappingBuilder add(
        final YamlNode key, final JsonStructure value
    ) {
        final YamlNode node;
        if(value instanceof JsonObject) {
            node = new JsonYamlMapping((JsonObject) value);
        } else {
            node = new JsonYamlSequence((JsonArray) value);
        }
        return this.add(key, node);
    }

    /**
     * Add a pair to the mapping.
     * @param key String
     * @param value JsonStructure ({@link javax.json.JsonObject}
     *              or {@link javax.json.JsonArray})
     * @return Builder
     */
    default YamlMappingBuilder add(
        final String key, final JsonStructure value
    ) {
        final YamlNode node;
        if(value instanceof JsonObject) {
            node = new JsonYamlMapping((JsonObject) value);
        } else {
            node = new JsonYamlSequence((JsonArray) value);
        }
        return this.add(key, node);
    }

    /**
     * Build the YamlMapping.
     * @return Built YamlMapping.
     */
    default YamlMapping build() {
        return this.build("");
    }
    
    /**
     * Build the YamlMapping.
     * @param comment The multiple line comment on top of the YamlMapping.
     * @return Built YamlMapping.
     */
    default YamlMapping build(final Collection<String> comment) {
        return this.build(String.join(System.lineSeparator(), comment));
    }

    /**
     * Build the YamlMapping.
     * @param comment Comment on top of the YamlMapping.
     * @return Built YamlMapping.
     */
    YamlMapping build(final String comment);
}
