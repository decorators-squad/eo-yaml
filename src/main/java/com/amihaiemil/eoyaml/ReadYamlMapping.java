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

import com.amihaiemil.eoyaml.exceptions.YamlReadingException;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.amihaiemil.eoyaml.YamlLine.UNKNOWN_LINE_NUMBER;

/**
 * YamlMapping read from somewhere. YAML directives and
 * document start/end markers are ignored. This is assumed
 * to be a plain YAML mapping.
 * @checkstyle CyclomaticComplexity (300 lines)
 * @checkstyle ExecutableStatementCount (300 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @todo #600:60min Make sure to also read flow-style YamlNodes as such from
 *  a read block YamlMapping.
 */
final class ReadYamlMapping extends BaseYamlMapping {

    /**
     * Regex for a key in a mapping.
     */
    private static final Pattern KEY_PATTERN = Pattern.compile(
        "^-?\\s*((?<key>[^:'\"]+)|\"(?<keyQ>.+)\"|'(?<keySQ>.+)'):(|\\s.*)$"
    );

    /**
     * Yaml line just previous to the one where this mapping starts. E.g.
     * <pre>
     * 0  mapping:
     * 1    key1: elem1
     * 2    key2: elem2
     * </pre>
     * In the above example the mapping consists of keys key1 and key2, while
     * "previous" is line 0. If the mapping starts at the root, then line
     * "previous" is {@link com.amihaiemil.eoyaml.YamlLine.NullYamlLine}; E.g.
     * <pre>
     * 0  key1: elem1
     * 1  key2: elem2
     * </pre>
     */
    private YamlLine previous;

    /**
     * All the lines of this YAML document.
     */
    private final AllYamlLines all;

    /**
     * Only the significant lines of this YamlMapping.
     */
    private final YamlLines significant;

    /**
     * Where to stop looking for comments.
     */
    private final int commentStop;

    /**
     * Ctor.
     * @param lines Given lines.
     */
    ReadYamlMapping(final AllYamlLines lines) {
        this(UNKNOWN_LINE_NUMBER, new YamlLine.NullYamlLine(), lines);
    }

    /**
     * Ctor.
     * @checkstyle ParameterNumber (100 lines)
     * @param commentStop Where to
     * @param previous Line just before the start of this mapping.
     * @param lines Given lines.
     */
    ReadYamlMapping(
        final int commentStop,
        final YamlLine previous,
        final AllYamlLines lines
    ) {
        this.commentStop = commentStop;
        this.previous = previous;
        this.all = lines;
        this.significant = new SameIndentationLevel(
            new WellIndented(
                new Skip(
                    lines,
                    line -> line.number() <= previous.number(),
                    line -> line.trimmed().startsWith("#"),
                    line -> line.trimmed().startsWith("---"),
                    line -> line.trimmed().startsWith("..."),
                    line -> line.trimmed().startsWith("%"),
                    line -> line.trimmed().startsWith("!!")
                ),
                Boolean.FALSE
            ),
            true
        );
    }

