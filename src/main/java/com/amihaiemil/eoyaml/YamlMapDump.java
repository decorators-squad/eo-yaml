/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
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

import java.util.Map;
import java.util.Set;

/**
 * A Map represented as YamlNode.
 * @deprecated Will be removed in one of the future versions. You can dump any
 *  Java Object by starting from the method Yaml.createYamlDump(...).
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
@Deprecated
public final class YamlMapDump extends AbstractYamlDump {

    /**
     * Map<Object, Object> to dump.
     */
    private Map<Object, Object> map;

    /**
     * Ctor.
     * @param map Map to dump
     */
    public YamlMapDump(final Map<Object, Object> map) {
        this.map = map;
    }

    @Override
    public YamlMapping represent() {
        YamlMappingBuilder mapBuilder = new RtYamlMappingBuilder();
        Set<Map.Entry<Object, Object>> entries = this.map.entrySet();
        for (final Map.Entry<Object, Object> entry : entries) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (super.leafProperty(key) && super.leafProperty(value)) {
                mapBuilder = mapBuilder.add(
                    key.toString(),
                    value.toString()
                );
            } else if (super.leafProperty(key)) {
                mapBuilder = mapBuilder.add(
                    key.toString(),
                    new YamlObjectDump(value).represent()
                );
            } else if (super.leafProperty(value)) {
                mapBuilder = mapBuilder.add(
                    new YamlObjectDump(key).represent(),
                    value.toString()
                );
            } else {
                mapBuilder = mapBuilder.add(
                    new YamlObjectDump(key).represent(),
                    new YamlObjectDump(value).represent()
                );
            }
        }
        return mapBuilder.build();
    }

}
