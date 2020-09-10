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

import java.util.Iterator;

/**
 * A comment which has been read from somewhere.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since  4.2.0
 */
final class ReadComment implements Comment {

    /**
     * Lines of this comment.
     */
    private final YamlLines lines;

    /**
     * Node to which this comment refers.
     */
    private final YamlNode node;

    /**
     * Constructor.
     * @param lines Lines of this comment.
     * @param node Node to which it refers.
     */
    ReadComment(final YamlLines lines, final YamlNode node) {
        this.lines = lines;
        this.node = node;
    }

    @Override
    public YamlNode yamlNode() {
        return this.node;
    }

    @Override
    public int number() {
        Iterator<YamlLine> iterator = lines.iterator();
        int lineNumber = UNKNOWN_LINE_NUMBER;
        if (iterator.hasNext()) {
            lineNumber = iterator.next().number();
        }
        return lineNumber;
    }

    @Override
    public String value() {
        final StringBuilder comment = new StringBuilder();
        for(final YamlLine line : this.lines) {
            comment
                .append(line.comment().trim())
                .append(System.lineSeparator());
        }
        return comment.toString().trim();
    }
}
