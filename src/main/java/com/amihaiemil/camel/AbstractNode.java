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

import java.util.Collection;

/**
 * YAML node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @see http://yaml.org/spec/1.2/spec.html#node//
 * @todo #7:1h/DEV Implement and unit test Sequence node.
 *  See specification: http://yaml.org/spec/1.2/spec.html#sequence//
 * @todo #7:1h/DEV Implement and unit test Mapping node.
 *  See specification: http://yaml.org/spec/1.2/spec.html#mapping//
 */
abstract class AbstractNode {

    /**
     * Parents of this node.
     */
    private Collection<AbstractNode> parents;

    /**
     * Ctor.
     * @param parents Given parents
     */
    AbstractNode(final Collection<AbstractNode> parents) {
        if(parents.isEmpty()) {
            throw new IllegalStateException(
                "A YAML graph cannot have orphaned nodes"
            );
        }
        this.parents = parents;
    }
    
    /**
     * Fetch the parent nodes of this node.
     * @return Collection of {@link AbstractNode}
     */
    public Collection<AbstractNode> parents() {
        return this.parents;
    }

    /**
     * Fetch the child nodes of this node.
     * @return Collection of {@link AbstractNode}
     */
    public abstract Collection<AbstractNode> children();

}
