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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation for {@link YamlScalarBuilder}. "Rt" stands for "Runtime.
 * This class is immutable and thread-safe.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.2.0
 * @todo #221:30min Finish the implementation of the methods in this interface.
 *  Figure out how to implement literal and built block scalars, also in the
 *  the context of indenting/printing a bigger YAML node, as well as printing
 *  only that single scalar. Maybe we will need a class called YamlPrinter,
 *  that looks for the type of the node to print.
 */
final class RtYamlScalarBuilder implements YamlScalarBuilder {

    /**
     * Added lines.
     */
    private final List<String> lines;

    /**
     * Default ctor.
     */
    RtYamlScalarBuilder() {
        this(new LinkedList<>());
    }

    /**
     * Constructor.
     * @param lines String lines of the Scalar.
     */
    RtYamlScalarBuilder(final List<String> lines) {
        this.lines = lines;
    }

    @Override
    public YamlScalarBuilder addLine(final String value) {
        final List<String> all = new LinkedList<>();
        all.addAll(this.lines);
        all.add(value);
        return new RtYamlScalarBuilder(all);
    }

    @Override
    public Scalar buildPlainScalar() {
        final String plain = this.lines.stream().map(
            line -> line.replaceAll(System.lineSeparator(), " ")
        ).collect(Collectors.joining(" "));
        return new PlainStringScalar(plain);
    }

    @Override
    public Scalar buildFoldedBlockScalar() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Scalar buildLiteralBlockScalar() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}
