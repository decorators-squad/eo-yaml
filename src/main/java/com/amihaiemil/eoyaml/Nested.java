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
 * Nested Yaml types.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.2
 */
final class Nested {

    /**
     * Hidden ctor.
     */
    private Nested() {}

    /**
     * If this is the last char on a line, it means a Yaml
     * node should be nested bellow.
     */
    static final String YAML = ":";

    /**
     * If this is the last char on a line, it means that
     * 1) a Yaml sequence should be wrapped bellow (looks the same as a normal
     *    one, but the char '-' is ommitted from the beginning of its lines).
     * or
     * 2) a Yaml node is bellow it (an element from the current sequence).
     */
    static final String SEQUENCE = "-";

    /**
     * If this is the last char on a line, it means a pointed wrapped scalar
     * should be nested bellow (see Example 2.15 from YAML spec 1.2).
     */
    static final String POINTED_SCALAR = ">";

    /**
     * If this is the last char on a line, it means a piped wrapped scalar
     * should be nested bellow (all newlines are significant, and
     * taken into account).
     */
    static final String PIPED_SCALAR = "|";

    /**
     * If this is the last char on a line, it means a complex key
     * (mapping or sequence) should be nested bellow.
     */
    static final String KEY_YAML = "?";
}
