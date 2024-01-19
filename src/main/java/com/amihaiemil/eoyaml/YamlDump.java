/**
 * Copyright (c) 2016-2023, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
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
 * In YAML, "dumping" means representing the state of an Object as YAML.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.3.3
 */
public interface YamlDump {

    /**
     * Dump an Object, represent it as YAML.
     * Generally, if the Object is a Collection or on Array, the resulting
     * YamlNode will be a YamlSequence.<br><br>
     * If the Object is a Map or other kind of Object, the resulting
     * YamlNode will be a YamlSequence.<br><br>
     * If the Object is a String, LocalDate, LocaDateTime or a primitive,
     * the resulting YamlNode will be a plain Scalar.
     * @param comment String comment.
     * @return YAML Representation.
     */
    YamlNode dump(final String comment);

    /**
     * Dump an Object, represent it as YAML.
     * Generally, if the Object is a Collection or on Array, the resulting
     * YamlNode will be a YamlSequence.<br><br>
     * If the Object is a Map or other kind of Object, the resulting
     * YamlNode will be a YamlSequence.<br><br>
     * If the Object is a String, LocalDate, LocaDateTime or a primitive,
     * the resulting YamlNode will be a plain Scalar.
     * @return YAML Representation.
     */
    default YamlNode dump() {
        return this.dump("");
    }

    /**
     * Convenience method, equivalent to calling the
     * dump(...) method and casting the YamlNode to YamlMapping.
     * @return YamlMapping.
     */
    default YamlMapping dumpMapping() {
        return this.dumpMapping("");
    }

    /**
     * Convenience method, equivalent to calling the
     * dump(...) method and casting the YamlNode to YamlSequence.
     * @return YamlSequence.
     */
    default YamlSequence dumpSequence() {
        return this.dumpSequence("");
    }

    /**
     * Convenience method, equivalent to calling the
     * dump(...) method and casting the YamlNode to Scalar.
     * @return Scalar.
     */
    default Scalar dumpScalar() {
        return this.dumpScalar("");
    }

    /**
     * Convenience method, equivalent to calling the
     * dump(...) method and casting the YamlNode to YamlMapping.
     * @param comment String comment referring to the mapping.
     * @return YamlMapping.
     */
    default YamlMapping dumpMapping(final String comment) {
        return (YamlMapping) this.dump(comment);
    }

    /**
     * Convenience method, equivalent to calling the
     * dump(...) method and casting the YamlNode to YamlSequence.
     * @param comment String comment referring to the sequence.
     * @return YamlSequence.
     */
    default YamlSequence dumpSequence(final String comment) {
        return (YamlSequence) this.dump(comment);
    }

    /**
     * Convenience method, equivalent to calling the
     * dump(...) method and casting the YamlNode to Scalar.
     * @param comment String comment referring to the scalar.
     * @return Scalar.
     */
    default Scalar dumpScalar(final String comment) {
        return (Scalar) this.dump(comment);
    }

}
