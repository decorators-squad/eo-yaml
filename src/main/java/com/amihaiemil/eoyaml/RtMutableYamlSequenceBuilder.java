/**
 * Copyright (c) 2016-2022, Mihai Emil Andronache
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * YamlSequenceBuilder implementation. "Rt" stands for "Runtime". This class is
 * mutable and thread-safe.
 * @author Lucas Hix (hixlucas@gmail.com)
 * @version $Id$
 * @since 6.0.4
 */
public class RtMutableYamlSequenceBuilder implements YamlSequenceBuilder {

	/**
	 * The nodes.
	 */
	private final List<YamlNode> nodes;

	/**
	 * Default constructor.
	 */
	RtMutableYamlSequenceBuilder() {
		this(new ArrayList<>());
	}

	/**
	 * Constructor.
	 * @param nodes Nodes used in building the YamlSequence
	 */
	RtMutableYamlSequenceBuilder(final List<YamlNode> nodes) {
		this.nodes = Collections.synchronizedList(nodes);
	}

	@Override
	public YamlSequenceBuilder add(String value) {
		return this.add(new PlainStringScalar(value));
	}

	@Override
	public YamlSequenceBuilder add(String value, String inlineComment) {
		return this.add(new PlainStringScalar(value, inlineComment));
	}

	@Override
	public YamlSequenceBuilder add(YamlNode node) {
		nodes.add(node);
		return this;
	}

	@Override
	public YamlSequenceBuilder remove(String value) {
		return this.remove(new PlainStringScalar(value));
	}

	@Override
	public YamlSequenceBuilder remove(YamlNode node) {
		nodes.remove(node);
		return this;
	}

	@Override
	public Iterator<YamlNode> iterator() {
		synchronized (nodes) {
			return nodes.iterator();
		}
	}

	@Override
	public Stream<YamlNode> stream() {
		synchronized (nodes) {
			return nodes.stream();
		}
	}

	@Override
	public YamlSequence build(String comment) {
		YamlSequence sequence = new RtYamlSequence(this.nodes, comment);
		if (this.nodes.isEmpty()) {
			sequence = new EmptyYamlSequence(sequence);
		}
		return sequence;
	}

}
