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

/**
 * YAML Plain Scalar from String.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#scalar//
 */
final class PlainStringScalar extends BaseScalar {

    /**
     * Comments referring to this scalar.
     */
    private final Comment comment;

    /**
     * This scalar's value.
     */
    private final String value;

    /**
     * Ctor.
     * @param value Given value for this scalar.
     */
    PlainStringScalar(final String value) {
        this(value, "");
    }

    /**
     * Ctor.
     * @param value Given value for this scalar.
     * @param inline Comment inline with the scalar (after it).
     */
    PlainStringScalar(final String value, final String inline) {
        this(value, "", inline);
    }

    /**
     * Ctor.
     * @param value Given value for this scalar.
     * @param above Comment above the scalar.
     * @param inline Comment inline with the scalar.
     */
    PlainStringScalar(
        final String value, final String above, final String inline
    ) {
        this.value = value;
        this.comment = new Concatenated(
            new BuiltComment(
                this, above
            ),
            new InlineComment(
                new BuiltComment(this, inline)
            )
        );
    }

    /**
     * Value of this scalar.
     * @return Value of type T.
     */
    @Override
    public String value() {
        final String unescaped;
        if("null".equals(this.value)) {
            unescaped = null;
        } else {
            unescaped = this.unescape(this.value);
        }
        return unescaped;
    }

    @Override
    public Comment comment() {
        return this.comment;
    }

    /**
     * Remove the possible escaping quotes or apostrophes surrounding the
     * given value.
     * @param escaped The value to unescape.
     * @return The value without quotes or apostrophes.
     */
    private String unescape(final String escaped) {
        final String unescaped;
        if(escaped == null) {
            unescaped = escaped;
        } else {
            if (escaped.startsWith("\"") && escaped.endsWith("\"")) {
                unescaped = escaped.substring(1, escaped.length() - 1);
            } else if (escaped.startsWith("'") && escaped.endsWith("'")) {
                unescaped = escaped.substring(1, escaped.length() - 1);
            } else {
                unescaped = escaped;
            }
        }
        return unescaped;
    }
}
