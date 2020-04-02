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

import com.amihaiemil.eoyaml.exceptions.YamlReadingException;
import java.util.*;

/**
 * YamlMapping read from somewhere. YAML directives and
 * document start/end markers are ignored. This is assumed
 * to be a plain YAML mapping.
 * @checkstyle CyclomaticComplexity (300 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class ReadYamlMapping extends BaseYamlMapping {

    /**
     * Lines read.
     */
    private YamlLines lines;

    /**
     * Ctor.
     * @param lines Given lines.
     */
    ReadYamlMapping(final AllYamlLines lines) {
        this.lines = new SameIndentationLevel(
            new WellIndented(
                new Skip(
                    lines,
                    line -> line.trimmed().startsWith("---"),
                    line -> line.trimmed().startsWith("..."),
                    line -> line.trimmed().startsWith("%"),
                    line -> line.trimmed().startsWith("!!")
                )
            )
        );
    }

    @Override
    public Set<YamlNode> keys() {
        final Set<YamlNode> keys = new LinkedHashSet<>();
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(trimmed.startsWith(":")) {
                continue;
            } else if ("?".equals(trimmed)) {
                keys.add(this.lines.nested(line.number()).toYamlNode(line));
            } else {
                if(!trimmed.contains(":")) {
                    throw new YamlReadingException(
                        "Expected scalar key on line " 
                        + (line.number() + 1) + "."
                        + " The line should have the format " 
                        + "'key: value' or 'key:'. "
                        + "Instead, the line is: "
                        + "[" + line.trimmed() + "]."
                    );
                }
                final String key = trimmed.substring(
                        0, trimmed.indexOf(":")).trim();
                if(!key.isEmpty()) {
                    keys.add(new PlainStringScalar(key));
                }
            }
        }
        return keys;
    }

    @Override
    public Collection<YamlNode> values() {
        final List<YamlNode> values = new LinkedList<>();
        for(final YamlNode key : this.keys()) {
            values.add(this.value(key));
        }
        return values;
    }

    @Override
    public YamlNode value(final YamlNode key) {
        final YamlNode value;
        if(key instanceof Scalar) {
            value = this.valueOfStringKey(((Scalar) key).value());
        } else {
            value = this.valueOfNodeKey(key);
        }
        return value;
    }

    /**
     * Return this mapping's comment.
     * @todo #261:30min Continue with the support for comments by implementing
     *  the reading of comments in the case of a YamlMapping. Comments for
     *  key:value pairs are already implemented in BaseYamlMapping.
     * @return Comment.
     */
    @Override
    public Comment comment() {
        final Comment empty = new BuiltComment(this, "");
        return empty;
    }

    @Override
    public YamlMapping yamlMapping(final YamlNode key) {
        final YamlMapping found;
        final YamlNode value = this.value(key);
        if(value instanceof ReadYamlMapping) {
            found = (ReadYamlMapping) value;
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public YamlSequence yamlSequence(final YamlNode key) {
        final YamlSequence found;
        final YamlNode value = this.value(key);
        if(value instanceof ReadYamlSequence) {
            found = (ReadYamlSequence) value;
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public String string(final YamlNode key) {
        final String found;
        final YamlNode value = this.value(key);
        if(value instanceof ReadPlainScalar) {
            found = ((ReadPlainScalar) value).value();
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public String foldedBlockScalar(final YamlNode key) {
        final String found;
        final YamlNode value = this.value(key);
        if(value instanceof ReadFoldedBlockScalar) {
            found = ((ReadFoldedBlockScalar) value).toString();
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public Collection<String> literalBlockScalar(final YamlNode key) {
        final Collection<String> found;
        final YamlNode value = this.value(key);
        if(value instanceof ReadLiteralBlockScalar) {
            found = Arrays.asList(
                ((ReadLiteralBlockScalar) value)
                    .value()
                    .split(System.lineSeparator())
            );
        } else {
            found = null;
        }
        return found;
    }

    /**
     * The YamlNode value associated with a String (scalar) key.
     * @param key String key.
     * @return YamlNode.
     */
    private YamlNode valueOfStringKey(final String key) {
        YamlNode value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(trimmed.endsWith(key + ":")
                || trimmed.matches("^" + key + "\\:[ ]*\\>$")
                || trimmed.matches("^" + key + "\\:[ ]*\\|$")
            ) {
                value = this.lines.nested(line.number()).toYamlNode(line);
            } else if(trimmed.startsWith(key + ":")
                && trimmed.length() > 1
            ) {
                value = new ReadPlainScalar(line);
            }
        }
        return value;
    }

    /**
     * The YamlNode value associated with a YamlNode key
     * (a "complex" key starting with '?').
     * @param key YamlNode key.
     * @return YamlNode.
     */
    private YamlNode valueOfNodeKey(final YamlNode key) {
        YamlNode value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if("?".equals(trimmed)) {
                final YamlLines keyLines = this.lines.nested(
                    line.number()
                );
                final YamlNode keyNode = keyLines.toYamlNode(line);
                if(keyNode.equals(key)) {
                    final YamlLine colonLine = this.lines.line(
                        line.number() + keyLines.lines().size() + 1
                    );
                    if(":".equals(colonLine.trimmed())
                        || colonLine.trimmed().matches("^\\:[ ]*\\>$")
                        || colonLine.trimmed().matches("^\\:[ ]*\\|$")
                    ) {
                        value = this.lines.nested(colonLine.number())
                                    .toYamlNode(colonLine);
                    } else if(colonLine.trimmed().startsWith(":")
                        && (colonLine.trimmed().length() > 1)
                    ){
                        value = new ReadPlainScalar(colonLine);
                    } else {
                        throw new YamlReadingException(
                            "No value found for existing complex key: "
                          + System.lineSeparator()
                          + ((BaseYamlNode) key).indent(0)
                        );
                    }
                    break;
                }
            }
        }
        return value;
    }
}
