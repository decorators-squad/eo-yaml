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
package com.amihaiemil.eoyaml;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * YamlMapping read from somewhere.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class ReadYamlMapping extends ComparableYamlMapping {

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
        final OrderedYamlLines ordered = new OrderedYamlLines(this.lines);
        for (final YamlLine line : ordered) {
            final String trimmed = line.trimmed();
            if("?".equals(trimmed) || ":".equals(trimmed)) {
                continue;
            } else {
                if(trimmed.endsWith(":")) {
                    kids.add(ordered.nested(line.number()).toYamlNode(line));
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
        return kids;
    }

    @Override
    public YamlMapping yamlMapping(final String key) {
        return (YamlMapping) this.nodeValue(key, true);
    }

    @Override
    public YamlMapping yamlMapping(final YamlNode key) {
        return (YamlMapping) this.nodeValue(key, true);
    }

    @Override
    public YamlSequence yamlSequence(final String key) {
        return (YamlSequence) this.nodeValue(key, false);
    }

    @Override
    public YamlSequence yamlSequence(final YamlNode key) {
        return (YamlSequence) this.nodeValue(key, false);
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
        String value = null;
        boolean found = false;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if("?".equals(trimmed)) {
                final YamlNode keyNode = this.lines.nested(line.number())
                        .toYamlNode(line);
                if(keyNode.equals(key)) {
                    found = true;
                    continue;
                }
            }
            if(found && trimmed.startsWith(":")) {
                value = trimmed.substring(trimmed.indexOf(":") + 1).trim();
                break;
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return this.indent(0);
    }

    @Override
    public String indent(final int indentation) {
        return new OrderedYamlLines(this.lines).indent(indentation);
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

    /**
     * The YamlNode value associated with a String key.
     * @param key YamlNode key.
     * @param map Is the value a map or a sequence?
     * @return YamlNode.
     */
    private YamlNode nodeValue(final YamlNode key, final boolean map) {
        YamlNode value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if("?".equals(trimmed)) {
                final AbstractYamlLines keyLines = this.lines.nested(
                    line.number()
                );
                final YamlNode keyNode = keyLines.toYamlNode(line);
                if(keyNode.equals(key)) {
                    int colonLine = line.number() + keyLines.count() + 1;
                    if (map) {
                        value = new ReadYamlMapping(
                            this.lines.nested(colonLine)
                        );
                    } else {
                        value = new ReadYamlSequence(
                            this.lines.nested(colonLine)
                        );
                    }
                }
            }
        }
        return value;
    }

    @Override
    public Set<YamlNode> keys() {
        final Set<YamlNode> keys = new HashSet<>();
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(":".equals(trimmed)) {
                continue;
            } else if ("?".equals(trimmed)) {
                keys.add(this.lines.nested(line.number()).toYamlNode(line));
            } else {
                final String[] parts = trimmed.split(":");
                if(parts.length < 2 && !trimmed.endsWith(":")) {
                    throw new IllegalStateException(
                        "Expected ':' on line " + line.number()
                    );
                } else {
                    keys.add(
                        new Scalar(
                            trimmed.substring(0, trimmed.indexOf(":")).trim()
                        )
                    );
                }
            }
        }
        return keys;
    }
}
