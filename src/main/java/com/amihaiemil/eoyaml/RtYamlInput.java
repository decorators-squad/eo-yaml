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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation for {@link YamlInput}. "Rt" stands for "Runtime".
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class RtYamlInput implements YamlInput {

    /**
     * Source of the input.
     */
    private InputStream source;

    /**
     * Ctor.
     * @param source Given source.
     */
    RtYamlInput(final InputStream source) {
        this.source = source;
    }

    @Override
    public YamlMapping readYamlMapping() throws IOException {
        return new ReadYamlMapping(this.readInput());
    }

    @Override
    public YamlSequence readYamlSequence() throws IOException {
        return new ReadYamlSequence(this.readInput());
    }

    @Override
    public YamlStream readYamlStream() throws IOException {
        return new ReadYamlStream(this.readInput());
    }

    @Override
    public Scalar readPlainScalar() throws IOException {
        final ReadPlainScalarValue read;
        final Iterator<YamlLine> iterator = new NoDirectivesOrMarkers(
            this.readInput()
        ).iterator();
        if(!iterator.hasNext()) {
            read = new ReadPlainScalarValue(new YamlLine.NullYamlLine());
        } else {
            read = new ReadPlainScalarValue(iterator.next());
        }
        return read;
    }

    @Override
    public Scalar readFoldedBlockScalar() throws IOException {
        return new ReadFoldedBlockScalar(
            new NoScalarMarkers(
                new NoDirectivesOrMarkers(
                    this.readInput()
                )
            )
        );
    }

    @Override
    public Scalar readLiteralBlockScalar() throws IOException {
        return new ReadLiteralBlockScalar(
            new NoScalarMarkers(
                new NoDirectivesOrMarkers(
                    this.readInput()
                )
            )
        );
    }

    /**
     * Read the input's lines.
     * @return All read YamlLines
     * @throws IOException If something goes wrong while reading the input.
     */
    private AllYamlLines readInput() throws IOException {
        final List<YamlLine> lines = new ArrayList<>();
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.source)
            )
        ) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                final YamlLine current = new NoCommentsYamlLine(
                    new RtYamlLine(line, number)
                );
                if(!current.trimmed().isEmpty()) {
                    lines.add(new CachedYamlLine(current));
                }
                number++;
            }
        }
        return new AllYamlLines(lines);
    }
}
