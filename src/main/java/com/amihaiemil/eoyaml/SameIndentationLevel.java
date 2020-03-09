package com.amihaiemil.eoyaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

final class SameIndentationLevel implements YamlLines {

	/**
	 * All YamlLines, unaltered.
	 */
	private final AllYamlLines all;
	
	SameIndentationLevel(final AllYamlLines all) {
		this.all = all;
	}
	
    /**
     * Returns an iterator over these Yaml lines.
     * It <b>only</b> iterates over the lines which are at the same
     * level of indentation with the first!
     * @return Iterator over these yaml lines.
     */
     @Override
     public Iterator<YamlLine> iterator() {
         Iterator<YamlLine> iterator = this.all.iterator();
         if (iterator.hasNext()) {
             final List<YamlLine> sameIndentation = new ArrayList<>();
             final YamlLine first = iterator.next();
             sameIndentation.add(first);
             while (iterator.hasNext()) {
                 YamlLine current = iterator.next();
                 if(current.indentation() == first.indentation()) {
                     sameIndentation.add(current);
                 }
             }
             iterator = sameIndentation.iterator();
         }
         return iterator;
     }
	
	
	@Override
	public Collection<YamlLine> lines() {
		final List<YamlLine> lines = new ArrayList<>();
		for(final YamlLine line : this) {
			lines.add(line);
		}
		return lines;
	}

	@Override
	public int count() {
		return this.lines().size();
	}

}
