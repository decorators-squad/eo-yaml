package com.amihaiemil.camel;

import java.util.Collection;
import java.util.Iterator;

/**
 * YamlLines default implementation.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class RtYamlLines implements YamlLines {

	/**
	 * Yaml lines.
	 */
	private Collection<YamlLine> lines;

	/**
	 * Ctor.
	 * @param lines
	 */
	RtYamlLines (Collection<YamlLine> lines) {
		this.lines = lines;
	}

	@Override
	public Iterator<YamlLine> iterator() {
		return this.lines.iterator();
	}

	@Override
	public YamlLines contained() {
		return null;
	}

	@Override
	public YamlMapping toYamlMapping() {
		return null;
	}

	@Override
	public YamlSequence toYamlSequence() {
		return null;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (YamlLine line : lines) {
			sb.append(line.toString()).append("\n");
		}
		return sb.toString();
	}
}
