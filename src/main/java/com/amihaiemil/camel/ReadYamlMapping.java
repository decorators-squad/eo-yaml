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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * YamlMapping read from somewhere.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @todo #73:30min/DEV Continue implementing and unit-testing the methods
 *  from this class one by one.
 */
final class ReadYamlMapping implements YamlMapping {

    /**
     * Lines read.
     */
    private AbstractYamlLines lines;

    /**
     * Ctor.
     * @param lines Given lines.
     */
    ReadYamlMapping(final AbstractYamlLines lines) {
        this.lines = lines;
    }

    @Override
    public Collection<YamlNode> children() {
        final List<YamlNode> kids = new LinkedList<>();
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if("?".equals(trimmed)) {
                continue;
            } else {
                if(trimmed.endsWith(":")) {
                    kids.add(this.lines.nested(line.number()).toYamlNode());
                } else {
                    final String[] parts = trimmed.split(":");
                    if(parts.length < 2) {
                        throw new IllegalStateException(
                            "Expected ':' on line " + line.number()
                        );
                    } else {
                        kids.add(
                            new Scalar(
                                trimmed.substring(
                                    trimmed.indexOf(":") + 1
                                ).trim()
                            )
                        );
                    }
                }
            }
        }
        Collections.sort(kids);
        return kids;
    }

    @Override
    public YamlMapping yamlMapping(final String key) {
        return (YamlMapping) this.nodeValue(key, true);
    }

    @Override
    public YamlMapping yamlMapping(final YamlNode key) {
        return null;
    }

    @Override
    public YamlSequence yamlSequence(final String key) {
        return (YamlSequence) this.nodeValue(key, false);
    }

    @Override
    public YamlSequence yamlSequence(final YamlNode key) {
        return null;
    }

    @Override
    public String string(final String key) {
        String value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(trimmed.startsWith(key + ":")) {
                value = trimmed.substring(trimmed.indexOf(":") + 1).trim();
            }
        }
        return value;
    }

    @Override
    public String string(final YamlNode key) {
        return null;
    }

    @Override
    public String toString() {
        return this.indent(0);
    }

    @Override
    public String indent(final int indentation) {
        return null;
    }

    @Override
    public int compareTo(final YamlNode other) {
        return 0;
    }

    /**
     * The YamlNode value associated with a String key.
     * @param key String key.
     * @param map Is the value a map or a sequence?
     * @return YamlNode.
     */
    private YamlNode nodeValue(final String key, final boolean map) {
        YamlNode value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(trimmed.startsWith(key + ":")) {
                if (map) {
                    value = new ReadYamlMapping(
                        this.lines.nested(line.number())
                    );
                } else {
                    value = new ReadYamlSequence(
                        this.lines.nested(line.number())
                    );
                }
            }
        }
        return value;
    }
}
