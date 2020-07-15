/**
 * Copyright (c) 2020, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.eoyaml;

import java.util.Collection;

/**
 * A read YamlSequence in flow format (elements between square brackets).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 5.2.0
 */
final class ReadFlowSequence extends BaseYamlSequence {

    /**
     * Line where this flow sequence starts.
     */
    private final YamlLine start;

    /**
     * All lines of the YAML document.
     */
    private final AllYamlLines all;

    /**
     * Only the significant lines of this sequence.
     */
    private final YamlLines significant;

    /**
     * Ctor.
     * @param start Line where this flow sequence starts.
     * @param lines All lines of the YAML document.
     */
    ReadFlowSequence(final YamlLine start, final AllYamlLines lines) {
        this.start = start;
        this.all = lines;
        this.significant = new FoldedFlowLines(
            new Skip(
                lines,
                line -> line.number() < start.number(),
                line -> line.trimmed().startsWith("#"),
                line -> line.trimmed().startsWith("---"),
                line -> line.trimmed().startsWith("..."),
                line -> line.trimmed().startsWith("%"),
                line -> line.trimmed().startsWith("!!")
            ),
            '[',
            ']'
         );
    }

    @Override
    public Collection<YamlNode> values() {
        return null;
    }

    @Override
    public Comment comment() {
        return new BuiltComment(this, "");
    }
}
