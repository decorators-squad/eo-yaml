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

/**
 * A plain scalar value read from a read mapping or a read sequence.
 * @author Mihai Andronace (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.3
 */
final class ReadPlainScalarValue extends ComparableScalar {

    /**
     * Line where the plain scalar value is supposed to be found.
     * The Scalar can be either after the ":" character, if this
     * line is from a mapping, or after the "-" character, if
     * this line is from a sequence.
     */
    private YamlLine line;

    /**
     * Constructor.
     * @param line Read YamlLine.
     */
    ReadPlainScalarValue(final YamlLine line) {
        this.line = line;
    }

    @Override
    public String value() {
        final String value;
        final String trimmed = this.line.trimmed();
        if(trimmed.startsWith("-") && trimmed.length() > 1) {
            value = trimmed.substring(trimmed.indexOf('-')+1).trim();
        } else if(trimmed.contains(":") && !trimmed.endsWith(":")) {
            value = trimmed.substring(trimmed.indexOf(":") + 1).trim();
        } else {
            value = trimmed;
        }
        return this.unescape(value);
    }

    /**
     * Remove the possible escaping quotes or apostrophes surrounding the
     * given value.
     * @param value The value to unescape.
     * @return The value without quotes or apostrophes.
     */
    private String unescape(final String value) {
        final String unescaped;
        if(value == null || value.length()<=2) {
            unescaped = value;
        } else {
            if (value.startsWith("\"") && value.endsWith("\"")) {
                unescaped = value.substring(1, value.length() - 1);
            } else if (value.startsWith("'") && value.endsWith("'")) {
                unescaped = value.substring(1, value.length() - 1);
            } else {
                unescaped = value;
            }
        }
        return unescaped;
    }
}
