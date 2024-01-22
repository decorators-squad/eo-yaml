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

import com.amihaiemil.eoyaml.exceptions.YamlPrintException;
import com.amihaiemil.eoyaml.exceptions.YamlReadingException;
import java.io.StringWriter;

/**
 * Base YAML Node. This is the first class in the hierarchy
 * for any kind of YAML node.
 *
 * So far, the purpose of this base class is to hide methods that should
 * be applicable to all types of YamlNode and which we do not want
 * to make public on the YamlNode interface.
 *
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.0.0
 */
abstract class BaseYamlNode implements YamlNode {

    @Override
    public final Scalar asScalar()
        throws YamlReadingException, ClassCastException {
        return this.asClass(Scalar.class, Node.SCALAR);
    }

    @Override
    public final YamlMapping asMapping()
        throws YamlReadingException, ClassCastException {
        return this.asClass(YamlMapping.class, Node.MAPPING);
    }

    @Override
    public final YamlSequence asSequence()
        throws YamlReadingException, ClassCastException {
        return this.asClass(YamlSequence.class, Node.SEQUENCE);
    }

    @Override
    public final YamlStream asStream()
        throws YamlReadingException, ClassCastException {
        return this.asClass(YamlStream.class, Node.STREAM);
    }

    @Override
    public final <T extends YamlNode> T asClass(final Class<T> clazz,
                                                final Node type)
        throws YamlReadingException, ClassCastException {
        if (this.type() != type) {
            throw new YamlReadingException(
                "The YamlNode is not a " + clazz.getSimpleName() + '!');
        }
        return clazz.cast(this);
    }

    /**
     * Print this YamlNode using a StringWriter to create its
     * String representation.
     * @return String print of this YamlNode.
     * @throws YamlPrintException If there is any I/O problem
     *  when printing the YAML.
     *
     */
    @Override
    public final String toString() {
        final String lineSeparator = System.lineSeparator();
        final YamlVisitor<String> visitor = new YamlPrintVisitor(lineSeparator);
        String toString = "";
        if(this.type().equals(Node.SCALAR)) {
            toString += "---" + lineSeparator;
            toString += printPossibleComment(this, lineSeparator);
            toString += this.accept(visitor);
            toString += lineSeparator + "...";
        } else {
            toString += printPossibleComment(this, lineSeparator);
            if(!toString.isEmpty()) {
                toString += "---" + lineSeparator;
            }
            toString += this.accept(visitor);
        }
        return toString;
    }

    /**
     * Print a comment. Make sure to split the lines if there are more
     * lines separated by NewLine and also add a '# ' in front of each
     * line.
     * @param node Node containing the Comment.
     * @param lineSeparator Line separator.
     * @return Printed comment.
     */
    private String printPossibleComment(
        final YamlNode node, final String lineSeparator
    ) {
        final StringWriter writer = new StringWriter();
        if(node != null && node.comment() != null) {
            final Comment tmpComment;
            if(node.comment() instanceof ScalarComment) {
                tmpComment = ((ScalarComment) node.comment()).above();
            } else {
                tmpComment = node.comment();
            }
            final String com = tmpComment.value();
            if (com.trim().length() != 0) {
                String[] lines = com.split(lineSeparator);
                for (final String line : lines) {
                    writer
                        .append("# ")
                        .append(line)
                        .append(lineSeparator);
                }
            }
        }
        return writer.toString();
    }

}
