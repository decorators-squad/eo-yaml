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

/**
 * Default implementation of {@link YamlLine}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @todo #52:30min Right now, at every call of trimmed() and indentation()
 *  the values are calculated. This isn't efficient, so we need a decorator
 *  to cache these values. Let's name it CachedYamlLine. It should be used
 *  like this: YamlLine line = new CachedYamlLine(new RtYamlLine(...));
 * @todo #52:30min Method indentation() also checks that the value is multiple
 *  of 2. We should extract this in a decorator called WellIndentedLine which
 *  will also check if the intendetaion is correct relative to the previous
 *  line. It would be used like this: <br>
 *  YamlLine line = new WellINdentedLine(line, previousLine).
 */
final class RtYamlLine implements YamlLine {

    /**
     * Content.
     */
    private String value;

    /**
     * Line nr.
     */
    private int number;

    /**
     * Ctor.
     * @param value Contents of this line.
     * @param number Number of the line.
     */
    RtYamlLine(final String value, final int number) {
        this.value = value;
        this.number = number;
    }

    @Override
    public String trimmed() {
        return this.value.trim();
    }

    @Override
    public int number() {
        return this.number;
    }

    @Override
    public int indentation() {
        int indentation = 0;
        int index = 0;
        while (index < this.value.length()) {
            if (this.value.charAt(index) == ' ') {
                indentation++;
            }
            index++;
        }
        if (indentation % 2 != 0) {
            throw new IllegalStateException(
                "Indentation of line " + this.number + " is not correct. "
                + "It is " + indentation + " and it should be a multiple of 2!"
            );
        }
        return indentation;
    }

}