    @Override
    public Set<YamlNode> keys() {
        final Set<YamlNode> keys = new LinkedHashSet<>();
        YamlLine dashKey = null;
        for (final YamlLine line : this.significant) {
            final String trimmed = line.trimmed();
            if(dashKey != null && this.isDashMappingEntry(line)) {
                break;
            } else if ("?".equals(trimmed)) {
                keys.add(this.significant.nextYamlNode(line));
            } else if(trimmed.indexOf(":") > 0){
                final Matcher matcher = KEY_PATTERN.matcher(trimmed);
                if (matcher.matches()) {
                    if(trimmed.startsWith("-")) {
                        dashKey = line;
                        if(keys.size() > 0) {
                            dashKey = null;
                            continue;
                        }
                    }
                    if(dashKey != null
                        && line.indentation() == dashKey.indentation()
                        && line.number() != dashKey.number()) {
                        continue;
                    }
                    //@checkstyle NestedIfDepth (50 lines)
                    final String key = matcher.group("key");
                    if (key != null && !key.isEmpty()) {
                        keys.add(new PlainStringScalar(key));
                    } else {
                        final String keyQ = matcher.group("keyQ");
                        if (keyQ != null && !keyQ.isEmpty()) {
                            keys.add(new PlainStringScalar(keyQ));
                        } else {
                            final String keySQ = matcher.group("keySQ");
                            if (keySQ != null && !keySQ.isEmpty()) {
                                keys.add(new PlainStringScalar(keySQ));
                            }
                        }
                    }
                }
            }
        }
        return keys;
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

    @Override
    public Comment comment() {
        boolean documentComment = this.previous.number() < 0;
        //@checkstyle LineLength (50 lines)
        return new ReadComment(
            new Backwards(
                new FirstCommentFound(
                    new Backwards(
                        new Skip(
                            this.all,
                            line -> {
                                final boolean skip;
                                if(documentComment) {
                                    if(this.significant.iterator().hasNext()) {
                                        skip = line.number() >= this.significant
                                                .iterator().next().number();
                                    } else {
                                        skip = false;
                                    }
                                } else {
                                    skip = line.number() >= commentStop;
                                }
                                return skip;
                            },
                            line -> line.trimmed().startsWith("..."),
                            line -> line.trimmed().startsWith("%"),
                            line -> line.trimmed().startsWith("!!")
                        )
                    ),
                    documentComment
                )
            ),
            this
        );
    }

    /**
     * The YamlNode value associated with a String (scalar) key.
     * @param key String key.
     * @return YamlNode.
     * @checkstyle ReturnCount (50 lines)
     * @checkstyle LineLength (30 lines)
     */
    private YamlNode valueOfStringKey(final String key) {
        YamlNode value = null;
        final String[] keys = new String[] {
            key,
            "\"" + key + "\"",
            "'" + key + "'",
        };
        for(final String tryKey : keys) {
            for (final YamlLine line : this.significant) {
                final String trimmed = line.trimmed();
                final String relaxedKey = relaxed(tryKey);
                if(trimmed.matches("^-?[ ]*" + Pattern.quote(relaxedKey) + ":")
                    || trimmed.matches("^" + Pattern.quote(relaxedKey) + ":[ ]*>$")
                    || trimmed.matches("^" + Pattern.quote(relaxedKey) + ":[ ]*\\|[+-]?$")
                ) {
                    value = this.significant.nextYamlNode(line);
                } else if(trimmed.matches(Pattern.quote(relaxedKey) + ":[ ]*\\{}")) {
                    value = new EmptyYamlMapping(new ReadYamlMapping(
                        line.number(),
                        this.all.line(line.number()),
                        this.all
                    ));
                } else if(trimmed.matches(Pattern.quote(relaxedKey) + ":[ ]*\\[]")) {
                    value = new EmptyYamlSequence(new ReadYamlSequence(
                            this.all.line(line.number()),
                            this.all
                    ));
                } else if((trimmed.startsWith(tryKey + ":")
                        || trimmed.startsWith("- " + tryKey + ":"))
                        && trimmed.length() > 1
                ) {
                    value = new ReadPlainScalar(this.all, line);
                }

                if(value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * Escape [ and ] for regex matching when key starts with "[ and ends with
     * "].
     * @param key Provided key.
     * @return Relaxed key for pattern matching.
     */
    private String relaxed(final String key){
        final String regexEscape;
        if(key.startsWith("\"[") && key.endsWith("]\"")){
            final int openIndex = 1;
            final int closedIndex = key.length() - 1;
            final StringBuilder builder = new StringBuilder(key);
            builder.insert(openIndex, "\\\\");
            builder.insert(closedIndex, "\\\\");
            regexEscape = builder.toString();
        }else {
            regexEscape = key;
        }
        return regexEscape;
    }

    /**
     * The YamlNode value associated with a YamlNode key
     * (a "complex" key starting with '?').
     * @param key YamlNode key.
     * @return YamlNode.
     */
    private YamlNode valueOfNodeKey(final YamlNode key) {
        YamlNode value = null;
        final Iterator<YamlLine> linesIt = this.significant.iterator();
        while(linesIt.hasNext()) {
            final YamlLine line = linesIt.next();
            final String trimmed = line.trimmed();
            if("?".equals(trimmed)) {
                final YamlNode keyNode = this.significant.nextYamlNode(line);
                if(keyNode.equals(key)) {
                    final YamlLine colonLine = linesIt.next();
                    if(":".equals(colonLine.trimmed())
                        || colonLine.trimmed().matches("^\\:[ ]*\\>$")
                        || colonLine.trimmed().matches("^\\:[ ]*\\|$")
                    ) {
                        value = this.significant.nextYamlNode(colonLine);
                    } else if(colonLine.trimmed().startsWith(":")
                        && (colonLine.trimmed().length() > 1)
                    ){
                        value = new ReadPlainScalar(this.all, colonLine);
                    } else {
                        throw new YamlReadingException(
                            "No value found for existing complex key: "
                          + System.lineSeparator()
                          + key.toString()
                        );
                    }
                    break;
                }
            }
        }
        return value;
    }

    /**
     * Returns true if the line is a dash mapping entry.
     * @param line Line.
     * @return True of false.
     */
    private boolean isDashMappingEntry(final YamlLine line) {
        final String trim = line.trimmed();
        return trim.startsWith("-") && KEY_PATTERN.matcher(trim).matches();
    }
}
