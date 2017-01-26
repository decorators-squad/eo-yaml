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
 * A Yaml serializer.
 * @author Sherif Waly (sherifwaly95@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * 
 * @todo #30:30m/DEV This interface should have 3 implementor classes:
 * YamlObjectSerializer, YamlMapSerializer and YamlCollectionSerializer.
 * Each of these classes will encapsulate and serialize the
 * mentioned types (i.e. Object, Map<Object, Object> and Collection<Object>).
 * The return type or method ``serialize()`` should be overridden with the 
 * proper subtype (e.g. YamlObjectSerializer will have the method
 * ``YamlMapping serialize() {...}``
 * 
 * @todo #30:30m/TEST This interface should be tested by 3 unit tests: 
 * objectYamlSerialize(), mapYamlSerialize() and collectionYamlSerialize(). 
 * The tests should check that the returned YamlNode is the proper subtype
 * of YamlNode (e.g. YamlObjectSerializer in objectYamlSerialize() test should
 * return a proper YamlMapping)
 * 
 * @todo #30:30m/DEV New interface which is YamlPresenter should be implemented
 * to convert Yaml node to Presentation Stream. 
 * 
 */
public interface YamlSerializer {
    
    /**
     * Serialize one of Map<Object, Object>, Object or Collection<Object>.
     * @return Yaml node
     */
    YamlNode serialize();
}
